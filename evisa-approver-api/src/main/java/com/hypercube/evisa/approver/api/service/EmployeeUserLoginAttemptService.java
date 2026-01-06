package com.hypercube.evisa.approver.api.service;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginAttempts;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeUserLoginAttemptService {

	/**
	 * @param username
	 */
	void updateFailAttempts(String username);

	/**
	 * @param username
	 */
	void resetFailAttempts(String username);

	/**
	 * @param username
	 * @return
	 */
	EmployeeUserLoginAttempts getUserAttempts(String username);

	/**
	 * @param status
	 * @param username
	 */
	void updateUserAccountLock(boolean status, String username);

	/**
	 * @param b
	 * @param userName
	 * @param role
	 */
	void updateUserAccountEnableAndRole(boolean result, String userName, String role);

	/**
	 * @param accountenabled
	 * @param username
	 * @return
	 */
	void updateUserAccountEnabled(boolean accountenabled, String username);

	/**
	 * @param roleId
	 * @param loggeduser
	 * @param locale
	 * @return
	 */
	boolean verifyUserName(String userName);
}
