/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantUserLogin;
import com.hypercube.evisa.common.api.domain.ApplicantVerificationToken;
import com.hypercube.evisa.common.api.repository.ApplicantVerificationTokenRepository;
import com.hypercube.evisa.common.api.service.ApplicantVerificationTokenService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ApplicantVerificationTokenServiceImpl implements ApplicantVerificationTokenService {
    
    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVerificationTokenRepository applicantVerificationTokenRepository;

    /**
     * 
     */
    @Override
    public ApplicantUserLogin getUser(String verificationToken) {
        log.info("ApplicantVerificationTokenServiceImpl-getUser");
        return applicantVerificationTokenRepository.findByToken(verificationToken).getApplicantUserLogin();
    }

    /**
    * 
    */
    @Override
    public void createVerificationToken(ApplicantUserLogin user, String token, int expiry) {
        log.info("ApplicantVerificationTokenServiceImpl-createVerificationToken");
        applicantVerificationTokenRepository.save(new ApplicantVerificationToken(user, token, expiry));
        
    }

    /**
     * 
     */
    @Override
    public ApplicantVerificationToken getVerificationToken(String verificationToken) {
        log.info("ApplicantVerificationTokenServiceImpl-getVerificationToken");
        return applicantVerificationTokenRepository.findByToken(verificationToken);
    }

}
