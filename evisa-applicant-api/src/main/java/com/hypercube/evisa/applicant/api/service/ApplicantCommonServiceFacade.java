package com.hypercube.evisa.applicant.api.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.applicant.api.model.ApplicationFileDTO;
import com.hypercube.evisa.applicant.api.model.CheckVisaResultDTO;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ChargeRequestDTO;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.CheckOutPaymentDTO;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;
import com.hypercube.evisa.common.api.model.PreviewDTO;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantCommonServiceFacade {

    /**
     * @param applicationFileDTO
     * @param locale
     * @return
     */
    ResponseEntity<ApiResultDTO> applicationSubmission(String locale, ApplicationFileDTO applicationFileDTO);

    /**
     * @param locale
     * @param applicationNumber
     * @return
     */
    PreviewDTO applicationPreview(String locale, String applicationNumber);

    /**
     * @param chargeRequestDTO
     * @return
     */
    ChargeResponseDTO paymentChargeRequest(ChargeRequestDTO chargeRequestDTO);

    /**
     * @param checkOutPaymentDTO
     * @return
     */
    ChargeResponseDTO paymentWithCheckOutPage(HttpServletRequest request, CheckOutPaymentDTO checkOutPaymentDTO);

    /**
     * @param sessionId
     * @return
     */
    ChargeResponseDTO processSucessResponse(String sessionId);

    /**
     * @param paymentSearchDTO
     * @return
     */
    Page<ApplicantPaymentDetails> searchApplicantPayments(PaymentSearchDTO paymentSearchDTO);

    /**
     * @param filenumber
     * @return
     */
    ApplicantPaymentDetails fetchApplicantPaymentDetails(String filenumber);

    /**
     * @param sessionid
     * @return
     */
    ResponseEntity<Resource> paymentReceiptDetails(String sessionid);

    /**
     * @param applicationNumber
     * @return
     */
    ResponseEntity<Resource> applicationDownload(String applicationNumber,HttpServletRequest request);

    /**
     * @param locale
     * @param applicationNumber
     * @return
     */
    CheckVisaResultDTO checkVisaStatus(String locale, String applicationNumber);
    
   Date dateverification();

	ChargeResponseDTO paymentPenalityWithCheckOutPage(HttpServletRequest request,
			CheckOutPaymentDTO checkOutPaymentDTO);

    /**
     * Webhook Stripe - traite les événements de paiement
     * @param payload JSON payload de Stripe
     * @param sigHeader Signature Stripe
     * @return Réponse de traitement
     */
    ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader);

}
