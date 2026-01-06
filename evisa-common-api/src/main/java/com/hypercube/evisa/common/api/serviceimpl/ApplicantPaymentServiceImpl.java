/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hypercube.evisa.common.api.customsrepoimpl.ApplicantTravelHistoryCustomsRepoImpl;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;
import com.hypercube.evisa.common.api.repository.ApplicantPaymentRepository;
import com.hypercube.evisa.common.api.service.ApplicantPaymentService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
@Service
public class ApplicantPaymentServiceImpl implements ApplicantPaymentService {

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantPaymentRepository applicationPaymentRepository;

    /**
     * 
     */
    @Override
    @Transactional
    public void savePaymentDetails(ApplicantPaymentDetails applicantPaymentDetails) {
        log.info("ApplicantPaymentServiceImpl-savePaymentDetails");
        applicationPaymentRepository.save(applicantPaymentDetails);
    }

    /**
     *
     */
    @Override
    @Transactional
    public void updatePaymentSessionId(String referenceNumber, String sessionId) {
        log.info("ApplicantPaymentServiceImpl-updatePaymentSessionId");
        applicationPaymentRepository.updatePaymentSessionId(referenceNumber, sessionId);
    }

    /**
     *
     */
    @Override
    @Transactional
    public void updatePaymentSuccess(String status, long amount, String paymentMethod, String sessionId) {
        log.info("ApplicantPaymentServiceImpl-updatePaymentSuccess");
        applicationPaymentRepository.updatePaymentSuccess(status, amount, paymentMethod, sessionId, new Date());
    }

    /**
     *
     */
    @Override
    public ApplicantPaymentDetails getFileNumberBySessionId(String sessionId) {
        log.info("ApplicantPaymentServiceImpl-getFileNumberBySessionId");
        return applicationPaymentRepository.getFileNumberBySessionId(sessionId);
    }

    /**
     *
     */
    @Override
    public Page<ApplicantPaymentDetails> searchApplicantPayments(PaymentSearchDTO paymentSearchDTO) {
        log.info("ApplicantPaymentServiceImpl-searchApplicantPayments");
        return applicationPaymentRepository.searchApplicantPayments(paymentSearchDTO);
    }

    /**
     *
     */
    @Override
    public ApplicantPaymentDetails fetchApplicantPaymentDetails(String filenumber) {
        log.info("ApplicantPaymentServiceImpl-fetchApplicantPaymentDetails");
        return applicationPaymentRepository.findByFileNumber(filenumber);
    }
    
    
    @Override
    @Transactional
    public void updateByAmountDue(String filenumber, String sessionId, long amount) {
        log.info("ApplicantPaymentServiceImpl-updateByAmountDue");
        applicationPaymentRepository.updatePaymentAmount(filenumber, sessionId, amount);
    }

	@Override
	@Transactional
	public void updatePaymentPenalitySuccess(String status, long amount, String paymentMethod, String sessionId) {
		log.info("ApplicantPaymentServiceImpl-updatePaymentPenalitySuccess");
        applicationPaymentRepository.updatePaymentPenalitySuccess(status, amount, paymentMethod, sessionId, new Date());
		
	}



}
