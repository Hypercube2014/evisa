package com.hypercube.evisa.approver.api.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.model.ChangeStatusDTO;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTO;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTOList;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;
import com.hypercube.evisa.approver.api.model.EmployeeUserAuthenticationDTO;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;
import com.hypercube.evisa.common.api.model.UserDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeAuthenticationServiceFacade {

	/**
	 * @param locale
	 * @param authorization
	 * @param employeeDetailsDTO
	 * @return
	 */
	ResponseEntity<ApiResultDTO> registerEmployee(String locale, String authorization,
			EmployeeDetailsDTO employeeDetailsDTO);

	/**
	 * @param locale
	 * @param authorization
	 * @param userDTO
	 * @return
	 */
	ResponseEntity<EmployeeUserAuthenticationDTO> authenticateEmployee(String authorization,
			UserDTO userDTO);

	/**
	 * @param locale
	 * @param authorization
	 * @param userDTO
	 * @return
	 */
	ResponseEntity<EmployeeUserAuthenticationDTO> updateEmployeecredentials(String locale, String authorization,
			UserDTO userDTO);

	/**
	 * @param locale
	 * @param authorization
	 * @param username
	 * @return
	 */
	ResponseEntity<EmployeeUserAuthenticationDTO> resetEmployeeSecretKey(String locale, String authorization,
			String username);

	/**
	 * @param locale
	 * @param authorization
	 * @param employeeDetailsDTO
	 * @return
	 */
	ResponseEntity<ApiResultDTO> modifyRegisterEmployee(String locale, String authorization,
			EmployeeDetailsDTO employeeDetailsDTO);

	/**
	 * @param locale
	 * @param authorization
	 * @param username
	 * @return
	 */
	ResponseEntity<EmployeeDetails> employeeProfile(String locale, String username);

	/**
	 * @param username
	 * @return
	 */
	boolean employeeUserExists(String username);

	/**
	 * @param employeeDetailsSearchDTO
	 * @return
	 */
	ResponseEntity<Page<EmployeeDetails>> searchEmployeeDetails(EmployeeDetailsSearchDTO employeeDetailsSearchDTO);

	/**
	 * @param locale
	 * @param authorization
	 * @param userName
	 * @return
	 */
	ResponseEntity<EmployeeUserAuthenticationDTO> resetsecretkey(String locale, String authorization, String userName);

	/**
	 * @param result
	 * @param username
	 * @return
	 */
	ResponseEntity<EmployeeUserAuthenticationDTO> updateUserAccountLock(boolean result, String username);

	/**
	 * @param roleCode
	 * @return
	 */
	EmployeeDetailsDTOList getEmployeesByRole(String roleCode);

	/**
	 * @param locale
	 * @param changeStatusDTO
	 * @return
	 */
	ApiResultDTO changeEmployeeStatus(String locale, ChangeStatusDTO changeStatusDTO);

	/**
	 * @param reportsSearchDTO
	 * @return
	 */
	ApiResultDTO transferAgents(ReportsSearchDTO reportsSearchDTO);

	/**
	 * @param locale
	 * @param username
	 * @return
	 */
	ResponseEntity<EmployeeDetails> employeeProfileDesc(String locale, String username);

	/**
	 * @param locale
	 * @param authorization
	 * @param emailid
	 * @param address
	 * @return
	 */
	ResponseEntity<ApiResultDTO> forgetPassword(String locale, String authorization, String emailid, String address);
}
