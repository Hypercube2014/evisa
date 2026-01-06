package com.hypercube.evisa.approver.api.util;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLogin;
import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginAttempts;
import com.hypercube.evisa.approver.api.model.EmployeeUserDetailsDTO;
import com.hypercube.evisa.approver.api.repository.EmployeeUserLoginAttemptsRepository;
import com.hypercube.evisa.approver.api.repository.EmployeeUserLoginRepository;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Transactional
@Slf4j
@Data
public class EmployeeUserDetailsService implements UserDetailsService {

	/**
	 * 
	 */
	private static final int MILLI_SECONDS = 1200000;

	/**
	 * 
	 */
	@Autowired(required = true)
	EmployeeUserLoginRepository employeeUserLoginRepository;

	/**
	 * 
	 */
	@Autowired(required = true)
	EmployeeUserLoginAttemptsRepository employeeUserLoginAttemptsRepository;

	/**
	 *
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("EmployeeUserDetailsService-loadUserByUsername");

		EmployeeUserLogin user = employeeUserLoginRepository.findByUsernameIgnoreCase(username);

		if (user == null) {
			throw new UsernameNotFoundException("User Not found: " + username);
		} else {
			return verifyUserDetails(user);
		}
	}

	/**
	 * @param user
	 * @return
	 */
	private UserDetails verifyUserDetails(EmployeeUserLogin user) {
		log.info("EmployeeUserDetailsService-verifyUserDetails");
		if ((CommonsUtil.getTime(user.getCredentialsExpiryDate())
				- CommonsUtil.getTime(Calendar.getInstance().getTime())) <= 0) {
			employeeUserLoginAttemptsRepository.updateChangePasswordRequired(true, user.getUsername());
		}

		EmployeeUserLoginAttempts userAttempts = employeeUserLoginAttemptsRepository.findByUsername(user.getUsername());

		/* verifying whether the account is locked */
		if (!user.isAccountNonLocked()) {

			/* account is locked */

			/*
			 * checking the last login attempt with wrong credentials - less than 20 Minutes
			 */
			if ((CommonsUtil.getTime(Calendar.getInstance().getTime())
					- getUserLastModifiedTime(userAttempts)) <= MILLI_SECONDS) {

				/*
				 * identified its less than 20 Minutes and no change to the account is locked
				 */
				user.setAccountNonLocked(false);
			} else {

				/*
				 * its more than 20mins since the last wrong attempt and reseting fail attempts
				 * and also removing the account lock
				 */
				employeeUserLoginAttemptsRepository.resetFailAttempts(user.getUsername());
				employeeUserLoginAttemptsRepository.updateUserAccountLock(true, user.getUsername());
				user.setAccountNonLocked(true);
			}

			/* account non locked & checking no user attempts */
		} else if (userAttempts == null) {

			/* no entries in user attempts so account non locked */
			user.setAccountNonLocked(true);

			/* account non locked & checking user attempts available */
		} else if (listUserAttempts(userAttempts) > 0) {

			/* user attempts are available even the account is non locked */

			/* identifying the last user attempt */
			if ((CommonsUtil.getTime(Calendar.getInstance().getTime())
					- getUserLastModifiedTime(userAttempts)) >= MILLI_SECONDS) {

				/*
				 * user attempting after 20 Minutes and need to reset failed attempts and update
				 * account non lock as true
				 */
				employeeUserLoginAttemptsRepository.resetFailAttempts(user.getUsername());
				employeeUserLoginAttemptsRepository.updateUserAccountLock(true, user.getUsername());
			}

			/* account non locked */
			user.setAccountNonLocked(true);
		}
		return new EmployeeUserDetailsDTO(user);

	}

	private int listUserAttempts(EmployeeUserLoginAttempts userAttempts) {
		log.info("EmployeeUserDetailsService-listUserAttempts");
		return userAttempts.getAttempts();
	}

	/**
	 * @param userAttempts
	 * @return
	 */
	private long getUserLastModifiedTime(EmployeeUserLoginAttempts userAttempts) {
		log.info("EmployeeUserDetailsService-getUserLastModifiedTime");
		return CommonsUtil.getTime(userAttempts.getLastModified());
	}

	/**
	 * @param username
	 * @return
	 */
	public EmployeeUserLogin getUserDetailsByUserName(String username) {
		log.info("EmployeeUserDetailsService-getUserDetailsByUserName");
		return employeeUserLoginRepository.findByUsernameIgnoreCase(username);
	}

	/**
	 * @param userLogin
	 * @return
	 */
	public EmployeeUserLogin generateUserCredentials(EmployeeUserLogin userLogin) {
		log.info("EmployeeUserDetailsService-generateUserCredentials");
		return employeeUserLoginRepository.save(userLogin);
	}

	/**
	 * @param userlogin
	 * @return
	 */
	public EmployeeUserLogin updateUserCredentials(EmployeeUserLogin userlogin) {
		log.info("EmployeeUserDetailsService-updateUserCredentials");
		return employeeUserLoginRepository.save(userlogin);
	}

	/**
	 * @param accountenabled
	 * @param username
	 */
	public void updateUserAccountEnabled(boolean accountenabled, String username) {
		log.info("EmployeeUserDetailsService-updateUserAccountEnabled");
		employeeUserLoginAttemptsRepository.updateUserAccountEnabled(accountenabled, username);
	}

	/**
	 * @param username
	 * @param role
	 */
	public void updateUserRole(String username, String role) {
		log.info("EmployeeUserDetailsService-updateUserRole");
		employeeUserLoginRepository.updateUserRole(username, role);
	}

	/**
	 * @param username
	 * @param encodePassword
	 */
	public void forgetPasswordChange(String username, String encodePassword) {
		log.info("EmployeeUserDetailsService-forgetPasswordChange");
		employeeUserLoginRepository.forgetPasswordChange(username, encodePassword);
	}

}
