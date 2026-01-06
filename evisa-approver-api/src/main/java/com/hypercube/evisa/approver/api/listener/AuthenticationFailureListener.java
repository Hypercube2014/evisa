package com.hypercube.evisa.approver.api.listener;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hypercube.evisa.approver.api.service.EmployeeUserLoginAttemptService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Component
@Slf4j
@Data
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	/**
	 * 
	 */
	@Autowired(required = true)
	EmployeeUserLoginAttemptService loginAttemptService;

	/**
	 *
	 */
	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		String username = (String) getPrincipalFromAuth(event.getAuthentication());
		log.info("loginAttemptService-=-=-=-{}", username);
		loginAttemptService.updateFailAttempts(username);
	}

	/**
	 * @param authentication
	 * @return
	 */
	private Object getPrincipalFromAuth(Authentication authentication) {
		return authentication.getPrincipal();
	}

}
