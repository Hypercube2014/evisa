package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;
import com.hypercube.evisa.common.api.model.ApplicantTravelHistorySearchDTO;
import com.hypercube.evisa.common.api.repository.ApplicantTravelHistoryRepository;
import com.hypercube.evisa.common.api.service.ApplicantTravelHistoryService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
public class ApplicantTravelHistoryServiceImpl implements ApplicantTravelHistoryService {
    
    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantTravelHistoryRepository applTravelHisRepo;

    /**
     *
     */
    @Override
    public Page<ApplicantTravelHistory> searchApplicantTravelHistory(
            ApplicantTravelHistorySearchDTO applTravelHisSearchDTO) {
        log.info("ApplicantTravelHistoryServiceImpl-searchApplicantTravelHistory");
        return applTravelHisRepo.searchApplicantTravelHistory(applTravelHisSearchDTO);
    }

}
