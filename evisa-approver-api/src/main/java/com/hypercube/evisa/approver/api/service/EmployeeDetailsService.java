package com.hypercube.evisa.approver.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTOList;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeDetailsService {

	/**
	 * @param map
	 * @return
	 */
	EmployeeDetails saveEmployeeDetails(EmployeeDetails employeeDetails);

	/**
	 * @param username
	 * @return
	 */
	EmployeeDetails getEmployeeDetailsByUsername(String username);

	/**
	 * @param userName
	 * @return
	 */
	boolean verifyUserNameExists(String userName);

	/**
	 * @param employeeDetailsSearchDTO
	 * @return
	 */
	Page<EmployeeDetails> searchEmployeeDetails(EmployeeDetailsSearchDTO employeeDetailsSearchDTO);

	/**
	 * @param roleCode
	 * @return
	 */
	EmployeeDetailsDTOList getEmployeesByRole(String roleCode);

	/**
	 * @param username
	 * @param status
	 */
	void updateEmployeeStatus(String username, String status);

	/**
	 * @param loggeduser
	 * @param role
	 * @return
	 */
	List<String> employeesReportToManager(String loggeduser, String role);

	/**
	 * @param reportingManager
	 * @param agentList
	 * @return
	 */
	ApiResultDTO transferAgents(String reportingManager, List<String> agentList);

	/**
	 * @param emailid
	 * @return
	 */
	EmployeeDetails getEmployeeDetailsByEmailId(String emailid);

	/**
	 * @param email
	 * @return
	 */
	boolean verifyEmailExists(String email);

}
