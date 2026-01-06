package com.hypercube.evisa.common.api.serviceimpl;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionFileUpload;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionHistory;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTOList;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionHistoryDTO;
import com.hypercube.evisa.common.api.model.AttachmentPreviewDTO;
import com.hypercube.evisa.common.api.model.AttachmentUrlList;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.DashboardDTO;
import com.hypercube.evisa.common.api.model.ExtensionPreviewDTO;
import com.hypercube.evisa.common.api.model.IndexDTO;
import com.hypercube.evisa.common.api.model.MailDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;
import com.hypercube.evisa.common.api.repository.ApplicantAttachmentRepository;
import com.hypercube.evisa.common.api.service.ApplicantAttachmentService;
import com.hypercube.evisa.common.api.service.ApplicantPassportTravelDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantPaymentService;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionFileUploadService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionHistoryService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionServiceFacade;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
import com.hypercube.evisa.common.api.service.StripeService;
import com.hypercube.evisa.common.api.service.VisaExtensionCheckService;
import com.hypercube.evisa.common.api.util.CommonsUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicantVisaExtensionServiceFacadeImpl implements ApplicantVisaExtensionServiceFacade {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVisaExtensionService applicantVisaExtensionService;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVisaExtensionHistoryService appVisaExtensionHisService;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicationHeaderService applicationHeaderService;

    /**
     * 
     */
    @Autowired(required = true)
    private VisaExtensionCheckService visaExtensionCheckService;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantPaymentService applicantPaymentService;

    /**
     * 
     */
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    /**
     * 
     */
    @Autowired(required = true)
    private StripeService stripeService;

    /**
     * 
     */
    @Value("${app.url}")
    private String appUrl;

    /**
     * 
     */
    @Value("${mail.sent.from}")
    private String mailFrom;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicationTrackerService applicationTrackerService;

    /**
     * 
     */
    @Autowired(required = true)
    private JmsTemplate jmsTemplate;

    /**
     * 
     */
    @Autowired(required = true)
    ModelMapper modelMapper;

    /**
     * 
     */
    @Autowired(required = true)
    private MasterCodeDetailsService masterCodeDetailsService;

    /**
     * 
     */
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
    private ApplicantVisaExtensionFileUploadService ApplicantVisaExtensionFileUploadService;
    
    @Autowired(required = true)
    private ApplicantAttachmentRepository applicantAttachmentRepository;

    /**
     * @throws IOException
     *
     */
    @Override
    @Transactional
    public ChargeResponseDTO applyVisaExtension(HttpServletRequest request,
            ApplicantVisaExtension applicantVisaExtension, MultipartFile file) throws IOException {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-applyVisaExtension");

        
        System.out.println("Enregistrement de extension de visa");
        /* save visa extension details */
        applicantVisaExtension.setDocumentStatus(CommonsConstants.PENDING_PAYMENT);
        applicantVisaExtension.setExtensionStatus(CommonsConstants.PENDING_PAYMENT);
        ApplicantVisaExtension appVisaExten = applicantVisaExtensionService
                .saveApplicantVisaExtension(applicantVisaExtension);
        System.out.println("vide*********************************************************");
        if (file != null && !file.isEmpty()) {
        	System.out.println("ayanleh*********************************************************");
        	applicantAttachmentRepository.save(new ApplicantAttachmentDetails(null, appVisaExten.getApplicationNumber(), "EX",
                    StringUtils.cleanPath(file.getOriginalFilename()), file.getContentType(), file.getBytes(),
                    file.getBytes().length / 1024));
        }
        
       

        ChargeResponseDTO chargeResponseDTO;
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(
                        appUrl + request.getContextPath() + "/#/main/success-payment?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(appUrl + request.getContextPath() + "/#/main/evisa-extension/apply-visa-list")
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency(CommonsConstants.USD)
                                .setUnitAmount(appVisaExten.getTotalAmount() * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setDescription("DJIB E-Visa Extension Checkout Request For Ref Number: "
                                                + appVisaExten.getVisaExtensionId())
                                        .setName("E-Visa Extension Payment").build())
                                .build())
                        .build())
                .build();

        try {
            Session session = Session.create(params);
            log.info("session.getId -=-=- {}", session.getId());

            /* insert payment details */
            ApplicantPaymentDetails appPaymentDtls = new ApplicantPaymentDetails(appVisaExten.getVisaExtensionId(),
                    CommonsConstants.USD, applicantVisaExtension.getTotalAmount(), 0L, "EVISA_EXT", "PP",
                    applicantVisaExtension.getUsername());
            appPaymentDtls.setPaymentInstructions(session.getId());
            applicantPaymentService.savePaymentDetails(appPaymentDtls);

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
     *
     */
    @Override
    public Page<VisaExtensionCheck> searchVisaExtensionCheck(VisaExtensionSearchDTO visaExtensionCheckSearchDTO) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-searchVisaExtensionCheck");
        return visaExtensionCheckService.searchVisaExtensionCheck(visaExtensionCheckSearchDTO);
    }

    /**
     *
     */
    @Override
    public Page<ApplicantVisaExtension> searchApplicantVisaExtension(VisaExtensionSearchDTO visaExtensionSearchDTO) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-searchApplicantVisaExtension");
        return applicantVisaExtensionService.searchApplicantVisaExtension(visaExtensionSearchDTO);
    }

    /**
     *
     */
    @Override
    public ApplicantVisaExtension fetchApplicantVisaExtension(String extensionId) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-fetchApplicantVisaExtension");
        ApplicantVisaExtension visaExtension = applicantVisaExtensionService.fetchApplicantVisaExtension(extensionId);
        visaExtension.setFileUploads(ApplicantVisaExtensionFileUploadService.findByVisaExtensionId(extensionId));
        return visaExtension;
    }

    /**
     *
     */
    @Override
    @Transactional
    public ApiResultDTO visaExtensionProcessApproval(String locale,
            ApplicantVisaExtensionHistoryDTO applicantVisaExtensionHistoryDTO) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-visaExtensionProcessApproval");

        ApiResultDTO apiResultDTO;

        try {

            ApplicantVisaExtension appVisaExtension = applicantVisaExtensionService
                    .fetchApplicantVisaExtension(applicantVisaExtensionHistoryDTO.getVisaExtensionId());

            /*
             * Need to verify whether the application is allocated to same person who
             * provided approval
             */
            if (appVisaExtension != null
                    && (appVisaExtension.getAssignedTo().equals(applicantVisaExtensionHistoryDTO.getActionBy())
                            || "VAL".equals(appVisaExtension.getDocumentStatus()))) {

                /* need to update approval details to header if its APR/REJ */
                if ("APR".equals(applicantVisaExtensionHistoryDTO.getStatus())
                        || "REJ".equals(applicantVisaExtensionHistoryDTO.getStatus())) {

                    /* set the update application header details */
                    appVisaExtension.setDocumentStatus("CLS");
                    appVisaExtension.setClosedDate(new Date());
                    appVisaExtension.setExtensionStatus(applicantVisaExtensionHistoryDTO.getStatus());
                    if ("APR".equals(applicantVisaExtensionHistoryDTO.getStatus())) {
                        Date expiryAfterExtension = CommonsUtil.addDays(appVisaExtension.getPreviousExpiryDate(),
                                appVisaExtension.getDaysOfExtension());
                        appVisaExtension.setNewExpiryDate(expiryAfterExtension);
                        //update new expire date to application header
                        applicationHeaderService.updateVisaExpiryAfterExtension(expiryAfterExtension, appVisaExtension.getApplicationNumber());
                    } else {
                        /* update the visa extension applied flag to N */
                        applicationHeaderService.updateExtensionAppliedFlag(appVisaExtension.getVisaExtensionId(),
                                CommonsConstants.NO);
                    }
                } else if ("VAL".equals(applicantVisaExtensionHistoryDTO.getStatus())) {
                    appVisaExtension.setDocumentStatus("VAL");
                }

                applicantVisaExtensionService.saveApplicantVisaExtension(appVisaExtension);

                appVisaExtensionHisService.saveApplicantVisaExtensionHistory(
                        modelMapper.map(applicantVisaExtensionHistoryDTO, ApplicantVisaExtensionHistory.class));
                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Processed Successfully!");

                /*
                 * drop the extension approval details (APR/REJ) to queue for sending to the
                 * applicant
                 */
//                if ("APR".equals(applicantVisaExtensionHistoryDTO.getStatus())
//                        || "REJ".equals(applicantVisaExtensionHistoryDTO.getStatus())) {
//                    ApplicationTracker applicationTracker = applicationTrackerService
//                            .getApplicationDetails(appVisaExtension.getApplicationNumber());
//
//                    Map<String, Object> model = new HashMap<String, Object>();
//                    model.put("title", "Extension Application - "
//                            + (applicantVisaExtensionHistoryDTO.getStatus().equals("APR") ? "Approved" : "Rejected"));
//                    model.put("username", applicationTracker.getGivenName());
//                    model.put("confirmurl", appUrl + "/applicant-api/");
//                    model.put("message", "Your Extension No " + appVisaExtension.getVisaExtensionId() + " has been "
//                            + (applicantVisaExtensionHistoryDTO.getStatus().equals("APR")
//                                    ? "Approved, Please visit the Djib Evisa website to download the approved extension visa."
//                                    : "Rejected, Please visit the Djib Evisa website to know the reasons for extension visa rejection."));
//                    jmsTemplate.convertAndSend("mailbox",
//                            new MailDTO(mailFrom, applicationTracker.getEmailAddress(),
//                                    "Djib E-Visa Extension Approval - " + appVisaExtension.getVisaExtensionId(),
//                                    "visa-approval.ftl", model));
//                }
//
            } else {
               apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                      "Extension Application is not assigned or might be deallocated");
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
    @Transactional
    public ApiResultDTO processNextSetOfExtensionFiles(String locale, String loggeduser) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-processNextSetOfExtensionFiles");

        ApiResultDTO apiResultDTO;

        /* check the logged user is Decision Maker or not */

        /* check the pending allocated list count */
        int pendingCount = applicantVisaExtensionService.checkExtensionPendingAllocatedList(loggeduser);

        if (pendingCount > 10) {
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    "More Extension Applications Are Pending For Approval");
        } else {

            /* get pending extension visa */
            List<String> extensionVisaAppList = applicantVisaExtensionService
                    .pendingExtensionVisaProcessList(PageRequest.of(0, 20));

            if (extensionVisaAppList == null || extensionVisaAppList.isEmpty()) {
                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                        "No Pending Extension Applications Found To Allocate!");
            } else {
                applicantVisaExtensionService.allocateSubmittedExtensionApplications(loggeduser, extensionVisaAppList);
                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                        "Allocated Next Set Of Extension Files Successfully!");
            }
        }

        return apiResultDTO;
    }

    /**
     *
     */
    @Override
    public ExtensionPreviewDTO extensionPreview(HttpServletRequest request, String locale, String extensionNumber) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-extensionPreview");

        ApplicantVisaExtension appVisaExtension = applicantVisaExtensionService
                .fetchApplicantVisaExtension(extensionNumber);

        /* get personal information */
        ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsService
                .findByApplicationNumber(appVisaExtension.getApplicationNumber());

        /* get passport information */
        ApplicantPassportTravelDetails applicantPassportTravelDetails = applicantPassportTravelDetailsService
                .findPassportTravelInfoByApplicantionNumber(appVisaExtension.getApplicationNumber());

        /* get attachment information */
        ApplicantAttachmentDetailsDTOList appAttDtsList = applicantAttachmentService
                .attachmentsByApplicationNumber(appVisaExtension.getApplicationNumber());

        String attachmentUrl = appUrl + request.getContextPath() + "/v1/api/attachment/";

        List<AttachmentPreviewDTO> attchUrlList = appAttDtsList.getApplicantAttachmentDTOs().stream()
                .map(v -> new AttachmentPreviewDTO(v.getAttachmentType(),
                        attachmentUrl + v.getAttachmentId() + "/" + v.getFileName()))
                .collect(Collectors.toList());

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

        return new ExtensionPreviewDTO(applicantPersonalDetails, applicantPassportTravelDetails,
                new AttachmentUrlList(attchUrlList), appVisaExtension);
    }

    /**
     *
     */
    @Override
    public DashboardDTO decisionMakerExtensionDashboard(String loggeduser, String period) {
        log.info("ApplicantVisaExtensionServiceFacadeImpl-decisionMakerExtensionDashboard");

        Map<String, Long> countMapResult = applicantVisaExtensionService.decisionMakerExtensionDashboard(loggeduser,
                period);
        
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setPending(countMapResult.get(CommonsConstants.PENDING));
        dashboardDTO.setValidation(countMapResult.get(CommonsConstants.VALIDATION));
        dashboardDTO.setRejected(countMapResult.get(CommonsConstants.REJECTED));
        dashboardDTO.setTotalAllocated(
                countMapResult.get(CommonsConstants.CLOSED)+ dashboardDTO.getPending() + countMapResult.get(CommonsConstants.VALIDATION));
        return dashboardDTO;
    }
}
