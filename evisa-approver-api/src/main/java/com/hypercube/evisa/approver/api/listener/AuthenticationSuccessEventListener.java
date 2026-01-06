package com.hypercube.evisa.approver.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hypercube.evisa.approver.api.model.EmployeeUserDetailsDTO;
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
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserLoginAttemptService loginAttemptService;

	/**
	 *
	 */
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {

		log.info("-=-=-event.getAuthentication().getPrincipal()-=-=- {}",
				getPrincipalFromAuth(event.getAuthentication()));
		EmployeeUserDetailsDTO username = (EmployeeUserDetailsDTO) getPrincipalFromAuth(event.getAuthentication());
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
	private String getUsernameFromUserDetails(EmployeeUserDetailsDTO userDetails) {
		return userDetails.getUsername();
	}

}
