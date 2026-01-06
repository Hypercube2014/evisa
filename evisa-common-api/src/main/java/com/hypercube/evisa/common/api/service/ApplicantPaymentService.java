package com.hypercube.evisa.common.api.service;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantPaymentService {
    
    /**
     * @param ApplicantPaymentDetails
     */
    void savePaymentDetails(ApplicantPaymentDetails applicantPaymentDetails);

    /**
     * @param referenceNumber
     * @param sessionId
     */
    void updatePaymentSessionId(String referenceNumber, String sessionId);

    /**
     * @param status
     * @param amount
     * @param paymentMethod
     * @param sessionId
     */
    void updatePaymentSuccess(String status, long amount, String paymentMethod, String sessionId);
    
    /**
     * @param status
     * @param amount
     * @param paymentMethod
     * @param sessionId
     */
    void updatePaymentPenalitySuccess(String status, long amount, String paymentMethod, String sessionId);

    /**
     * @param sessionId
     * @return
     */
    ApplicantPaymentDetails getFileNumberBySessionId(String sessionId);
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
    
    void updateByAmountDue(String filenumber, String sessionId, long amount);

}
