package com.hypercube.evisa.approver.api.service;

import com.hypercube.evisa.approver.api.domain.EmployeeSuspensionHistory;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeSuspensionHistoryService {

	/**
	 * @param empSuspensionHistory
	 * @return
	 */
	EmployeeSuspensionHistory saveEmployeeSuspensionHistory(EmployeeSuspensionHistory empSuspensionHistory);

}
