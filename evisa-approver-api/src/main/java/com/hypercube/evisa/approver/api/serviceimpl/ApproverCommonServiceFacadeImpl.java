package com.hypercube.evisa.approver.api.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.hypercube.evisa.approver.api.model.ApproverApplicationPreviewDTO;
import com.hypercube.evisa.approver.api.model.ApproverHistoryDetailsDTO;
import com.hypercube.evisa.approver.api.model.ApproverHistoryDetailsDTOList;
import com.hypercube.evisa.approver.api.repository.ArrivalDepartureHistoryRepository;
import com.hypercube.evisa.approver.api.service.ApproverCommonServiceFacade;
import com.hypercube.evisa.approver.api.service.ApproverHistoryDetailsService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicationHeader;
import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.domain.ApproverHistoryDetails;
import com.hypercube.evisa.common.api.domain.PayementCash;
import com.hypercube.evisa.common.api.model.AgeStatisticsDTO;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTOList;
import com.hypercube.evisa.common.api.model.ApplicationTrackerStatsAgeCountry;
import com.hypercube.evisa.common.api.model.AttachmentPreviewDTO;
import com.hypercube.evisa.common.api.model.AttachmentUrlList;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.DashboardDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ImmigrationOfficerStatisticsDTO;
import com.hypercube.evisa.common.api.model.IndexDTO;
import com.hypercube.evisa.common.api.model.MailDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardRequestDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardResponseDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.PreviewDTO;
import com.hypercube.evisa.common.api.model.VisaProcessingDTO;
import com.hypercube.evisa.common.api.repository.VisaCheckOverstayRepository;
import com.hypercube.evisa.common.api.service.ApplicantAttachmentService;
import com.hypercube.evisa.common.api.service.ApplicantPassportTravelDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantPaymentService;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionService;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
import com.hypercube.evisa.common.api.service.ProductConfigService;
import com.hypercube.evisa.common.api.service.VisaPenalityPayment;
import com.hypercube.evisa.common.api.util.CommonsUtil;
import com.hypercube.evisa.common.api.util.MediaReplacedElementFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.javase.QRCode;
import net.glxn.qrgen.core.image.ImageType;
import com.lowagie.text.DocumentException;

/**
 * @author SivaSreenivas
 */
@Service
@Data
@Slf4j
public class ApproverCommonServiceFacadeImpl implements ApproverCommonServiceFacade {

	@Value("${app.url}")
	private String appUrl;

	@Value("${applicant.url}")
	private String applicantUrl;

	@Value("${mail.sent.from}")
	private String mailFrom;

	private String appUrlHyp;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private ApplicationHeaderService applicationHeaderService;

	@Autowired
	private ApplicationTrackerService applicationTrackerService;

	@Autowired
	private ApplicantPersonalDetailsService applicantPersonalDetailsService;

	@Autowired
	private ApplicantPassportTravelDetailsService applicantPassportTravelDetailsService;

	@Autowired
	private ApplicantAttachmentService applicantAttachmentService;

	@Autowired
	private MasterCodeDetailsService masterCodeDetailsService;

	@Autowired
	private ApproverHistoryDetailsService approverHistoryDetailsService;

	@Autowired
	private ProductConfigService productConfigService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private VisaCheckOverstayRepository repositorycheckoverstay;

	@Autowired
	private ApplicantVisaExtensionService appVisaExtensionService;

	@Autowired
	ApplicantPaymentService applicantPaymentService;

	@Autowired
	private VisaPenalityPayment visaPenalityPayment;

	@Autowired
	private ArrivalDepartureHistoryRepository arrivalDepartureHistoryRepository;

