package com.hypercube.evisa.applicant.api.listener;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hypercube.evisa.applicant.api.model.ApplicantUserDetailsDTO;
import com.hypercube.evisa.common.api.service.ApplicantUserLoginAttemptService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Component
@Data
@Slf4j
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantUserLoginAttemptService loginAttemptService;

    /**
     *
     */
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        log.info("-=-=-event.getAuthentication().getPrincipal()-=-=- {}",
                getPrincipalFromAuth(event.getAuthentication()));
        ApplicantUserDetailsDTO username = (ApplicantUserDetailsDTO) getPrincipalFromAuth(event.getAuthentication());
        log.info("loginAttemptService-=-=-=- {}", getUsernameFromUserDetails(username));
        loginAttemptService.resetFailAttempts(getUsernameFromUserDetails(username));
    }

    /**
     * @param authentication
     * @return
     */
    private Object getPrincipalFromAuth(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /**
     * @param userDetails
     * @return
     */
    private String getUsernameFromUserDetails(ApplicantUserDetailsDTO userDetails) {
        return userDetails.getUsername();
    }

}
