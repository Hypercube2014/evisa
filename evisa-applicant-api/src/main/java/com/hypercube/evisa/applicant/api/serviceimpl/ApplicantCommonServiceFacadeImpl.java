package com.hypercube.evisa.applicant.api.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.model.Event;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.hypercube.evisa.applicant.api.model.ApplicationFileDTO;
import com.hypercube.evisa.applicant.api.model.CheckVisaResultDTO;
import com.hypercube.evisa.applicant.api.service.ApplicantCommonServiceFacade;
import com.hypercube.evisa.applicant.api.service.ApplicantFileService;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicationHeader;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTOList;
import com.hypercube.evisa.common.api.model.ChargeRequestDTO;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.CheckOutPaymentDTO;
import com.hypercube.evisa.common.api.model.GenericGroupCountDTO;
import com.hypercube.evisa.common.api.model.IndexDTO;
import com.hypercube.evisa.common.api.model.InvoiceDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;
import com.hypercube.evisa.common.api.model.PreviewDTO;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;
import com.hypercube.evisa.common.api.service.ApplicantAttachmentService;
import com.hypercube.evisa.common.api.service.ApplicantPassportTravelDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantPaymentService;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionService;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;
import com.hypercube.evisa.common.api.service.ManagementVisaDetailsService;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
import com.hypercube.evisa.common.api.service.ProductConfigService;
import com.hypercube.evisa.common.api.service.StripeService;
import com.hypercube.evisa.common.api.util.MediaReplacedElementFactory;
import com.lowagie.text.DocumentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicantCommonServiceFacadeImpl implements ApplicantCommonServiceFacade {

	/**
	 * 
	 */
	@Value("${app.url}")
	private String appUrl;

	@Value("${app.url.hyp}")
	private String appUrlHyp;

	@Value("${app.download}")
	private String appDownload;
	/**
	 * 
	 */

	@Autowired(required = true)
	private ProductConfigService productConfigService;

	@Autowired(required = true)
	private ApplicantPersonalDetailsService applicantPersonalDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantPassportTravelDetailsService applicantPassportTravelDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantAttachmentService applicantAttachmentService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ManagementVisaDetailsService managementVisaDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationHeaderService applicationHeaderService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantFileService applicationFileService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantPaymentService applicantPaymentService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private MasterCodeDetailsService masterCodeDetailsService;

	/**
	 * 
	 */
	@Value("${stripe.secret.key}")
	private String stripeSecretKey;

	/**
	 * 
	 */
	/**
	 * Secret webhook Stripe pour vérification des signatures
	 */
	@Value("${stripe.webhook.secret:}")
	private String stripeWebhookSecret;

	@Autowired(required = true)
	private StripeService stripeService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationTrackerService applicationTrackerService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantVisaExtensionService applicantVisaExtensionService;

	/**
	 * 
	 */
	@Override
	@Transactional
	public ResponseEntity<ApiResultDTO> applicationSubmission(String locale, ApplicationFileDTO applicationFileDTO) {
		log.info("ApplicantCommonServiceFacadeImpl-applicationSubmission");

		ApiResultDTO apiResultDTO;

		List<String> strErrorList = new ArrayList<>();

		/* get all applications associated to the file number */
		List<String> applicationList = applicantPersonalDetailsService
				.findApplications(applicationFileDTO.getFileNumber());

		if (applicationList != null && !applicationList.isEmpty()) {

			int applicationCount = applicationList.size();

			if ("I".equals(applicationFileDTO.getApplicantType()) && applicationCount > 1) {
				strErrorList.add(LocaleConfig.getResourceValue("app.sub.val.error.appliedfor", null, locale, null));
				apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
						LocaleConfig.getResourceValue("app.sub.val.errors", null, locale, null), strErrorList);
			} else if ("G".equals(applicationFileDTO.getApplicantType()) && applicationCount > 15) {
				strErrorList.add("Group Applications cannot be more than 15 in count");
				apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
						LocaleConfig.getResourceValue("app.sub.val.errors", null, locale, null), strErrorList);
			} else {
				/* verify passport is submitted for all applications */
				List<String> passportApplicationList = applicantPassportTravelDetailsService
						.findApplications(applicationList);

				/* verify attachment is submitted for all applications */
				List<GenericGroupCountDTO> attachList = applicantAttachmentService.findApplications(applicationList);

				if (passportApplicationList != null && attachList != null) {
					List<String> attachAppList = attachList.stream().filter(element -> element.getCount() == 3)
							.map(element -> element.getRefNo()).collect(Collectors.toList());

					List<String> passportDifference = applicationList.stream()
							.filter(element -> !passportApplicationList.contains(element)).collect(Collectors.toList());

					List<String> attachmentDifference = applicationList.stream()
							.filter(element -> !attachAppList.contains(element)).collect(Collectors.toList());

					if (passportDifference.isEmpty() && attachmentDifference.isEmpty()) {
						log.info("Eligible for Submission of File");

						/* calculate the visa fee */
						ManagementVisaDetails visaDetails = managementVisaDetailsService
								.getVisaCompleteDetailsByVisaType(applicationFileDTO.getVisaType());

						if (visaDetails != null) {
							Long visaFeeAmount;
							if (applicationFileDTO.isExpressVisa()) {
								visaFeeAmount = visaDetails.getExpressVisaFee() * applicationCount;
							} else {
								visaFeeAmount = visaDetails.getVisaFee() * applicationCount;
							}
							log.info("Calculated Visa Fee: {}", visaFeeAmount);

							/*
							 * update the draft status to submitted in file table
							 */
							applicationFileService.updateFileStatusAndCount(applicationFileDTO.getFileNumber(),
									CommonsConstants.SUBMITTED, applicationList.size());

							/*
							 * insert the applications in header tracking table
							 */
							List<ApplicationHeader> applicationHeaderList = new ArrayList<>();

							for (String applicationNo : applicationList) {
								applicationHeaderList.add(new ApplicationHeader(applicationNo,
										applicationFileDTO.getFileNumber(), CommonsConstants.PENDING_PAYMENT,
										CommonsConstants.PENDING_PAYMENT, CommonsConstants.NO));
							}
							applicationHeaderService.saveAllApplicationHeader(applicationHeaderList);

							/* insert payment details */
							applicantPaymentService.savePaymentDetails(new ApplicantPaymentDetails(
									applicationFileDTO.getFileNumber(), CommonsConstants.USD, visaFeeAmount, 0L,
									"EVISA_PAY", "PP", applicationFileDTO.getUsername()));

							apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
									LocaleConfig.getResourceValue("submit.success", null, locale, null),
									applicationFileDTO.getFileNumber(), visaFeeAmount + " " + CommonsConstants.USD);

						} else {
							List<Object> objArray = new ArrayList<>();
							objArray.add(applicationFileDTO.getVisaType());
							strErrorList.add(LocaleConfig.getResourceValue("app.sub.val.novisafee", objArray.toArray(),
									locale, null));
							apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
									LocaleConfig.getResourceValue("app.sub.val.errors", null, locale, null),
									strErrorList);
						}
					} else {
						if (passportDifference != null) {
							List<Object> objArray = new ArrayList<>();
							objArray.add(passportDifference);
							strErrorList.add(LocaleConfig.getResourceValue("app.sub.val.passport.missing",
									objArray.toArray(), locale, null));
						}

						if (attachmentDifference != null) {
							List<Object> objArray = new ArrayList<>();
							objArray.add(attachmentDifference);
							strErrorList.add(LocaleConfig.getResourceValue("app.sub.val.attachment.missing",
									objArray.toArray(), locale, null));
						}

						apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
								LocaleConfig.getResourceValue("app.sub.val.errors", null, locale, null), strErrorList);
					}

				} else {
					strErrorList.add(
							LocaleConfig.getResourceValue("app.sub.val.personal.passport.missing", null, locale, null));
					apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
							LocaleConfig.getResourceValue("app.sub.val.errors", null, locale, null), strErrorList);
				}
			}
		} else {
			strErrorList.add(LocaleConfig.getResourceValue("app.sub.val.notavailable", null, locale, null));
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
					LocaleConfig.getResourceValue("app.sub.val.errors", null, locale, null), strErrorList);
		}

		return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
	}

	/**
	 * 
	 */
	@Override
	public PreviewDTO applicationPreview(String locale, String applicationNumber) {
		log.info("ApplicantCommonServiceFacadeImpl-applicationPreview");

		/* get personal information */
		ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsService
				.findByApplicationNumber(applicationNumber);

		/* get passport information */
		ApplicantPassportTravelDetails applicantPassportTravelDetails = applicantPassportTravelDetailsService
				.findPassportTravelInfoByApplicantionNumber(applicationNumber);

		/* get attachment information */
		ApplicantAttachmentDetailsDTOList applicantAttachmentDetailsDTOList = applicantAttachmentService
				.attachmentsByApplicationNumber(applicationNumber);

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
			} else {
				applicantPassportTravelDetails.setLastVisitedCountries("");
			}

		} else {
			log.info("applicantPassportTravelDetails is NULL - not set desc");
		}

		return new PreviewDTO(applicantPersonalDetails, applicantPassportTravelDetails,
				applicantAttachmentDetailsDTOList);
	}

	public Date dateverification() {

		return new Date();
	}

	/**
	 *
	 */
	@Override
	public ChargeResponseDTO paymentChargeRequest(ChargeRequestDTO chargeRequestDTO) {
		log.info("ApplicantCommonServiceFacade-paymentChargeRequest");

		ChargeResponseDTO chargeResponseDTO;

		try {
			chargeRequestDTO.setDescription(
					"Evisa for Djibouti checkout request for Ref Number: " + chargeRequestDTO.getReferenceNumber());
			Charge charge = stripeService.paymentChargeRequest(chargeRequestDTO);

			log.info("Charge-=-=-=-=-=- {}", charge);

			/* Update documentStatus=SUB, visaStatus=UP for given FileNumber */
			// applicationHeaderService.updatePaymentDetails(chargeRequestDTO.getReferenceNumber());

			/* Update Payment Instruction Table */

			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.SUCCESS, charge.getStatus(),
					charge.getReceiptUrl(), charge.getId());
		} catch (StripeException stexe) {
			log.error("paymentChargeRequest-StripeException {}", stexe.getMessage());
			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, stexe.getMessage(), null, null);
		} catch (Exception exe) {
			log.error("paymentChargeRequest-Exception {}", exe.getCause());
			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, exe.getMessage(), null, null);
		}
		return chargeResponseDTO;
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public ChargeResponseDTO paymentWithCheckOutPage(HttpServletRequest request,
			CheckOutPaymentDTO checkOutPaymentDTO) {
		log.info("ApplicantCommonServiceFacadeImpl-paymentWithCheckOutPage");

		ChargeResponseDTO chargeResponseDTO = null;
		Stripe.apiKey = stripeSecretKey;

		checkOutPaymentDTO.setSuccessUrl(
				appUrl + request.getContextPath() + "/#/main/success-payment?session_id={CHECKOUT_SESSION_ID}");
		checkOutPaymentDTO.setCurrency("EUR");
		checkOutPaymentDTO.setQuantity(1L);

		String name = "";
		if ("EVISA_PAY".equals(checkOutPaymentDTO.getInstrType())) {
			checkOutPaymentDTO.setDescription(
					"Evisa for Djibouti checkout request for …: " + checkOutPaymentDTO.getReferenceNumber());
			name = "E-Visa Payment";
		} else if ("EVISA_EXT".equals(checkOutPaymentDTO.getInstrType())) {
			checkOutPaymentDTO.setDescription("DJIB E-Visa Extension Checkout Request For Ref Number: "
					+ checkOutPaymentDTO.getReferenceNumber());
			name = "E-Visa Extension Payment";
		} else if ("EVISA_PEN".equals(checkOutPaymentDTO.getInstrType())) {
			checkOutPaymentDTO.setDescription("DJIB E-Visa Penality Checkout Request For Ref Number: "
					+ checkOutPaymentDTO.getReferenceNumber());
			name = "E-Visa Penality Payment";
		}
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(checkOutPaymentDTO.getSuccessUrl())
				.setCancelUrl(checkOutPaymentDTO.getCancelUrl())
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(checkOutPaymentDTO.getQuantity())
						.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
								.setCurrency(checkOutPaymentDTO.getCurrency())
								.setUnitAmount(checkOutPaymentDTO.getAmount())
								.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
										.setDescription(checkOutPaymentDTO.getDescription()).setName(name).build())
								.build())
						.build())
				.build();

		try {
			Session session = Session.create(params);
			log.info("session.getId -=-=- {}", session.getId());

			if ("EVISA_PEN".equals(checkOutPaymentDTO.getInstrType())) {
				ApplicantPaymentDetails applicationPayementDetails = applicantPaymentService
						.fetchApplicantPaymentDetails(checkOutPaymentDTO.getReferenceNumber());
				if (applicationPayementDetails != null) {
					if (applicationPayementDetails.getFileNumber().equals(checkOutPaymentDTO.getReferenceNumber())) {
						applicantPaymentService.updateByAmountDue(checkOutPaymentDTO.getReferenceNumber(),
								session.getId(), checkOutPaymentDTO.getAmount());
						chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.SUCCESS, session.getPaymentStatus(),
								session.getId());
						log.info("chargeResponseDTO -=-=- {}", chargeResponseDTO);
					}
				} else {
					applicantPaymentService.savePaymentDetails(new ApplicantPaymentDetails(
							checkOutPaymentDTO.getReferenceNumber(), CommonsConstants.USD,
							checkOutPaymentDTO.getAmount(), 0L,
							"EVISA_PEN", "PP", checkOutPaymentDTO.getUsername(), session.getId()));

					chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.SUCCESS, session.getPaymentStatus(),
							session.getId());
					log.info("chargeResponseDTO -=-=- {}", chargeResponseDTO);

				}

			} else {
				/* Update the session Id to the payment details */
				System.out.println("Test 11111111111111111111111");
				applicantPaymentService.updatePaymentSessionId(checkOutPaymentDTO.getReferenceNumber(),
						session.getId());

				chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.SUCCESS, session.getPaymentStatus(),session.getSuccessUrl(), 
						session.getId());
				log.info("chargeResponseDTO -=-=- {}", chargeResponseDTO);
			}

		} catch (StripeException stexe) {
			log.error("paymentChargeRequest-StripeException {}", stexe.getMessage());
			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, stexe.getMessage(), null, null);
		} catch (Exception exe) {
			log.error("paymentChargeRequest-Exception {}", exe.getCause());
			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, exe.getMessage(), null, null);
		}
		return chargeResponseDTO;
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public ChargeResponseDTO processSucessResponse(String sessionId) {
		log.info("ApplicantCommonServiceFacadeImpl-processSucessResponse");

		Stripe.apiKey = stripeSecretKey;

		log.info("sessionId-=-=- {}", sessionId);

		// Session session = Session.retrieve(sessionId);

		/* Find the File Number with session Id */
		ApplicantPaymentDetails applicantPaymentDetails = applicantPaymentService.getFileNumberBySessionId(sessionId);

		if ("EVISA_PAY".equals(applicantPaymentDetails.getInstrType())) {
			/* Update Payment Details in Application Header to get processed */
			// get stripe amount paid 
			Long amountPaid = 0L;
			try {
				Session session = Session.retrieve(sessionId);
				String paymentIntentId = session.getPaymentIntent();
				PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
				List<Charge> chargeList = paymentIntent.getCharges().getData();
				amountPaid = chargeList.get(0).getAmount() / 100;
				log.error("Retrieve PaymentIntent for amount: {}", amountPaid, applicantPaymentDetails.getAmountDue());
			} catch (StripeException e) {
				log.error("Failed to retrieve PaymentIntent for sessionId: {}", sessionId, e);
			}

			// If stripe amound paid is egal to amount due update payment details in application header
			if (amountPaid.equals(applicantPaymentDetails.getAmountDue())) {				
				applicationHeaderService.updatePaymentDetails(applicantPaymentDetails.getFileNumber());
				applicantPaymentService.updatePaymentSuccess("PC", 0, "card", sessionId);
			}
			
		} else if ("EVISA_EXT".equals(applicantPaymentDetails.getInstrType())) {
			/* Update Payment Details in Visa Extension to get processed */
			applicantVisaExtensionService.updatePaymentDetails(applicantPaymentDetails.getFileNumber());

			/* update extension applied flag as Y */
			applicationHeaderService.updateExtensionAppliedFlag(applicantPaymentDetails.getFileNumber(),
					CommonsConstants.YES);
			applicantPaymentService.updatePaymentSuccess("PC", 0, "card", sessionId);
		} else if ("EVISA_PEN".equals(applicantPaymentDetails.getInstrType())) {
			applicantPaymentService.updatePaymentPenalitySuccess("PC", 0, "card", sessionId);

		}

		/* Update Success Payment Transaction to avoid doing payments again */

		// Session.retrieve(sessionId).getPaymentIntentObject().getCharges().getData().get(0).getReceiptUrl();

		return new ChargeResponseDTO(CommonsConstants.SUCCESS, "paid", applicantPaymentDetails.getFileNumber());
	}

	/**
	 *
	 */
	@Override
	public Page<ApplicantPaymentDetails> searchApplicantPayments(PaymentSearchDTO paymentSearchDTO) {
		log.info("ApplicantCommonServiceFacadeImpl-searchApplicantPayments");
		return applicantPaymentService.searchApplicantPayments(paymentSearchDTO);
	}

	/**
	 *
	 */
	@Override
	public ApplicantPaymentDetails fetchApplicantPaymentDetails(String filenumber) {
		log.info("ApplicantCommonServiceFacadeImpl-fetchApplicantPaymentDetails");
		return applicantPaymentService.fetchApplicantPaymentDetails(filenumber);
	}

	/**
	 *
	 */
	@Override
	public ResponseEntity<Resource> paymentReceiptDetails(String sessionid) {
		log.info("ApplicantCommonServiceFacadeImpl-paymentReceiptDetails");

		Stripe.apiKey = stripeSecretKey;
		InvoiceDTO invoiceDTO = null;
		try {
			Session session = Session.retrieve(sessionid);
			String paymentIntentId = session.getPaymentIntent();
			PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

			List<Charge> chargeList = paymentIntent.getCharges().getData();

			invoiceDTO = new InvoiceDTO();
			invoiceDTO.setStatusCode(CommonsConstants.SUCCESS);
			invoiceDTO.setStatusDesc(session.getPaymentStatus().toUpperCase());
			invoiceDTO.setReceiptNumber(chargeList.get(0).getInvoice());
			log.info(chargeList.get(0).toString());
			invoiceDTO.setAmountPaid(chargeList.get(0).getAmount() / 100);
			invoiceDTO.setCurrency(chargeList.get(0).getCurrency().toUpperCase());
			String dateString = new SimpleDateFormat("MMM dd, yyyy")
					.format(new Date(chargeList.get(0).getCreated() * 1000));
			invoiceDTO.setDatePaid(dateString);
			String paymentMethod = chargeList.get(0).getPaymentMethodDetails().getCard().getBrand().toUpperCase()
					+ " - " + chargeList.get(0).getPaymentMethodDetails().getCard().getLast4();
			invoiceDTO.setPaymentMethod(paymentMethod);

			byte[] byteArr = generatePdfFromHtml(parseThymeleafTemplate(invoiceDTO), null);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + invoiceDTO.getAmountPaid() + ".pdf" + "\"")
					.body(new ByteArrayResource(byteArr));

		} catch (StripeException strexe) {
			log.error("paymentReceiptDetails-StripeExeception {}", strexe.getMessage());
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return null;
	}

	/**
	 * @param invoiceDTO
	 * @return
	 */
	private String parseThymeleafTemplate(InvoiceDTO invoiceDTO) {
		log.info("ApplicantCommonServiceFacadeImpl-parseThymeleafTemplate");

		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context();
		context.setVariable("receiptNumber", invoiceDTO.getReceiptNumber());
		context.setVariable("amount", invoiceDTO.getAmountPaid());
		context.setVariable("datePaid", invoiceDTO.getDatePaid());
		context.setVariable("paymentMethod", invoiceDTO.getPaymentMethod());

		return templateEngine.process("templates/invoicereceipt", context);
	}

	/**
	 * @param html
	 * @param filename
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] generatePdfFromHtml(String html, String applicationNumber) throws DocumentException, IOException {
		log.info("ApplicantCommonServiceFacadeImpl-generatePdfFromHtml");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		ITextRenderer renderer = new ITextRenderer();

		if (applicationNumber != null) {
			renderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(
					renderer.getSharedContext().getReplacedElementFactory(), applicationNumber));
		}

		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(bos);
		return bos.toByteArray();
	}

	/**
	 *
	 */
	@Override
	public ResponseEntity<Resource> applicationDownload(String applicationNumber, HttpServletRequest request) {
		log.info("ApplicantCommonServiceFacadeImpl-applicationDownload");

		try {
			/* get Application Details */
			String htmlContent = parseThymeleafTemplateForApplDownload(
					getApplicationDetailsForDownload(applicationNumber), request);

			byte[] byteArr = generatePdfFromHtml(htmlContent, applicationNumber);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + applicationNumber + ".pdf" + "\"")
					.body(new ByteArrayResource(byteArr));

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String parseThymeleafTemplateForApplDownload(PreviewDTO previewDTO, HttpServletRequest request)
			throws IOException {
		log.info("ApplicantCommonServiceFacadeImpl-parseThymeleafTemplateForApplDownload");

	 ApplicationHeader applicationHeader = applicationHeaderService
			.getHeaderApplication(previewDTO.getApplicantPersonalDetails().getApplicationNumber());

		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		

		// String codeContent = "REF NO: " + previewDTO.getApplicantPersonalDetails().getApplicationNumber() + "\n"
		// 		+ "DJIBOUTI,Le: " + sdf.format(previewDTO.getApplicationTracker().getClosedDate()) + "\n"
		// 		+ "NOM ET PRENOM: " + previewDTO.getApplicantPersonalDetails().getGivenName().toUpperCase() + " "
		// 		+ previewDTO.getApplicantPersonalDetails().getSurname().toUpperCase() + "\n" + "NATIONALITE: "
		// 		+ previewDTO.getApplicantPersonalDetails().getNationality().toUpperCase() + "\n"
		// 		+ "NUMERO DE PASSEPORT: "
		// 		+ previewDTO.getApplicantPassportTravelDetails().getPassportNumber().toUpperCase() + "\n" + "A /AU: "
		// 		+ previewDTO.getApplicantPassportTravelDetails().getIssuedCountry().toUpperCase() + "\n"
		// 		+ "DELIVRE LE: " + sdf.format(previewDTO.getApplicantPassportTravelDetails().getIssuedDate()) + "\n"
		// 		+ "EXPIRE LE: " + sdf.format(previewDTO.getApplicantPassportTravelDetails().getExpiryDate()) + "\n"
		// 		+ "DUREE DU SEJOUR: " + previewDTO.getApplicationTracker().getVisaDuration() + " JOURS" + "\n"
		// 		+ appDownload + "/applicant-api/v1/api/application/download/"
		// 		+ previewDTO.getApplicantPersonalDetails().getApplicationNumber();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

// Récupérer les dates en tant que LocalDate

LocalDate issuedDate = previewDTO.getApplicantPassportTravelDetails().getIssuedDate();
LocalDate expiryDate = previewDTO.getApplicantPassportTravelDetails().getExpiryDate();
LocalDate dob = previewDTO.getApplicantPersonalDetails().getDateOfBirth();



// Formater les dates en chaînes de caractères

String issuedDateFormatted = issuedDate.format(formatter);
String expiryDateFormatted = expiryDate.format(formatter);
String dateOfBirth = dob.format(formatter);


 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

// Construire le contenu du code
String codeContent = "REF NO: " + previewDTO.getApplicantPersonalDetails().getApplicationNumber() + "\n"
        + "DJIBOUTI, Le: " +previewDTO.getApplicationTracker().getClosedDate() + "\n"
        + "NOM ET PRENOM: " + previewDTO.getApplicantPersonalDetails().getGivenName().toUpperCase() + " "
        + previewDTO.getApplicantPersonalDetails().getSurname().toUpperCase() + "\n"
        + "NATIONALITE: " + previewDTO.getApplicantPersonalDetails().getNationality().toUpperCase() + "\n"
        + "NUMERO DE PASSEPORT: " + previewDTO.getApplicantPassportTravelDetails().getPassportNumber().toUpperCase() + "\n"
        + "A /AU: " + previewDTO.getApplicantPassportTravelDetails().getIssuedCountry().toUpperCase() + "\n"
        + "DELIVRE LE: " + issuedDateFormatted + "\n"
        + "EXPIRE LE: " + expiryDateFormatted + "\n"
       // + "DUREE DU SEJOUR: " + previewDTO.getApplicationTracker().getVisaDuration() + " JOURS" + "\n"
        + appDownload + "/applicant-api/v1/api/application/download/"
        + previewDTO.getApplicantPersonalDetails().getApplicationNumber();

		File directory = new File(appUrlHyp + "evisa/common/rel1_0/barcode/"
				+ previewDTO.getApplicantPersonalDetails().getApplicationNumber());
		boolean success = false;
		if (directory.exists()) {
			log.info("Directory already exists ...");
		} else {
			log.info("Directory not exists, creating now");
			success = directory.mkdir();
			if (success) {
				log.info("Successfully created new directory : {} {}", appUrlHyp + "evisa/common/rel1_0/barcode/",
						previewDTO.getApplicantPersonalDetails().getApplicationNumber());
			} else {
				log.info("Failed to create new directory: {} {}", appUrlHyp + "evisa/common/rel1_0/barcode/",
						previewDTO.getApplicantPersonalDetails().getApplicationNumber());
			}
		}

		/* setting the QR Code to the visa application download */
		FileOutputStream qrfos;
		boolean qrsuccess = false;
		ByteArrayOutputStream stream = QRCode.from(codeContent).withSize(75, 80).to(ImageType.PNG).stream();
		File qrf = new File(appUrlHyp + "evisa/common/rel1_0/barcode/"
				+ previewDTO.getApplicantPersonalDetails().getApplicationNumber() + "/" + "QRCode" + ".png");
		if (qrf.exists()) {
			log.info("QR code File already exists");
		} else {
			log.info("No such file exists, creating now");
			qrsuccess = qrf.createNewFile();
			qrfos = new FileOutputStream(qrf);
			qrfos.write(stream.toByteArray());
			qrfos.flush();
			qrfos.close();
			if (qrsuccess) {
				log.info("Successfully created new qrcode file: {}", qrf);
			} else {
				log.info("Failed to create new qrcode file: {}", qrf);
			}
		}
		/* setting the photograph to the visa application download */
		FileOutputStream avatarfos;
		boolean avatarsuccess = false;
		File avatarf = new File(this.appUrlHyp + "evisa/common/rel1_0/barcode/"
				+ previewDTO.getApplicantPersonalDetails().getApplicationNumber() + "/" + "avatar" + ".png");
		if (avatarf.exists()) {
			log.info("Avatar File already exists");
		} else {
			log.info("No such Avatar file exists, creating now");
			avatarsuccess = avatarf.createNewFile();
			avatarfos = new FileOutputStream(avatarf);
			avatarfos.write(previewDTO.getPhotograph());
			avatarfos.flush();
			avatarfos.close();
			if (avatarsuccess) {
				log.info("Successfully created new qrcode file: {}", avatarf);
			} else {
				log.info("Failed to create new qrcode file: {}", avatarf);
			}
		}

		/* setting the barcode to the visa application download */
		Code128Bean bean = new Code128Bean();
		final int dpi = 160;
		bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
		bean.doQuietZone(false);

		BitmapCanvasProvider canvas = new BitmapCanvasProvider(300, BufferedImage.TYPE_BYTE_GRAY, true, 0);
		bean.generateBarcode(canvas, previewDTO.getApplicantPersonalDetails().getApplicationNumber());
		BufferedImage barcodeImage = canvas.getBufferedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(barcodeImage, "png", baos);

		boolean barcodesuccess = false;
		FileOutputStream barcodefos = null;
		File barcoderf = new File(appUrlHyp + "evisa/common/rel1_0/barcode/"
				+ previewDTO.getApplicantPersonalDetails().getApplicationNumber() + "/" + "barcode" + ".png");
		if (barcoderf.exists()) {
			log.info("barcode File already exists");
		} else {
			log.info("No such file exists, creating now");
			barcodesuccess = barcoderf.createNewFile();
			barcodefos = new FileOutputStream(barcoderf);
			barcodefos.write(baos.toByteArray());
			barcodefos.flush();
			barcodefos.close();
			if (barcodesuccess) {
				log.info("Successfully created new barcode file: {}", barcoderf);
			} else {
				log.info("Failed to create new barcode file: {}", barcoderf);
			}
		}

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
		context.setVariable("nationality", previewDTO.getApplicantPersonalDetails().getCurrentNationality().toUpperCase());
		context.setVariable("passportNo",
				previewDTO.getApplicantPassportTravelDetails().getPassportNumber().toUpperCase());
		context.setVariable("issueDate", issuedDate);
		context.setVariable("expiryDate", expiryDate);
		context.setVariable("reasonsForTravel",
				previewDTO.getApplicantPassportTravelDetails().getTravelPurpose().toUpperCase());
		context.setVariable("durationStay", previewDTO.getApplicationTracker().getVisaDuration() + " JOURS ");

		// int visaDuration = previewDTO.getApplicationTracker().getVisaDuration();

		// // Vérifier si la durée est égale à 90
		// if (visaDuration == 90) {
		// context.setVariable("durationStay", "VOUS NE POUVEZ PAS DÉPASSER CE DÉLAI DE
		// 90 JOURS");
		// } else {
		// context.setVariable("durationStay", visaDuration + " JOURS ");
		// }
		int visaDuration = previewDTO.getApplicationTracker().getVisaDuration();

		// Mettre à jour les variables selon la logique
		if (visaDuration == 90) {
			context.setVariable("durationStay", "90 Jours"); // Affiche une étoile
			context.setVariable("warningMessage", "* The stay date can't exceed the validity date / Le séjour ne peut pas dépasser la date de validité.");
		} else {
			context.setVariable("durationStay", visaDuration + " JOURS "); // Affiche normalement pour les autres cas
			context.setVariable("warningMessage", ""); // Pas de message d'avertissement
		}
		
		context.setVariable("entryType",
				"S".equals(previewDTO.getApplicationTracker().getEntryType()) ? "UNE SEULE ENTREE" : "MULTIPLE ENTREE");

				//Le validate from a ete modifier en raison de la demande de la police provisoirement 
		//context.setVariable("visaValidFrom", sdf.format(applicationHeader.getVisaValidFrom()));

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		context.setVariable("visaValidFrom",previewDTO.getApplicantPassportTravelDetails().getArrivalDate().format(dtf));

		if (previewDTO.getApplicantPassportTravelDetails().getDepartureDate() != null) {
			context.setVariable("visaValidTo",previewDTO.getApplicantPassportTravelDetails().getDepartureDate().format(dtf));
		} else {
			context.setVariable("visaValidTo",sdf.format(applicationHeader.getVisaValidTo()));
		}

		context.setVariable("group", previewDTO.getApplicationTracker().getApplicantType().equals("G") ? "YES" : "NO");
		if (applicationHeader.getExtensionApplied().equals("Y")) {
			String extensionType = applicantVisaExtensionService.getExtensionTypeByApplicationNumber(
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

	/**
	 * @param applicationNumber
	 * @return
	 */
	PreviewDTO getApplicationDetailsForDownload(String applicationNumber) {
		log.info("ApplicantCommonServiceFacadeImpl-getApplicationDetailsForDownload");

		/* get personal information */
		ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsService
				.findByApplicationNumber(applicationNumber);

		/* get passport information */
		ApplicantPassportTravelDetails applicantPassportTravelDetails = applicantPassportTravelDetailsService
				.findPassportTravelInfoByApplicantionNumber(applicationNumber);

		ApplicationTracker applicationTracker = applicationTrackerService.getApplicationDetails(applicationNumber);

		/* consolidate all the master codes */
		List<String> codeList = new ArrayList<>();
		codeList.add(applicantPersonalDetails.getNationality());
		codeList.add(applicantPersonalDetails.getResidenceCountry());
		codeList.add(applicantPersonalDetails.getBirthCountry());
		codeList.add(applicantPersonalDetails.getOriginCountry());

		if (applicantPassportTravelDetails != null) {
			codeList.add(applicantPassportTravelDetails.getArrivalLocation());
			codeList.add(applicantPassportTravelDetails.getIssuedCountry());
			codeList.add(applicantPassportTravelDetails.getTravelPurpose());

		} else {
			log.info("applicantPassportTravelDetails is NULL - not set code");
		}

		/* get master code details with consolidate master code list */
		List<MasterCodeResultDTO> masterCodeResultDTOList = masterCodeDetailsService.getMasterCodeLists(codeList);

		/* avatar */
		List<ApplicantAttachmentDetails> applAttchDtls = applicantAttachmentService
				.findAttchDtlsByAppNoAndAttchType(applicationNumber, "PG");
		byte[] fileDate = applAttchDtls.get(0).getFileData();

		/* construct map object to identify the desc with code and code type */
		Map<IndexDTO, String> resultMap = new HashMap<>();
		for (MasterCodeResultDTO masterCodeDTO : masterCodeResultDTOList) {
			String value = masterCodeDTO.getDescriptionOther();
			resultMap.put(new IndexDTO(masterCodeDTO.getCode(), masterCodeDTO.getCodeType()), value);
		}

		/* replace the code with description from result map */
		applicantPersonalDetails.setNationality(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getNationality(), CommonsConstants.CODETYPE_NTNLT)));
		applicantPersonalDetails.setResidenceCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getResidenceCountry(), CommonsConstants.CODETYPE_CNTRY)));
		applicantPersonalDetails.setBirthCountry(resultMap
				.get(new IndexDTO(applicantPersonalDetails.getBirthCountry(), CommonsConstants.CODETYPE_CNTRY)));
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

		} else {
			log.info("applicantPassportTravelDetails is NULL - not set desc");
		}

		PreviewDTO previewDTO = new PreviewDTO(applicantPersonalDetails, applicantPassportTravelDetails,
				applicationTracker);
		previewDTO.setPhotograph(fileDate);

		return previewDTO;
	}

	/**
	 *
	 */
	@Override
	public CheckVisaResultDTO checkVisaStatus(String locale, String applicationNumber) {
		log.info("ApplicantCommonServiceFacadeImpl-checkVisaStatus");

		ApplicationTracker applicationTracker = applicationTrackerService.getApplicationDetails(applicationNumber);

		CheckVisaResultDTO checkVisaResultDTO;
		if (applicationTracker != null) {
			checkVisaResultDTO = new CheckVisaResultDTO("SUCCESS", "Application Details available in the System!");
			checkVisaResultDTO.setApplicationTracker(applicationTracker);
			if ("CLS".equals(applicationTracker.getDocumentStatus())) {
				String approverRemarks = applicationTrackerService.getApproverRemarks(applicationNumber);
				checkVisaResultDTO.setApproverRemarks(approverRemarks);
			} else {
				checkVisaResultDTO.setApproverRemarks("-NA-");
			}

			String applicationResult;
			if ("APR".equals(applicationTracker.getVisaStatus())) {
				applicationResult = "Your Application Has Been Approved, You Must Download Your Visa";
			} else if ("REJ".equals(applicationTracker.getVisaStatus())) {
				applicationResult = "Your Application Has Been Rejected, Refer Remarks Section For More Details";
			} else {
				applicationResult = "-NA-";
			}

			checkVisaResultDTO.setApplicationResult(applicationResult);

		} else {
			checkVisaResultDTO = new CheckVisaResultDTO("ERROR", "Unable to Fetch Application Details, Contact ADMIN!");
		}

		return checkVisaResultDTO;
	}

	@Override
	@Transactional
	public ChargeResponseDTO paymentPenalityWithCheckOutPage(HttpServletRequest request,
			CheckOutPaymentDTO checkOutPaymentDTO) {
		log.info("ApplicantCommonServiceFacadeImpl-paymentPenalityWithCheckOutPage");
		ChargeResponseDTO chargeResponseDTO;
		Stripe.apiKey = stripeSecretKey;

		checkOutPaymentDTO.setSuccessUrl(
				appUrl + request.getContextPath() + "/#/main/success-payment?session_id={CHECKOUT_SESSION_ID}");
		checkOutPaymentDTO.setCurrency("USD");
		checkOutPaymentDTO.setQuantity(1L);
		String name = "";
		checkOutPaymentDTO.setDescription(
				"Evisa Penality for Djibouti checkout request for …: " + checkOutPaymentDTO.getReferenceNumber());
		name = "E-Visa Payment Penality";
		checkOutPaymentDTO.setInstrType("Evisa_Penality");
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(checkOutPaymentDTO.getSuccessUrl())
				.setCancelUrl(checkOutPaymentDTO.getCancelUrl())
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(checkOutPaymentDTO.getQuantity())
						.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
								.setCurrency(checkOutPaymentDTO.getCurrency())
								.setUnitAmount(checkOutPaymentDTO.getAmount())
								.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
										.setDescription(checkOutPaymentDTO.getDescription()).setName(name).build())
								.build())
						.build())
				.build();

		try {
			Session session = Session.create(params);
			log.info("session.getId -=-=- {}", session.getId());
			System.out.println("KJHGIUGIUGIU" + checkOutPaymentDTO.getInstrType() +
					checkOutPaymentDTO.getAmount() +
					checkOutPaymentDTO.getDescription() +
					checkOutPaymentDTO.getSuccessUrl() +
					checkOutPaymentDTO.getCancelUrl() +
					checkOutPaymentDTO.getCurrency());

			/*
			 * applicantPaymentService.savePaymentDetails(new ApplicantPaymentDetails(
			 * applicationFileDTO.getFileNumber(), CommonsConstants.USD, visaFeeAmount, 0L,
			 * "EVISA_PAY", "PP", applicationFileDTO.getUsername()));
			 * 
			 */

			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.SUCCESS, session.getPaymentStatus(),
					session.getId());
			log.info("chargeResponseDTO -=-=- {}", chargeResponseDTO);
		} catch (StripeException stexe) {
			log.error("paymentChargeRequest-StripeException {}", stexe.getMessage());
			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, stexe.getMessage(), null, null);
		} catch (Exception exe) {
			log.error("paymentChargeRequest-Exception {}", exe.getCause());
			chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, exe.getMessage(), null, null);
		}
		return chargeResponseDTO;
	}

	/**
	 * ============================================================================
	 * MÉTHODES WEBHOOK STRIPE - Gestion automatique des paiements
	 * ============================================================================
	 */

	/**
	 * WEBHOOK STRIPE - Gestion des événements de paiement
	 * Résout le problème des paiements restant en "Pending" si l'utilisateur ferme la fenêtre
	 */
	@Override
	@Transactional
	public ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader) {
		log.info("🔔 === DÉBUT TRAITEMENT WEBHOOK STRIPE ===");
		log.info("📨 Taille du payload reçu: {} caractères", payload != null ? payload.length() : 0);
		log.info("🔐 En-tête signature reçu: {}", sigHeader != null ? sigHeader.substring(0, Math.min(50, sigHeader.length())) + "..." : "AUCUNE");
		log.info("🔑 Secret webhook configuré: {}", stripeWebhookSecret != null && !stripeWebhookSecret.isEmpty() ? "OUI (masqué)" : "NON");

		if (stripeWebhookSecret != null && !stripeWebhookSecret.isEmpty()) {
			log.info("🔍 Type de secret détecté: {}", stripeWebhookSecret.startsWith("whsec_") ? "Webhook endpoint secret (✅ CORRECT)" : "❌ INCORRECT - doit commencer par 'whsec_'");
		}

		try {
			Event event;

			// Vérification de la signature si le secret est configuré
			if (stripeWebhookSecret != null && !stripeWebhookSecret.isEmpty()) {
				log.info("🛡️ Vérification de la signature Stripe en cours...");

				if (!stripeWebhookSecret.startsWith("whsec_")) {
					log.error("❌ ERREUR CONFIGURATION: Le secret webhook doit commencer par 'whsec_'");
					log.error("📋 Secret actuel (masqué): {}...", stripeWebhookSecret.substring(0, Math.min(10, stripeWebhookSecret.length())));
					return ResponseEntity.status(400).body("Configuration webhook incorrecte - secret invalide");
				}

				try {
					event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
					log.info("✅ Signature webhook VALIDÉE avec succès");
				} catch (SignatureVerificationException e) {
					log.error("❌ ÉCHEC VÉRIFICATION SIGNATURE");
					log.error("📝 Détails de l'erreur: {}", e.getMessage());
					log.error("🔍 Vérifiez que le secret webhook correspond à celui configuré dans Stripe Dashboard");
					log.error("🌐 URL webhook configurée dans Stripe: https://evisav2.gouv.dj/applicant-api/api/stripe/webhook");
					return ResponseEntity.status(400).body("Invalid signature");
				}
			} else {
				log.warn("⚠️ SECRET WEBHOOK NON CONFIGURÉ - signature NON vérifiée (DANGEREUX en production)");
				log.warn("🔧 Ajoutez 'stripe.webhook.secret=whsec_xxx' dans la configuration");
				event = Event.GSON.fromJson(payload, Event.class);
			}

			log.info("📋 Événement Stripe reçu: {} (ID: {})", event.getType(), event.getId());
			log.info("⏰ Timestamp de l'événement: {}", new Date(event.getCreated() * 1000));

			// Traitement selon le type d'événement
			switch (event.getType()) {
				case "checkout.session.completed":
					log.info("🎯 Traitement de l'événement: checkout.session.completed");
					handleCheckoutSessionCompleted(event);
					break;
				case "payment_intent.succeeded":
					log.info("💳 Payment Intent réussi - traité via checkout.session.completed");
					break;
				case "payment_intent.payment_failed":
					log.warn("💸 Payment Intent échoué - ID: {}", event.getId());
					break;
				default:
					log.info("ℹ️ Événement non géré (ignoré): {}", event.getType());
			}

			log.info("✅ === WEBHOOK TRAITÉ AVEC SUCCÈS ===");
			return ResponseEntity.ok("OK");

		} catch (Exception e) {
			log.error("💥 === ERREUR CRITIQUE WEBHOOK ===");
			log.error("🚨 Type d'erreur: {}", e.getClass().getSimpleName());
			log.error("📝 Message d'erreur: {}", e.getMessage());
			log.error("🔍 Stack trace:", e);

			if (e instanceof SignatureVerificationException) {
				return ResponseEntity.status(400).body("Invalid signature");
			} else {
				return ResponseEntity.status(500).body("Webhook error");
			}
		}
	}

	/**
	 * Traite l'événement checkout.session.completed
	 * Met à jour automatiquement le paiement et l'application
	 */
	@Transactional()
	private void handleCheckoutSessionCompleted(Event event) {
		try {
			log.info("Début traitement checkout.session.completed");

			// Récupération de la session Stripe
			Session session = null;
			String sessionId = null;

			try {
				// Première méthode : via le désérialiseur
				session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
				if (session != null) {
					log.info("Session récupérée via getDataObjectDeserializer");
				}
			} catch (Exception e) {
				log.warn("Échec désérialisation via getDataObjectDeserializer : {}", e.getMessage());
			}

			// Si la première méthode échoue, essayer via le JSON
			if (session == null) {
				try {
					String jsonString = event.getData().getObject().toJson();
					com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonString).getAsJsonObject();
					sessionId = jsonObject.get("id").getAsString();
					log.info("SessionId extrait du JSON : {}", sessionId);

					session = Session.retrieve(sessionId);
					log.info("Session récupérée via API Stripe");
				} catch (Exception e) {
					log.error("Échec récupération session via JSON parsing : {}", e.getMessage());
				}
			}

			if (session == null) {
				log.error("ERREUR CRITIQUE : Impossible de récupérer la session Stripe");
				return;
			}

			sessionId = session.getId();
			String paymentStatus = session.getPaymentStatus();
			Long amountTotal = session.getAmountTotal();

			log.info("📋 Session Stripe - ID: {}, Status: {}, Montant: {}", sessionId, paymentStatus, amountTotal);

			if ("paid".equals(paymentStatus)) {
				log.info("💰 Session checkout complétée avec succès : {}", sessionId);

				// Récupérer le paiement associé à cette session
				ApplicantPaymentDetails payment = applicantPaymentService.getFileNumberBySessionId(sessionId);

				if (payment != null) {
					String fileNumber = payment.getFileNumber();
					String instrType = payment.getInstrType();
					log.info("🔍 Paiement trouvé - Référence: {}, Type: {}, Statut actuel: {}", fileNumber, instrType, payment.getStatus());

					try {
						// Traitement selon le type d'instruction
						if ("EVISA_PAY".equals(instrType)) {
							// 1. Mettre à jour le statut du paiement vers "PC" (Payment Confirmed)
							// Montant déjà disponible dans la session Stripe
							Long amountInCents = amountTotal != null ? amountTotal : 0L;
							applicantPaymentService.updatePaymentSuccess("PC", 0L, "CARD", sessionId);
							log.info("✅ Paiement E-VISA mis à jour : PP → PC pour référence : {}", fileNumber);

							// 2. Mettre à jour le statut de l'application dans tevi_header
							applicationHeaderService.updatePaymentDetails(fileNumber);
							log.info("✅ Application mise à jour : documentStatus → SUB, visaStatus → UP pour référence : {}", fileNumber);

						} else if ("EVISA_EXT".equals(instrType)) {
							// Traitement pour l'extension de visa
							applicantPaymentService.updatePaymentSuccess("PC", 0L, "CARD", sessionId);
							log.info("✅ Paiement EXTENSION mis à jour : PP → PC pour référence : {}", fileNumber);

							// Mettre à jour l'extension de visa
							applicantVisaExtensionService.updatePaymentDetails(fileNumber);
							applicationHeaderService.updateExtensionAppliedFlag(fileNumber, CommonsConstants.YES);
							log.info("✅ Extension de visa mise à jour pour référence : {}", fileNumber);

						} else if ("EVISA_PEN".equals(instrType)) {
							// Traitement pour les pénalités
							applicantPaymentService.updatePaymentPenalitySuccess("PC", 0L, "CARD", sessionId);
							log.info("✅ Paiement PÉNALITÉ mis à jour : PP → PC pour référence : {}", fileNumber);
						}

						log.info("🎉 SYNCHRONISATION COMPLÈTE - Référence : {} (Type: {})", fileNumber, instrType);

					} catch (Exception updateException) {
						log.error("❌ Erreur lors de la mise à jour pour référence : {} - {}", fileNumber, updateException.getMessage());
						updateException.printStackTrace();
						// Ne pas re-lancer l'exception pour éviter les rollback en cascade
						// mais logger l'erreur pour investigation
					}

				} else {
					log.warn("⚠️ PROBLÈME: Aucun paiement trouvé pour la session : {}", sessionId);
					log.warn("🔍 Vérifiez que le sessionId est bien stocké lors de la création du checkout");
				}
			} else {
				log.warn("⚠️ Session Stripe non payée - Status: {} pour session: {}", paymentStatus, sessionId);
			}
		} catch (Exception e) {
			log.error("💥 Erreur critique lors du traitement de checkout.session.completed", e);
			// Ne pas re-lancer pour éviter que Stripe retente indéfiniment
		}
	}

	/**
	 * Estime le montant de la pénalité pour un séjour prolongé
	 * @param dto DTO contenant les informations de dépassement de visa
	 * @return DTO avec le montant de pénalité calculé
	 */
