/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantUserRegisterDetails;
import com.hypercube.evisa.common.api.repository.ApplicantUserRegisterDetailsRepository;
import com.hypercube.evisa.common.api.service.ApplicantUserRegisterDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicantUserRegisterDetailsServiceImpl implements ApplicantUserRegisterDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantUserRegisterDetailsRepository applicantUserRegisterDetailsRepository;

    /**
     * 
     */
    @Override
    public boolean verifyUserNameExists(String userName) {
        log.info("UserRegisterDetailsServiceImpl-verifyUserNameExists");
        return applicantUserRegisterDetailsRepository.existsByUserNameIgnoreCase(userName);
    }

    /**
     * 
     */
    @Override
    public ApplicantUserRegisterDetails saveUserRegisterDetails(ApplicantUserRegisterDetails userRegisterDetails) {
        log.info("UserRegisterDetailsServiceImpl-saveUserRegisterDetails");
        return applicantUserRegisterDetailsRepository.save(userRegisterDetails);
    }

    /**
     * 
     */
    @Override
    public ApplicantUserRegisterDetails getUserRegisterDetailsByUserName(String username) {
        log.info("UserRegisterDetailsServiceImpl-getUserRegisterDetailsByUserName");
        return applicantUserRegisterDetailsRepository.findByUserNameIgnoreCase(username);
    }

    /**
     * 
     */
    @Override
    public int updateAccountStatus(String active, String username) {
        log.info("UserRegisterDetailsServiceImpl-updateAccountStatus");
        return applicantUserRegisterDetailsRepository.updateAccountStatus(active, username);
    }

    /**
     *
     */
    @Override
    public boolean verifyEmailExists(String emailId) {
        log.info("UserRegisterDetailsServiceImpl-verifyEmailExists");
        return applicantUserRegisterDetailsRepository.existsByEmailIdIgnoreCase(emailId);
    }

    /**
     *
     */
    @Override
    public ApplicantUserRegisterDetails getUserDetailsByEmailId(String emailid) {
        log.info("UserRegisterDetailsServiceImpl-getUserDetailsByEmailId");
        return applicantUserRegisterDetailsRepository.findByEmailIdIgnoreCase(emailid);
    }

}
