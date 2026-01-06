package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionHistory;
import com.hypercube.evisa.common.api.repository.ApplicantVisaExtensionHistoryRepository;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionHistoryService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class ApplicantVisaExtensionHistoryServiceImpl implements ApplicantVisaExtensionHistoryService {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVisaExtensionHistoryRepository applicantVisaExtensionHisRepo;

    /**
     *
     */
    @Override
    public ApplicantVisaExtensionHistory saveApplicantVisaExtensionHistory(
            ApplicantVisaExtensionHistory applicantVisaExtensionHistory) {
        log.info("ApplicantVisaExtensionHistoryServiceImpl-saveApplicantVisaExtensionHistory");
        return applicantVisaExtensionHisRepo.save(applicantVisaExtensionHistory);
    }

}
