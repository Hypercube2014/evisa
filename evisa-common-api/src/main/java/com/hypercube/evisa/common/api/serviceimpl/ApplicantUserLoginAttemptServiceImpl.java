package com.hypercube.evisa.common.api.serviceimpl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantUserLoginAttempts;
import com.hypercube.evisa.common.api.repository.ApplicantUserLoginAttemptsRepository;
import com.hypercube.evisa.common.api.repository.ApplicantUserLoginRepository;
import com.hypercube.evisa.common.api.service.ApplicantUserLoginAttemptService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sivasreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicantUserLoginAttemptServiceImpl implements ApplicantUserLoginAttemptService {
    
    /**
     * 
     */
    private static final int MAX_ATTEMPTS = 3;
    
    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantUserLoginAttemptsRepository applicantUserLoginAttemptsRepository;
    
    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantUserLoginRepository applicantUserLoginRepository;

    /**
     * 
     */
    @Override
    @Transactional
    public void updateFailAttempts(String username) {
        log.info("ApplicantUserLoginAttemptServiceImpl-updateFailAttempts");
        
        ApplicantUserLoginAttempts user = getUserAttempts(username);
        if (user == null) {
            log.info("-=-=-=-user is NULL=-=-=-=-");
            if (applicantUserLoginRepository.existsByUserNameIgnoreCase(username)) {
                log.info("-=-=-=-user is NULL and exists by username =-=-=-=-");
                // if no record, insert a new
                applicantUserLoginAttemptsRepository.save(new ApplicantUserLoginAttempts(username, 1, new Date()));
            }
        } else {
            log.info("-=-=-=-User Is Not NULL =-=-=-");
            if (applicantUserLoginRepository.existsByUserNameIgnoreCase(username)) {
                log.info("-=-=-=-user is not NULL and exists by username =-=-=-=- {}", listUserAttempts(user));
                // update attempts count, +1
                applicantUserLoginAttemptsRepository.save(new ApplicantUserLoginAttempts(username, listUserAttempts(user) + 1, new Date()));
            }
            log.info("-=-=-=-MAX_ATTEMPTS Verification-=-=-=- {}", listUserAttempts(user));
            if (listUserAttempts(user) + 1 >= MAX_ATTEMPTS) {
                // locked user
                applicantUserLoginAttemptsRepository.updateUserAccountLock(false, username);
            }
        }

    }
    
    /**
     * @param user
     * @return
     */
    public int listUserAttempts(ApplicantUserLoginAttempts user) {
        return user.getAttempts();
    }

    /**
     * 
     */
    @Override
    @Transactional
    public void resetFailAttempts(String username) {
        log.info("ApplicantUserLoginAttemptServiceImpl-resetFailAttempts");
        applicantUserLoginAttemptsRepository.resetFailAttempts(username);
    }

    /**
     * 
     */
    @Override
    public ApplicantUserLoginAttempts getUserAttempts(String username) {
        log.info("ApplicantUserLoginAttemptServiceImpl-getUserAttempts");
        return applicantUserLoginAttemptsRepository.findByUsername(username);
    }

    /**
     * 
     */
    @Override
    @Transactional
    public void updateUserAccountLock(boolean status, String username) {
        log.info("ApplicantUserLoginAttemptServiceImpl-updateUserAccountLock");
        applicantUserLoginAttemptsRepository.updateUserAccountLock(status, username);
    }

    /**
     * 
     */
    @Override
    @Transactional
    public void updateUserAccountEnableAndRole(boolean result, String userName, String role) {
        log.info("ApplicantUserLoginAttemptServiceImpl-updateUserAccountEnableAndRole");
        applicantUserLoginAttemptsRepository.updateUserAccountEnableAndRole(result, userName, role);
    }

    /**
     * 
     */
    @Override
    @Transactional
    public void updateUserAccountEnabled(boolean accountenabled, String username) {
        log.info("ApplicantUserLoginAttemptServiceImpl-updateUserAccountEnabled");
        applicantUserLoginAttemptsRepository.updateUserAccountEnabled(accountenabled, username);
    }

    /**
     * 
     */
    @Override
    public boolean verifyUserName(String userName) {
        log.info("ApplicantUserLoginAttemptServiceImpl-verifyUserName");
        return applicantUserLoginRepository.existsByUserNameIgnoreCase(userName);
    }

}
