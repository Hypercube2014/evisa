package com.hypercube.evisa.approver.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeSuspensionHistory;
import com.hypercube.evisa.approver.api.repository.EmployeeSuspensionHistoryRepository;
import com.hypercube.evisa.approver.api.service.EmployeeSuspensionHistoryService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class EmployeeSuspensionHistoryServiceImpl implements EmployeeSuspensionHistoryService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeSuspensionHistoryRepository empSuspensionHistoryRepository;

	/**
	 *
	 */
	@Override
	public EmployeeSuspensionHistory saveEmployeeSuspensionHistory(EmployeeSuspensionHistory empSuspensionHistory) {
		log.info("EmployeeSuspensionHistoryServiceImpl-saveEmployeeSuspensionHistory");
		return empSuspensionHistoryRepository.save(empSuspensionHistory);
	}

}
