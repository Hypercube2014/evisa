package com.hypercube.evisa.applicant.api.resource;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.applicant.api.service.ApplicantAuthenticationServiceFacade;
import com.hypercube.evisa.applicant.api.service.ApplicantCommonServiceFacade;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.model.ChargeRequestDTO;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.CheckOutPaymentDTO;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;
import com.stripe.exception.StripeException;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class PaymentResource {

    /**
     * 
     */
    @Value("${stripe.public.key}")
    private String stripePublicKey;

    /**
     * 
     */
    @Autowired
    private ApplicantCommonServiceFacade applicantCommonServiceFacade;

    @Autowired(required = true)
    ApplicantAuthenticationServiceFacade applicantAuthenticationServiceFacade;

    @Autowired(required = true)
    private ApplicantPersonalDetailsService applicantPersonalDetailsService;

    

    /**
     * @param model
     * @return
     */

    /**
     * @param chargeRequest
     * @return
     * @throws StripeException
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/payment/charge")
    public ResponseEntity<ChargeResponseDTO> paymentChargeRequest(@RequestBody ChargeRequestDTO chargeRequestDTO)
            throws StripeException {
        log.info("PaymentResource-paymentChargeRequest");

        return new ResponseEntity<>(applicantCommonServiceFacade.paymentChargeRequest(chargeRequestDTO), HttpStatus.OK);
    }

    /**
     * @param request
     * @param checkOutPaymentDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/payment/checkout")
    public ChargeResponseDTO paymentWithCheckOutPage(HttpServletRequest request,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody CheckOutPaymentDTO checkOutPaymentDTO) {
        log.info("PaymentResource-paymentWithCheckOutPage");

        return applicantCommonServiceFacade.paymentWithCheckOutPage(request, checkOutPaymentDTO);
    }

    /**
     * @param sessionId
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/payment/success")
    public ChargeResponseDTO processSucessResponse(@RequestParam("session_id") String sessionId) {
        log.info("PaymentResource-processSucessResponse");
        return applicantCommonServiceFacade.processSucessResponse(sessionId);
    }

    /**
     * @param paymentSearchDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/payment/search")
    public ResponseEntity<Page<ApplicantPaymentDetails>> searchApplicantPayments(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody PaymentSearchDTO paymentSearchDTO) {
        log.info("PaymentResource-searchApplicantPayments");
        ApplicantUtil applicantUtil = new ApplicantUtil();
        if (applicantUtil.isRequestUserSameAsAuthenticated(paymentSearchDTO.getLoggedUser())) {
                log.info("User is authenticated and authorized to create a draft application file.");
        } else {
                log.error("User is not authorized to create a draft application file.");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (applicantAuthenticationServiceFacade.isAuthorized(paymentSearchDTO.getLoggedUser())) {
            return new ResponseEntity<>(applicantCommonServiceFacade.searchApplicantPayments(paymentSearchDTO),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @param filenumber
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/payment/{filenumber}")
    public ResponseEntity<ApplicantPaymentDetails> fetchApplicantPaymentDetails(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @PathVariable("filenumber") String filenumber) {
        log.info("PaymentResource-fetchApplicantPaymentDetails");
        String username = applicantPersonalDetailsService.findUsername(filenumber);
        ApplicantUtil applicantUtil = new ApplicantUtil();
        if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
                log.info("User is authenticated and authorized to create a draft application file.");
        } else {
                log.error("User is not authorized to create a draft application file.");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(applicantCommonServiceFacade.fetchApplicantPaymentDetails(filenumber),
                HttpStatus.OK);
    }

    /**
     * @param authorization
     * @param sessionid
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/payment/recieptinfo/{sessionid}")
    public ResponseEntity<Resource> paymentReceiptDetails(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @PathVariable("sessionid") String sessionid) {
        log.info("PaymentResource-paymentReceiptDetails");

        return applicantCommonServiceFacade.paymentReceiptDetails(sessionid);
    }

}
