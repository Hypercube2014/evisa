package com.hypercube.evisa.approver.api.service;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginHistory;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeUserLoginHistoryService {

	/**
	 * @param userLoginHistory
	 * @return
	 */
	EmployeeUserLoginHistory saveUserLoginHistory(EmployeeUserLoginHistory userLoginHistory);
}