//	public VisaCheckOverstayDTO estimatePenality(VisaCheckOverstayDTO dto) {
//		log.info("Début calcul estimation pénalité pour application: {}", dto.getApplicationNumber());
//
//		long daysDiff = 0;
//		double penaltyAmount = 0;
//		double penaltyPaid = 0;
//		VisaCheckOverstayDTO visaDto = new VisaCheckOverstayDTO();
//		long timeDiff = 0;
//
//		try {
//			long departedDate = dto.getDepartedestimate().getTime();
//			long currentDate = new Date().getTime();
//
//			if (departedDate > currentDate) {
//				timeDiff = departedDate - currentDate;
//			}
//
//			daysDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
//			log.info("Différence en jours calculée: {}", daysDiff);
//
//			if (daysDiff > 0) {
//				// Calculer le montant de la pénalité
//				String penaltyValue = productConfigService.getConfigValueByConfigCode("VISA_PNLTY_CHRE");
//				log.info("Valeur pénalité VISA_PNLTY_CHRE: {}", penaltyValue);
//
//				int dailyPenalty = Integer.parseInt(penaltyValue != null ? penaltyValue : "20");
//				penaltyAmount = daysDiff * dailyPenalty;
//				penaltyPaid = penaltyAmount + dto.getPenalityAmount();
//			}
//
//			// Remplir le DTO de retour avec toutes les informations
//			visaDto.setApplicationNumber(dto.getApplicationNumber());
//			visaDto.setDepartedestimate(dto.getDepartedestimate());
//			visaDto.setPenalityAmount(penaltyPaid);
//			visaDto.setDaysDiff(daysDiff);
//
//			log.info("Pénalité calculée - Jours: {}, Montant journalier: {}, Montant total: {}", daysDiff, penaltyAmount, penaltyPaid);
//
//		} catch (NumberFormatException e) {
//    }
}
