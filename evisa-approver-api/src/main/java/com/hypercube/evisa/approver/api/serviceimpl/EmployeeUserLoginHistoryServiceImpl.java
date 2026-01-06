package com.hypercube.evisa.approver.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginHistory;
import com.hypercube.evisa.approver.api.repository.EmployeeUserLoginHistoryRepository;
import com.hypercube.evisa.approver.api.service.EmployeeUserLoginHistoryService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class EmployeeUserLoginHistoryServiceImpl implements EmployeeUserLoginHistoryService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserLoginHistoryRepository employeeUserLoginHistoryRepository;

	/**
	 * 
	 */
	@Override
	public EmployeeUserLoginHistory saveUserLoginHistory(EmployeeUserLoginHistory userLoginHistory) {
		log.info("ApplicantUserLoginHistoryServiceImpl-saveUserLoginHistory");
		return employeeUserLoginHistoryRepository.save(userLoginHistory);
	}

}
