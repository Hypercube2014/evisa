package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTOList;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;
import com.hypercube.evisa.approver.api.repository.EmployeeDetailsRepository;
import com.hypercube.evisa.approver.api.service.EmployeeDetailsService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeDetailsRepository employeeDetailsRepository;

	/**
	 * 
	 */
	@Override
	public EmployeeDetails saveEmployeeDetails(EmployeeDetails employeeDetails) {
		log.info("EmployeeDetailsServiceImpl-saveEmployeeDetails");

		return employeeDetailsRepository.save(employeeDetails);

	}

	/**
	 * 
	 */
	@Override
	public boolean verifyUserNameExists(String username) {
		log.info("EmployeeDetailsServiceImpl-verifyUserNameExists");

		return employeeDetailsRepository.existsByUsername(username);

	}

	/**
	 * 
	 */
	@Override
	public EmployeeDetails getEmployeeDetailsByUsername(String username) {
		log.info("EmployeeDetailsServiceImpl-getEmployeeDetailsByUsername");
		return employeeDetailsRepository.findByUsername(username);
	}

	/**
	 * 
	 */
	@Override
	public Page<EmployeeDetails> searchEmployeeDetails(EmployeeDetailsSearchDTO employeeDetailsSearchDTO) {
		log.info("EmployeeDetailsServiceImpl-searchEmployeeDetails");
		return employeeDetailsRepository.searchEmployeeDetails(employeeDetailsSearchDTO);
	}

	/**
	 * 
	 */
	@Override
	public EmployeeDetailsDTOList getEmployeesByRole(String roleCode) {
		log.info("EmployeeDetailsServiceImpl-getEmployeesByRole");
		return new EmployeeDetailsDTOList(employeeDetailsRepository.getEmployeesByRole(roleCode));
	}

	/**
	 *
	 */
	@Override
	public void updateEmployeeStatus(String username, String status) {
		log.info("EmployeeDetailsServiceImpl-getEmployeesByRole");
		employeeDetailsRepository.updateEmployeeStatus(username, status);
	}

	/**
	 *
	 */
	@Override
	public List<String> employeesReportToManager(String loggeduser, String role) {
		log.info("EmployeeDetailsServiceImpl-employeesReportToManager");
		return employeeDetailsRepository.employeesReportToManager(loggeduser, role);
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public ApiResultDTO transferAgents(String reportingManager, List<String> agentList) {
		log.info("EmployeeDetailsServiceImpl-transferAgents");
		employeeDetailsRepository.transferAgents(reportingManager, agentList);
		return new ApiResultDTO(CommonsConstants.SUCCESS, "Transferred Agents Successfully!");
	}

	/**
	 *
	 */
	@Override
	public EmployeeDetails getEmployeeDetailsByEmailId(String emailid) {
		log.info("EmployeeDetailsServiceImpl-getEmployeeDetailsByEmailId");
		return employeeDetailsRepository.findByEmailIgnoreCase(emailid);
	}

	/**
	 *
	 */
	@Override
	public boolean verifyEmailExists(String emailid) {
		log.info("EmployeeDetailsServiceImpl-verifyEmailExists");
		return employeeDetailsRepository.existsByEmailIgnoreCase(emailid);
	}

}
