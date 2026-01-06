/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantUserLoginHistory;
import com.hypercube.evisa.common.api.repository.ApplicantUserLoginHistoryRepository;
import com.hypercube.evisa.common.api.service.ApplicantUserLoginHistoryService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicantUserLoginHistoryServiceImpl implements ApplicantUserLoginHistoryService {
    
    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantUserLoginHistoryRepository applicantUserLoginHistoryRepository;
    
    /**
     * 
     */
    @Override
    public ApplicantUserLoginHistory saveUserLoginHistory(ApplicantUserLoginHistory userLoginHistory) {
        log.info("ApplicantUserLoginHistoryServiceImpl-saveUserLoginHistory");
        return applicantUserLoginHistoryRepository.save(userLoginHistory);
    }

}
