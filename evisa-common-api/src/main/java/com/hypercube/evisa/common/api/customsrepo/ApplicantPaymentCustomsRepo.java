package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantPaymentCustomsRepo {
    
    /**
     * @param paymentSearchDTO
     * @return
     */
    Page<ApplicantPaymentDetails> searchApplicantPayments(PaymentSearchDTO paymentSearchDTO);

}