	/**
	 * 
	 */
	@Override
	@Transactional
	public ApiResultDTO processNextSetOfFiles(String locale, String loggeduser) {
		log.info("ApproverCommonServiceFacadeImpl-processNextSetOfFiles");
		ApiResultDTO apiResultDTO;

		/* check the logged user is Decision Maker or not */

		/* check the pending allocated list count */
		int pendingCount = applicationHeaderService.checkPendingAllocatedList(loggeduser);

		if (pendingCount > 10) {
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "D'autres demandes sont en attente d'approbation");
		} else {

			/* get pending express visa */
			List<String> expressVisaAppList = applicationTrackerService.pendingVisaProcessList(true,
					PageRequest.of(0, 20));

			if (expressVisaAppList.size() == 20) {
				/* allocate all the applicationList to logged user */
				applicationHeaderService.allocateSubmittedApplications(loggeduser, expressVisaAppList);
				apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
						"L'allocation du prochain paquet de demandes s'est déroulée avec succès !");
			} else {

				/* get remaining pending visa after fetching express visa */
				List<String> normalVisaAppList = applicationTrackerService.pendingVisaProcessList(false,
						PageRequest.of(0, 20 - expressVisaAppList.size()));
				normalVisaAppList.addAll(expressVisaAppList);

				if (normalVisaAppList == null || normalVisaAppList.isEmpty()) {
					apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
							"Aucune demande en attente n'a été trouvée !");
				} else {
					applicationHeaderService.allocateSubmittedApplications(loggeduser, normalVisaAppList);
					apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
							"L'allocation du prochain paquet de demandes s'est déroulée avec succès !");
				}
			}
		}

		return apiResultDTO;
	}

	@Override
	public ApproverApplicationPreviewDTO applicationPreview(HttpServletRequest request, String locale,
			String applicationNumber) {
		log.info("ApproverCommonServiceFacadeImpl-applicationPreview");

		/* get personal information */
		ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsService
				.findByApplicationNumber(applicationNumber);

		/* get passport information */
		ApplicantPassportTravelDetails applicantPassportTravelDetails = applicantPassportTravelDetailsService
				.findPassportTravelInfoByApplicantionNumber(applicationNumber);

		/* get attachment information */
		ApplicantAttachmentDetailsDTOList appAttDtsList = applicantAttachmentService
				.attachmentsByApplicationNumber(applicationNumber);

		String attachmentUrl = appUrl + request.getContextPath() + "/v1/api/attachment/";

		List<AttachmentPreviewDTO> attchUrlList = appAttDtsList.getApplicantAttachmentDTOs().stream()
				.map(v -> new AttachmentPreviewDTO(v.getAttachmentType(),
						attachmentUrl + v.getAttachmentId() + "/" + v.getFileName()))
				.collect(Collectors.toList());

		/* get approver history information */
		List<ApproverHistoryDetails> approverHistoryList = approverHistoryDetailsService
				.findAllApplicationHistory(applicationNumber);

		/* consolidate all the master codes */
		List<String> codeList = new ArrayList<>();
		codeList.add(applicantPersonalDetails.getCurrentNationality());
		codeList.add(applicantPersonalDetails.getNationality());
		codeList.add(applicantPersonalDetails.getPreferredLanguage());
		codeList.add(applicantPersonalDetails.getResidenceCountry());
		codeList.add(applicantPersonalDetails.getBirthCountry());
		codeList.add(applicantPersonalDetails.getMaritalStatus());
		codeList.add(applicantPersonalDetails.getOriginCountry());

		List<String> visitedCountryCodeList = null;

		if (applicantPassportTravelDetails != null) {
			codeList.add(applicantPassportTravelDetails.getArrivalLocation());
			codeList.add(applicantPassportTravelDetails.getIssuedCountry());
			codeList.add(applicantPassportTravelDetails.getTravelPurpose());

			if (applicantPassportTravelDetails.getLastVisitedCountries() != null) {
				visitedCountryCodeList = Arrays
						.asList(applicantPassportTravelDetails.getLastVisitedCountries().split(","));
				for (String country : visitedCountryCodeList) {
					codeList.add(country);
				}
			}
		}

		if (approverHistoryList != null) {
			for (ApproverHistoryDetails apprHistDtls : approverHistoryList) {
				codeList.add(apprHistDtls.getApproverRole());
				codeList.add(apprHistDtls.getStatus());
			}
		}

		/* get master code details with consolidate master code list */
		List<MasterCodeResultDTO> masterCodeResultDTOList = masterCodeDetailsService.getMasterCodeLists(codeList);

		/* construct map object to identify the desc with code and code type */
		Map<IndexDTO, String> resultMap = new HashMap<>();
		for (MasterCodeResultDTO masterCodeDTO : masterCodeResultDTOList) {
			String value;
			if ("en".equalsIgnoreCase(locale)) {
				value = masterCodeDTO.getDescription();
			} else {
				value = masterCodeDTO.getDescriptionOther();
			}
			resultMap.put(new IndexDTO(masterCodeDTO.getCode(), masterCodeDTO.getCodeType()), value);
		}

		/* replace the code with description from result map */
		applicantPersonalDetails.setCurrentNationality(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getCurrentNationality(), CommonsConstants.CODETYPE_NTNLT)));
		applicantPersonalDetails.setNationality(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getNationality(), CommonsConstants.CODETYPE_NTNLT)));
		applicantPersonalDetails.setPreferredLanguage(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getPreferredLanguage(), CommonsConstants.CODETYPE_PRLNG)));
		applicantPersonalDetails.setResidenceCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getResidenceCountry(), CommonsConstants.CODETYPE_CNTRY)));
		applicantPersonalDetails.setBirthCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getBirthCountry(), CommonsConstants.CODETYPE_CNTRY)));
		applicantPersonalDetails.setMaritalStatus(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getMaritalStatus(), CommonsConstants.CODETYPE_MRT)));
		applicantPersonalDetails.setOriginCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getOriginCountry(), CommonsConstants.CODETYPE_CNTRY)));

		if (applicantPassportTravelDetails != null) {
			applicantPassportTravelDetails
					.setArrivalLocation(resultMap.get(new IndexDTO(applicantPassportTravelDetails.getArrivalLocation(),
							CommonsConstants.CODETYPE_ARLOC)));
			applicantPassportTravelDetails.setIssuedCountry(resultMap.get(
					new IndexDTO(applicantPassportTravelDetails.getIssuedCountry(), CommonsConstants.CODETYPE_CNTRY)));
			applicantPassportTravelDetails.setTravelPurpose(resultMap.get(
					new IndexDTO(applicantPassportTravelDetails.getTravelPurpose(), CommonsConstants.CODETYPE_POT)));

			if (visitedCountryCodeList != null && !visitedCountryCodeList.isEmpty()) {
				List<String> visitedCountryDescList = new ArrayList<>();
				for (String countryCode : visitedCountryCodeList) {
					visitedCountryDescList
							.add(resultMap.get(new IndexDTO(countryCode, CommonsConstants.CODETYPE_CNTRY)));
				}
				applicantPassportTravelDetails.setLastVisitedCountries(String.join(",", visitedCountryDescList));
			} else {
				applicantPassportTravelDetails.setLastVisitedCountries("");
			}
		}

		List<ApproverHistoryDetails> apprHistDtlsDesc = new ArrayList<>();
		if (approverHistoryList != null) {
			for (ApproverHistoryDetails apprHistDtls : approverHistoryList) {
				apprHistDtls.setApproverRole(
						resultMap.get(new IndexDTO(apprHistDtls.getApproverRole(), CommonsConstants.CODETYPE_ROLE)));
				apprHistDtls.setStatus(
						resultMap.get(new IndexDTO(apprHistDtls.getStatus(), CommonsConstants.CODETYPE_STATUS)));
				apprHistDtlsDesc.add(apprHistDtls);
			}
		}

		return new ApproverApplicationPreviewDTO(applicantPersonalDetails, applicantPassportTravelDetails,
				new AttachmentUrlList(attchUrlList), new ApproverHistoryDetailsDTOList(apprHistDtlsDesc));
	}

	/**
	 * 
	 */
	@Override
	public ApplicantAttachmentDetails getAttachment(Long id) {
		log.info("ApproverCommonServiceFacadeImpl-getAttachment");
		return applicantAttachmentService.findAttachmentById(id);
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public ApiResultDTO processApproval(String locale, ApproverHistoryDetailsDTO approverHistoryDetailsDTO) {
		log.info("ApproverCommonServiceFacadeImpl-processApproval");

		ApiResultDTO apiResultDTO;

		try {

			ApplicationHeader appHeader = applicationHeaderService
					.getHeaderApplication(approverHistoryDetailsDTO.getApplicationNumber());

			/*
			 * Need to verify whether the application is allocated to same person who
			 * provided approval
			 */
			if (appHeader.getAssignedTo().equals(approverHistoryDetailsDTO.getApprover())
					|| "VAL".equals(appHeader.getDocumentStatus())) {
				appHeader.setApplicationNumber(approverHistoryDetailsDTO.getApplicationNumber());
				/* need to update approval details to header if its APR/REJ */
				if ("APR".equals(approverHistoryDetailsDTO.getStatus())
						|| "REJ".equals(approverHistoryDetailsDTO.getStatus())) {
					Date currentDate = new Date();
					/* set the update application header details */
					appHeader.setDocumentStatus("CLS");
					appHeader.setClosedDate(currentDate);
					appHeader.setVisaStatus(approverHistoryDetailsDTO.getStatus());
					if ("APR".equals(approverHistoryDetailsDTO.getStatus())) {
						int durationCount = applicationTrackerService
								.getVisaDuration(approverHistoryDetailsDTO.getApplicationNumber());

						LocalDate arrivalDate = applicationTrackerService.getArrivalDate(approverHistoryDetailsDTO.getApplicationNumber());

						// Spécifiez le fuseau horaire souhaité
						ZoneId zoneId = ZoneId.systemDefault();

						// Convertissez LocalDate en Date
						Date date = Date.from(arrivalDate.atStartOfDay(zoneId).toInstant());

						String validityDaysCountStr = productConfigService
								.getConfigValueByConfigCode("VISA_VALID_DAYS");
						int validityDaysCount = Integer
								.parseInt(validityDaysCountStr != null ? validityDaysCountStr : "90");

								System.out.println("arrival date -----------------------------------"   +arrivalDate);

						appHeader.setVisaValidFrom(date);
						appHeader.setVisaValidDuration(durationCount);
						appHeader.setVisaValidTo(CommonsUtil.addDays(date, validityDaysCount));
						appHeader.setArrDepIndicator("PA");
					}
				} else if ("VAL".equals(approverHistoryDetailsDTO.getStatus())) {
					appHeader.setDocumentStatus("VAL");
				}

				System.out.println("------------------------------------------------"+appHeader.toString());

				applicationHeaderService.updateApprovalDetails(appHeader);

				approverHistoryDetailsService.saveApproverHistoryDetails(
						modelMapper.map(approverHistoryDetailsDTO, ApproverHistoryDetails.class));
				apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Processed Successfully!");
			} else {
				apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
						"Application is not assigned or might be deallocated");
			}
			//
			/* drop the approval details (APR/REJ) to queue for sending to the applicant */

			// if ("APR".equals(approverHistoryDetailsDTO.getStatus())
			// 		|| "REJ".equals(approverHistoryDetailsDTO.getStatus())) {
			// 	ApplicationTracker applicationTracker = applicationTrackerService
			// 			.getApplicationDetails(approverHistoryDetailsDTO.getApplicationNumber());
			// 	Map<String, Object> model = new HashMap<String, Object>();
			// 	model.put("title",
			// 			"Application| Demande - "
			// 					+ (approverHistoryDetailsDTO.getStatus().equals("APR") ? "Approved | Approuvée"
			// 							: "Rejected | Rejetter"));
			// 	model.put("name", applicationTracker.getGivenName());
			// 	model.put("applicationNumber", applicationTracker.getApplicationNumber());
			// 	model.put("visa_type", applicationTracker.getVistaDescOther());
			// 	model.put("message", "Your Application N°." + applicationTracker.getApplicationNumber() + " has been "
			// 			+ (approverHistoryDetailsDTO.getStatus().equals(
			// 					"APR") ? "Approved, Please visit the Djib Evisa website to download the approved visa. " + " < br > " + " Votre demande N°." + applicationTracker.getApplicationNumber() + " a été approuvé, veuillez visiter le site web de Djib Evisa pour télécharger le visa approuvé. " : "Rejected, Please visit the Djib Evisa website to know the reasons for visa rejection. " + " < br > " + " Rejeté, Veuillez visiter le site web de Djib Evisa pour connaître les raisons du rejet du visa."));
			// 	jmsTemplate.convertAndSend("mailbox",
			// 			new MailDTO(mailFrom, applicationTracker.getEmailAddress(),
			// 					"Djib E-Visa Approval - " + applicationTracker.getApplicationNumber(),
			// 					"visa-approval.ftl", model));
			// 	log.info("Email envoyé avec succès à {}", applicationTracker.getEmailAddress());
			// }

			if ("APR".equals(approverHistoryDetailsDTO.getStatus())) {
				ApplicationTracker applicationTracker = applicationTrackerService
						.getApplicationDetails(approverHistoryDetailsDTO.getApplicationNumber());
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("title",
						"Application| Demande - "
								+ (approverHistoryDetailsDTO.getStatus().equals("APR") ? "Approved | Approuvée"
										: "Rejected | Rejetter"));
				model.put("name", applicationTracker.getGivenName());
				model.put("applicationNumber", applicationTracker.getApplicationNumber());
				model.put("visa_type", applicationTracker.getVistaDescOther());
				jmsTemplate.convertAndSend("mailbox",
						new MailDTO(mailFrom, "support.evisa@evisa.dj",
								"Nouvel demande d'approbation - N°" + applicationTracker.getApplicationNumber(),
								"new-visa-approval.ftl", model));
			}

		} catch (Exception exe) {
			exe.printStackTrace();
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "Error While Processing, Contact Admin!");
		}
		return apiResultDTO;
	}

	/**
	 *
	 */
	@Override
	public DashboardDTO decisionMakerDashboard(String loggeduser, String period) {
		log.info("ApproverCommonServiceFacadeImpl-decisionMakerDashboard merde");

		Map<String, Long> countMapResult = applicationHeaderService.decisionMakerDashboard(loggeduser, period);

		DashboardDTO dashboardDTO = new DashboardDTO();
		dashboardDTO.setPending(countMapResult.get(CommonsConstants.PENDING));
		dashboardDTO.setTotalAllocated(countMapResult.get(CommonsConstants.ALLOCATED));
		dashboardDTO.setApproved(countMapResult.get(CommonsConstants.APPROVED));
		dashboardDTO.setValidation(countMapResult.get(CommonsConstants.VALIDATION));
		dashboardDTO.setRejected(countMapResult.get(CommonsConstants.REJECTED));
		/* dashboardDTO.setClosed(countMapResult.get(CommonsConstants.CLOSED)); */
		/*
		 * dashboardDTO.setTotalAllocated(
		 * dashboardDTO.getClosed() + dashboardDTO.getPending() +
		 * countMapResult.get(CommonsConstants.VALIDATION));
		 */
		return dashboardDTO;
	}

	/**
	 *
	 */
	@Override
	public ApiResultDTO deallocateSelectedApplications(List<String> applicationlist) {
		log.info("ApproverCommonServiceFacadeImpl-deallocateApplications");

		applicationHeaderService.deallocateSelectedApplications(applicationlist);
		return new ApiResultDTO(CommonsConstants.SUCCESS, "Deallocated Selected Applications Successfully!");
	}

	/**
	 *
	 */
	@Override
	public ApproverApplicationPreviewDTO applicationDeparturePreview(HttpServletRequest request, String locale,
			String applicationNumber) {
		log.info("ApproverCommonServiceFacadeImpl-applicationDeparturePreview");

		/* get application header information */
		ApplicationHeader appHeader = applicationHeaderService.getHeaderApplication(applicationNumber);
		/* get personal information */
		ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsService
				.findByApplicationNumber(applicationNumber);
		/* get passport information */
		ApplicantPassportTravelDetails applicantPassportTravelDetails = applicantPassportTravelDetailsService
				.findPassportTravelInfoByApplicantionNumber(applicationNumber);
		/* get attachment information */
		List<ApplicantAttachmentDetails> appAttDtsList = applicantAttachmentService
				.findAttchDtlsByAppNoAndAttchType(applicationNumber, "PG");
		String attachmentUrl = appUrl + request.getContextPath() + "/v1/api/attachment/";
		List<AttachmentPreviewDTO> attchUrlList = appAttDtsList.stream()
				.map(v -> new AttachmentPreviewDTO(v.getAttachmentType(),
						attachmentUrl + v.getAttachmentId() + "/" + v.getFileName()))
				.collect(Collectors.toList());
		long daysDiff = 0;
		long penaltyAmount = 0;
		// Date expiredDate = null;
		if ("PD".equals(appHeader.getArrDepIndicator())) {
			/*
			 * expiredDate = (appHeader.getExtensionApplied().equals("Y")) ?
			 * appVisaExtensionService.getNewExipredDate(applicationNumber) :
			 * appHeader.getVisaExpiry();
			 */
			long timeDiff = 0;
			long currentDate = new Date().getTime();
			// long visaToDate = (expiredDate == null ? appHeader.getVisaExpiry() :
			// expiredDate).getTime();
			long visaToDate = appHeader.getVisaExpiry().getTime();
			if (currentDate > visaToDate) {
				timeDiff = currentDate - visaToDate;
			}
			daysDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
			log.info("diffDays -=-=-=-=- {}", daysDiff);

			if (daysDiff > 0) {
				/* need to calculate the penalty amount */
				String penaltyValue = productConfigService.getConfigValueByConfigCode("VISA_PNLTY_CHRE");
				log.info("penaltyValue VISA_PNLTY_CHRE -=-=-=- {}", penaltyValue);
				penaltyAmount = daysDiff * Integer.parseInt(penaltyValue != null ? penaltyValue : "20");
			}
			long penalityPaid;
			ApplicantPaymentDetails applicationPayementDetails = applicantPaymentService
					.fetchApplicantPaymentDetails(applicationNumber);
			if (applicationPayementDetails != null) {
				if (applicationPayementDetails.getFileNumber().equals(applicationNumber)) {
					penalityPaid = applicationPayementDetails.getAmountPaid();
					penaltyAmount = penaltyAmount - penalityPaid;
				} else {

				}
			}
			// String dateString = "2023-05-28";
			// String date2string="2023-06-25";
			// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-d");
			// Date date1 = null;
			// Date date2 = null;
			// try {
			// date1 = format.parse(dateString);
			// date2= format.parse(date2string);
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			//// PayementCash test=visaPenalityPayment.statistiqueCount("Secretaire", date1,
			// date2);
			//// System.out.println("testPayement count secretaire"+test);
			//// PayementCash test1=visaPenalityPayment.StatistiqueSum("Secretaire", date1,
			// date2);
			//// System.out.println("testPayement count secretaire"+test+"secretairee
			// amount"+test1);
			PayementCash payementcash = visaPenalityPayment.selectpayementCash(applicationNumber);
			if (payementcash != null) {
				penaltyAmount = (long) (penaltyAmount - payementcash.getAmount());
			}
			if (penaltyAmount < 0) {
				penaltyAmount = 0;
			} else {

			}
			log.info("penaltyAmount -=-=-=-=- {}", penaltyAmount);
		}
		/* consolidate all the master codes */
		List<String> codeList = new ArrayList<>();
		codeList.add(applicantPersonalDetails.getCurrentNationality());
		codeList.add(applicantPersonalDetails.getNationality());
		codeList.add(applicantPersonalDetails.getPreferredLanguage());
		codeList.add(applicantPersonalDetails.getResidenceCountry());
		codeList.add(applicantPersonalDetails.getBirthCountry());
		codeList.add(applicantPersonalDetails.getMaritalStatus());
		codeList.add(applicantPersonalDetails.getOriginCountry());

		List<String> visitedCountryCodeList = null;
		if (applicantPassportTravelDetails != null) {
			codeList.add(applicantPassportTravelDetails.getArrivalLocation());
			codeList.add(applicantPassportTravelDetails.getIssuedCountry());
			codeList.add(applicantPassportTravelDetails.getTravelPurpose());
			if (applicantPassportTravelDetails.getLastVisitedCountries() != null) {
				visitedCountryCodeList = Arrays
						.asList(applicantPassportTravelDetails.getLastVisitedCountries().split(","));
				for (String country : visitedCountryCodeList) {
					codeList.add(country);
				}
			}
		} else {
			log.info("applicantPassportTravelDetails is NULL - not set code");
		}

		/* get master code details with consolidate master code list */
		List<MasterCodeResultDTO> masterCodeResultDTOList = masterCodeDetailsService.getMasterCodeLists(codeList);

		/* construct map object to identify the desc with code and code type */
		Map<IndexDTO, String> resultMap = new HashMap<>();
		for (MasterCodeResultDTO masterCodeDTO : masterCodeResultDTOList) {
			String value;
			if ("en".equalsIgnoreCase(locale)) {
				value = masterCodeDTO.getDescription();
			} else {
				value = masterCodeDTO.getDescriptionOther();
			}
			resultMap.put(new IndexDTO(masterCodeDTO.getCode(), masterCodeDTO.getCodeType()), value);
		}

		/* replace the code with description from result map */
		applicantPersonalDetails.setCurrentNationality(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getCurrentNationality(), CommonsConstants.CODETYPE_NTNLT)));
		applicantPersonalDetails.setNationality(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getNationality(), CommonsConstants.CODETYPE_NTNLT)));
		applicantPersonalDetails.setPreferredLanguage(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getPreferredLanguage(), CommonsConstants.CODETYPE_PRLNG)));
		applicantPersonalDetails.setResidenceCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getResidenceCountry(), CommonsConstants.CODETYPE_CNTRY)));
		applicantPersonalDetails.setBirthCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getBirthCountry(), CommonsConstants.CODETYPE_CNTRY)));
		applicantPersonalDetails.setMaritalStatus(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getMaritalStatus(), CommonsConstants.CODETYPE_MRT)));
		applicantPersonalDetails.setOriginCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getOriginCountry(), CommonsConstants.CODETYPE_CNTRY)));
		if (applicantPassportTravelDetails != null) {
			applicantPassportTravelDetails
					.setArrivalLocation(resultMap.get(new IndexDTO(applicantPassportTravelDetails.getArrivalLocation(),
							CommonsConstants.CODETYPE_ARLOC)));
			applicantPassportTravelDetails.setIssuedCountry(resultMap.get(
					new IndexDTO(applicantPassportTravelDetails.getIssuedCountry(), CommonsConstants.CODETYPE_CNTRY)));
			applicantPassportTravelDetails.setTravelPurpose(resultMap.get(
					new IndexDTO(applicantPassportTravelDetails.getTravelPurpose(), CommonsConstants.CODETYPE_POT)));
			if (visitedCountryCodeList != null && !visitedCountryCodeList.isEmpty()) {
				List<String> visitedCountryDescList = new ArrayList<>();
				for (String countryCode : visitedCountryCodeList) {
					visitedCountryDescList
							.add(resultMap.get(new IndexDTO(countryCode, CommonsConstants.CODETYPE_CNTRY)));
				}
				applicantPassportTravelDetails.setLastVisitedCountries(String.join(",", visitedCountryDescList));
			}
		} else {
			log.info("applicantPassportTravelDetails is NULL - not set desc");
		}
		return new ApproverApplicationPreviewDTO(applicationNumber, appHeader.getVisaExpiry(),
				appHeader.getExtensionApplied().equals("Y") ? true : false, daysDiff > 0, penaltyAmount,
				applicantPersonalDetails, applicantPassportTravelDetails, new AttachmentUrlList(attchUrlList));
	}

	/**
	 * @param monthNummber
	 * @return
	 */
	private String getMonthMapping(String monthNummber) {
		log.info("ApproverCommonServiceFacadeImpl-getMonthMapping");
		List<String> monthsList = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT",
				"NOV", "DEC");
		int value = Integer.parseInt(monthNummber);
		return (value >= 1 && value <= 12) ? monthsList.get(value - 1) : "";
	}

	/**
	 *
	 */
	@Override
	public AgentTrackerDTO borderControlDashboardStatistics(GenericSearchDTO genericSearchDTO) {
		log.info("ApproverCommonServiceFacadeImpl-borderControlDashboardStatistics");

		return applicationHeaderService.borderControlDashboardStatistics(genericSearchDTO);
	}

	/**
	 *
	 */
	@Override
	public Page<ApplicationTracker> agentTrackerForSBCO(GenericSearchDTO genericSearchDTO) {
		log.info("ApproverCommonServiceFacadeImpl-agentTrackerForSBCO");

		List<String> applicationNumberList = applicationHeaderService.agentTrackerForSBCO(genericSearchDTO);

		return applicationTrackerService.searchApplicationsForBCO(applicationNumberList, genericSearchDTO);

	}

	/**
	 *
	 */
	@Override
	public ManagerDashboardResponseDTO immigrationOfficerDashboard(ManagerDashboardRequestDTO managerDashboardDTO) {
		log.info("ApproverCommonServiceFacadeImpl-agentTrackerForSBCO");

		/* get DMM count details */
		AgentTrackerDTO agentTrackerDTO = applicationTrackerService.immigrationOfficerCount(managerDashboardDTO);

		/* get DMM Stats */
		ImmigrationOfficerStatisticsDTO immigrationStatsDTO = immigrationOfficerStatistics(managerDashboardDTO);
		ApplicationTrackerStatsAgeCountry statsageCountry = nOfficerStatistics(managerDashboardDTO);
		return new ManagerDashboardResponseDTO(agentTrackerDTO, immigrationStatsDTO, statsageCountry);
	}

	public ImmigrationOfficerStatisticsDTO immigrationOfficerStatistics(
			ManagerDashboardRequestDTO managerDashboardDTO) {
		log.info("ApproverCommonServiceFacadeImpl-immigrationOfficerStatistics");

		GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(managerDashboardDTO.getPeriod());

		/* get visa application process statistics */
		List<CountDTO> appProcessStats = applicationTrackerService.applicationProcessStatistics(
				CommonsUtil.setMonthToJavaUtilDate(new Date(), -3), managerDashboardDTO.getCountryList());

		List<VisaProcessingDTO> visaProcessingDTOs = new ArrayList<>();
		if (appProcessStats != null && !appProcessStats.isEmpty()) {
			Map<String, List<CountDTO>> mapVisaProcessDTOs = new HashMap<>();
			for (CountDTO countDTO : appProcessStats) {
				boolean result = mapVisaProcessDTOs.containsKey(getMonthMapping(countDTO.getAgentUsername()));
				if (result) {
					List<CountDTO> existCountDTO = mapVisaProcessDTOs.get(getMonthMapping(countDTO.getAgentUsername()));
					existCountDTO.add(new CountDTO(countDTO.getStatusCode(), countDTO.getCount()));
					mapVisaProcessDTOs.put(getMonthMapping(countDTO.getAgentUsername()), existCountDTO);
				} else {
					List<CountDTO> mergeCountDTO = new ArrayList<>();
					mergeCountDTO.add(new CountDTO(countDTO.getStatusCode(), countDTO.getCount()));
					mapVisaProcessDTOs.put(getMonthMapping(countDTO.getAgentUsername()), mergeCountDTO);
				}
			}

			for (Entry<String, List<CountDTO>> resultDTO : mapVisaProcessDTOs.entrySet()) {
				visaProcessingDTOs.add(new VisaProcessingDTO(resultDTO.getKey(), resultDTO.getValue()));
			}
		}

		/* get visa type process statistics */
		List<CountDTO> travelPurposeStats = applicationTrackerService.travelPurposeStatistics(
				managerDashboardDTO.getCountryList(), genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());

		/* get country process statistics */
		if (managerDashboardDTO.getCountryList() == null || managerDashboardDTO.getCountryList().isEmpty()) {
			managerDashboardDTO.setCountryList(Arrays.asList("FRA", "ETH", "CHN", "IND"));
		}
		List<CountDTO> countryStats = applicationTrackerService.countryStatistics(managerDashboardDTO.getCountryList(),
				genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());
		/* get age process statistics */
		// List<CountDTO> top5countryStats =
		// applicationTrackerService.top5countryStatistics(genericSearchDTO.getStartDate(),genericSearchDTO.getEndDate());

		/* get age process statistics */
		AgeStatisticsDTO ageStats = applicationTrackerService.ageStatistics(managerDashboardDTO.getCountryList(),
				genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());
		/* get age process statistics */
		// AgeStatisticsDTO ageStatss
		// =applicationTrackerService.ageStatisticsAll(genericSearchDTO.getStartDate(),
		// genericSearchDTO.getEndDate());

		return new ImmigrationOfficerStatisticsDTO(visaProcessingDTOs, travelPurposeStats, countryStats, ageStats);
	}

	public ApplicationTrackerStatsAgeCountry nOfficerStatistics(ManagerDashboardRequestDTO managerDashboardDTO) {
		log.info("ApproverCommonServiceFacadeImpl-immigrationOfficerStatistics");

		GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(managerDashboardDTO.getPeriod());

		List<CountDTO> top5countryStats = applicationTrackerService
				.top5countryStatistics(genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());

		AgeStatisticsDTO ageStatss = applicationTrackerService.ageStatisticsAll(genericSearchDTO.getStartDate(),
				genericSearchDTO.getEndDate());

		return new ApplicationTrackerStatsAgeCountry(top5countryStats, ageStatss);
	}

	public long overstayStatistics() {
		log.info("ApproverCommonServiceFacadeImpl-overstayStatistics");

		return applicationHeaderService.overstayStatistics();
	}

	@Override
	public List<ApplicationOverStayDetails> Checkoverstay() {
		List<ApplicationOverStayDetails> checkList = repositorycheckoverstay.findAll();

		return checkList;
	}

	@Override
	public ResponseEntity<Resource> downloadApprovedVisaPdf(String applicationNumber, HttpServletRequest request) {
		log.info("ApproverCommonServiceFacadeImpl - downloadApprovedVisaPdf: {}", applicationNumber);

		try {
			log.info("Étape 1: Début du processus de téléchargement pour l'application: {}", applicationNumber);

			PreviewDTO previewDTO = getApplicationDetailsForDownload(applicationNumber);
			log.info("Étape 2: Données récupérées avec succès pour l'application: {}", applicationNumber);

			String htmlContent = parseThymeleafTemplateForVisaDownload(previewDTO, request);
			log.info("Étape 3: HTML généré avec succès, taille: {} caractères", htmlContent.length());

			byte[] pdfBytes = generatePdfFromHtml(htmlContent, applicationNumber);
			log.info("Étape 4: PDF généré avec succès, taille: {} bytes", pdfBytes.length);

			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"visa-" + applicationNumber + ".pdf\"")
					.body(new ByteArrayResource(pdfBytes));

		} catch (Exception e) {
			log.error("ERREUR lors de la génération du PDF pour le visa: {}", applicationNumber, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private PreviewDTO getApplicationDetailsForDownload(String applicationNumber) {
		log.info("ApproverCommonServiceFacadeImpl - getApplicationDetailsForDownload: {}", applicationNumber);

		ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsService
				.findByApplicationNumber(applicationNumber);
		if (applicantPersonalDetails == null) {
			throw new RuntimeException("Aucune information personnelle trouvée pour l'application: " + applicationNumber);
		}

		ApplicantPassportTravelDetails applicantPassportTravelDetails = applicantPassportTravelDetailsService
				.findPassportTravelInfoByApplicantionNumber(applicationNumber);
		if (applicantPassportTravelDetails == null) {
			throw new RuntimeException("Aucune information de passeport trouvée pour l'application: " + applicationNumber);
		}

		ApplicationTracker applicationTracker = applicationTrackerService.getApplicationDetails(applicationNumber);
		if (applicationTracker == null) {
			throw new RuntimeException("Aucun tracker d'application trouvé pour: " + applicationNumber);
		}

		List<ApplicantAttachmentDetails> applAttchDtls = applicantAttachmentService
				.findAttchDtlsByAppNoAndAttchType(applicationNumber, "PG");
		if (applAttchDtls == null || applAttchDtls.isEmpty()) {
			throw new RuntimeException("Aucune photo trouvée pour l'application: " + applicationNumber);
		}
		byte[] fileData = applAttchDtls.get(0).getFileData();

		PreviewDTO previewDTO = new PreviewDTO(applicantPersonalDetails, applicantPassportTravelDetails, applicationTracker);
		previewDTO.setPhotograph(fileData);

		return previewDTO;
	}

	private String parseThymeleafTemplateForVisaDownload(PreviewDTO previewDTO, HttpServletRequest request) throws IOException {
		log.info("ApproverCommonServiceFacadeImpl - parseThymeleafTemplateForVisaDownload");

		ApplicationHeader applicationHeader = applicationHeaderService
				.getHeaderApplication(previewDTO.getApplicantPersonalDetails().getApplicationNumber());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		LocalDate issuedDate = previewDTO.getApplicantPassportTravelDetails().getIssuedDate();
		LocalDate expiryDate = previewDTO.getApplicantPassportTravelDetails().getExpiryDate();
		LocalDate dob = previewDTO.getApplicantPersonalDetails().getDateOfBirth();

		String issuedDateFormatted = issuedDate.format(formatter);
		String expiryDateFormatted = expiryDate.format(formatter);
		String dateOfBirth = dob.format(formatter);

		// Générer QR Code, Avatar et Barcode (comme dans applicant-api)
		String codeContent = "REF NO: " + previewDTO.getApplicantPersonalDetails().getApplicationNumber() + "\n"
				+ "DJIBOUTI, Le: " + previewDTO.getApplicationTracker().getClosedDate() + "\n"
				+ "NOM ET PRENOM: " + previewDTO.getApplicantPersonalDetails().getGivenName().toUpperCase() + " "
				+ previewDTO.getApplicantPersonalDetails().getSurname().toUpperCase() + "\n"
				+ "NATIONALITE: " + previewDTO.getApplicantPersonalDetails().getNationality().toUpperCase() + "\n"
				+ "NUMERO DE PASSEPORT: " + previewDTO.getApplicantPassportTravelDetails().getPassportNumber().toUpperCase() + "\n"
				+ "A /AU: " + previewDTO.getApplicantPassportTravelDetails().getIssuedCountry().toUpperCase() + "\n"
				+ "DELIVRE LE: " + issuedDateFormatted + "\n"
				+ "EXPIRE LE: " + expiryDateFormatted + "\n"
				+ "DUREE DU SEJOUR: " + previewDTO.getApplicationTracker().getVisaDuration() + " JOURS" + "\n"
				+ appUrl + "/v1/backoffice/visa/download/"
				+ previewDTO.getApplicantPersonalDetails().getApplicationNumber();

		File directory = new File(appUrlHyp + "evisa/common/rel1_0/barcode/"
				+ previewDTO.getApplicantPersonalDetails().getApplicationNumber());
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Génération du QR Code
		ByteArrayOutputStream stream = QRCode.from(codeContent).withSize(75, 80).to(ImageType.PNG).stream();
		File qrFile = new File(directory, "QRCode.png");
		if (!qrFile.exists()) {
			try (FileOutputStream qrfos = new FileOutputStream(qrFile)) {
				qrfos.write(stream.toByteArray());
				qrfos.flush();
			}
		}

		// Génération de l'avatar
		File avatarFile = new File(directory, "avatar.png");
		if (!avatarFile.exists()) {
			try (FileOutputStream avatarfos = new FileOutputStream(avatarFile)) {
				avatarfos.write(previewDTO.getPhotograph());
				avatarfos.flush();
			}
		}

		// Génération du code-barres
		Code128Bean bean = new Code128Bean();
		final int dpi = 160;
		bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
		bean.doQuietZone(false);

		BitmapCanvasProvider canvas = new BitmapCanvasProvider(300, BufferedImage.TYPE_BYTE_GRAY, true, 0);
		bean.generateBarcode(canvas, previewDTO.getApplicantPersonalDetails().getApplicationNumber());
		BufferedImage barcodeImage = canvas.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(barcodeImage, "png", baos);

		File barcodeFile = new File(directory, "barcode.png");
		if (!barcodeFile.exists()) {
			try (FileOutputStream barcodefos = new FileOutputStream(barcodeFile)) {
				barcodefos.write(baos.toByteArray());
				barcodefos.flush();
			}
		}

		// Configuration Thymeleaf
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context();
		context.setVariable("applicationNumber", previewDTO.getApplicantPersonalDetails().getApplicationNumber());
		context.setVariable("fileNumber", previewDTO.getApplicationTracker().getFileNumber().toUpperCase());
		context.setVariable("visaEntry", previewDTO.getApplicationTracker().getVistaDescOther().toUpperCase());
		context.setVariable("firstName", previewDTO.getApplicantPersonalDetails().getGivenName().toUpperCase());
		context.setVariable("lastName", previewDTO.getApplicantPersonalDetails().getSurname().toUpperCase());
		context.setVariable("dateOfBirth", dateOfBirth);
		context.setVariable("gender", previewDTO.getApplicantPersonalDetails().getGender().toUpperCase());
		context.setVariable("nationality", previewDTO.getApplicantPersonalDetails().getCurrentNationality() != null ?
				previewDTO.getApplicantPersonalDetails().getCurrentNationality().toUpperCase() : "N/A");
		context.setVariable("passportNo", previewDTO.getApplicantPassportTravelDetails().getPassportNumber().toUpperCase());
		context.setVariable("issueDate", issuedDate);
		context.setVariable("expiryDate", expiryDate);
		context.setVariable("reasonsForTravel", previewDTO.getApplicantPassportTravelDetails().getTravelPurpose() != null ?
				previewDTO.getApplicantPassportTravelDetails().getTravelPurpose().toUpperCase() : "N/A");

		int visaDuration = previewDTO.getApplicationTracker().getVisaDuration();
		if (visaDuration == 90) {
			context.setVariable("durationStay", "90 Jours");
			context.setVariable("warningMessage", "* The stay date can't exceed the validity date / Le séjour ne peut pas dépasser la date de validité.");
		} else {
			context.setVariable("durationStay", visaDuration + " JOURS ");
			context.setVariable("warningMessage", "");
		}

		context.setVariable("entryType",
				"S".equals(previewDTO.getApplicationTracker().getEntryType()) ? "UNE SEULE ENTREE" : "MULTIPLE ENTREE");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		context.setVariable("visaValidFrom", previewDTO.getApplicantPassportTravelDetails().getArrivalDate().format(dtf));
		context.setVariable("visaValidTo", previewDTO.getApplicantPassportTravelDetails().getDepartureDate().format(dtf));

		context.setVariable("group", previewDTO.getApplicationTracker().getApplicantType().equals("G") ? "YES" : "NO");

		if (applicationHeader.getExtensionApplied().equals("Y")) {
			String extensionType = appVisaExtensionService.getExtensionTypeByApplicationNumber(
					previewDTO.getApplicantPersonalDetails().getApplicationNumber());
			context.setVariable("extensionType", extensionType == null ? "N/A" : extensionType);
			context.setVariable("extensionExpiry",
					extensionType == null ? "N/A" : sdf.format(applicationHeader.getVisaExpiry()));
		} else {
			context.setVariable("extensionType", "N/A");
			context.setVariable("extensionExpiry", "N/A");
		}

		return templateEngine.process("templates/visa-appl-download", context);
	}

	private byte[] generatePdfFromHtml(String html, String applicationNumber) throws DocumentException, IOException {
		log.info("ApproverCommonServiceFacadeImpl - generatePdfFromHtml pour l'application: {}", applicationNumber);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ITextRenderer renderer = new ITextRenderer();
			log.info("Traitement du HTML, taille: {} caractères", html.length());

			if (applicationNumber != null) {
				renderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(
						renderer.getSharedContext().getReplacedElementFactory(), applicationNumber));
			}

			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.createPDF(bos);

			log.info("PDF généré avec succès, taille: {} bytes", bos.size());
			return bos.toByteArray();

		} catch (Exception e) {
			log.error("ERREUR dans generatePdfFromHtml pour l'application: {}", applicationNumber, e);
			throw e;
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				log.warn("Erreur lors de la fermeture du ByteArrayOutputStream", e);
			}
		}
	}
}
