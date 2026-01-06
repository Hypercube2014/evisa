package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginAttempts;
import com.hypercube.evisa.approver.api.repository.EmployeeUserLoginAttemptsRepository;
import com.hypercube.evisa.approver.api.repository.EmployeeUserLoginRepository;
import com.hypercube.evisa.approver.api.service.EmployeeUserLoginAttemptService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class EmployeeUserLoginAttemptServiceImpl implements EmployeeUserLoginAttemptService {

	/**
	 * 
	 */
	private static final int MAX_ATTEMPTS = 3;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserLoginAttemptsRepository employeeUserLoginAttemptsRepository;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserLoginRepository employeeUserLoginRepository;

	/**
	 * 
	 */
	@Override
	@Transactional
	public void updateFailAttempts(String username) {
		log.info("EmployeeUserLoginAttemptServiceImpl-updateFailAttempts");

		EmployeeUserLoginAttempts user = getUserAttempts(username);
		if (user == null) {
			log.info("-=-=-=-user is NULL=-=-=-=-");
			if (employeeUserLoginRepository.existsByUsernameIgnoreCase(username)) {
				log.info("-=-=-=-user is NULL and exists by username =-=-=-=-");
				// if no record, insert a new
				employeeUserLoginAttemptsRepository.save(new EmployeeUserLoginAttempts(username, 1, new Date()));
			}
		} else {
			log.info("-=-=-=-User Is Not NULL =-=-=-");
			if (employeeUserLoginRepository.existsByUsernameIgnoreCase(username)) {
				log.info("-=-=-=-user is not NULL and exists by username =-=-=-=- {}", listUserAttempts(user));
				// update attempts count, +1
				employeeUserLoginAttemptsRepository
						.save(new EmployeeUserLoginAttempts(username, listUserAttempts(user) + 1, new Date()));
			}
			log.info("-=-=-=-MAX_ATTEMPTS Verification-=-=-=- {}", listUserAttempts(user));
			if (listUserAttempts(user) + 1 >= MAX_ATTEMPTS) {
				// locked user
				employeeUserLoginAttemptsRepository.updateUserAccountLock(false, username);
			}
		}

	}

	/**
	 * @param user
	 * @return
	 */
	public int listUserAttempts(EmployeeUserLoginAttempts user) {
		return user.getAttempts();
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void resetFailAttempts(String username) {
		log.info("EmployeeUserLoginAttemptServiceImpl-resetFailAttempts");
		employeeUserLoginAttemptsRepository.resetFailAttempts(username);
	}

	/**
	 * 
	 */
	@Override
	public EmployeeUserLoginAttempts getUserAttempts(String username) {
		log.info("EmployeeUserLoginAttemptServiceImpl-getUserAttempts");
		return employeeUserLoginAttemptsRepository.findByUsername(username);
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void updateUserAccountLock(boolean status, String username) {
		log.info("EmployeeUserLoginAttemptServiceImpl-updateUserAccountLock");
		employeeUserLoginAttemptsRepository.updateUserAccountLock(status, username);
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void updateUserAccountEnableAndRole(boolean result, String userName, String role) {
		log.info("EmployeeUserLoginAttemptServiceImpl-updateUserAccountEnableAndRole");
		employeeUserLoginAttemptsRepository.updateUserAccountEnableAndRole(result, userName, role);
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void updateUserAccountEnabled(boolean accountenabled, String username) {
		log.info("EmployeeUserLoginAttemptServiceImpl-updateUserAccountEnabled");
		employeeUserLoginAttemptsRepository.updateUserAccountEnabled(accountenabled, username);
	}

	/**
	 * 
	 */
	@Override
	public boolean verifyUserName(String userName) {
		log.info("EmployeeUserLoginAttemptServiceImpl-verifyUserName");
		return employeeUserLoginRepository.existsByUsernameIgnoreCase(userName);
	}

}
