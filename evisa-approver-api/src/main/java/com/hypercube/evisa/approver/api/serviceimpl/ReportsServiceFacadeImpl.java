package com.hypercube.evisa.approver.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.service.ReportsServiceFacade;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ReportsServiceFacadeImpl implements ReportsServiceFacade {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationHeaderService applicationHeaderService;

	/**
	 *
	 */
	@Override
	public Page<AgentTrackerDTO> agentTracker(GenericSearchDTO genericSearchDTO) {
		log.info("ReportsServiceFacadeImpl-agentTracker");

		return applicationHeaderService.agentTrackerDetails(genericSearchDTO);
	}

	/**
	 *
	 */
	@Override
	public Page<AgentTrackerDTO> performanceReport(ReportsSearchDTO reportsSearchDTO) {
		log.info("ReportsServiceFacadeImpl-performanceReport");
		return applicationHeaderService.performanceReport(reportsSearchDTO);
	}

	/**
	 *
	 */
	@Override
	public ReportsSearchDTO getAgentListBasedOnOprTypeAndRole(String loggeduser, String role, String oprtype) {
		log.info("ReportsServiceFacadeImpl-getAgentListBasedOnOprTypeAndRole");
		return applicationHeaderService.getAgentListBasedOnOprTypeAndRole(loggeduser, role, oprtype);
	}

	/**
	 *
	 */
	@Override
	public Page<AgentTrackerDTO> processReport(ReportsSearchDTO reportsSearchDTO) {
		log.info("ReportsServiceFacadeImpl-processReport");
		return applicationHeaderService.processReport(reportsSearchDTO);
	}

	/**
	 *
	 */
	@Override
	public ReportsSearchDTO fetchApplicationsByLoggedUser(String agentusername) {
		log.info("ReportsServiceFacadeImpl-fetchApplicationsByLoggedUser");
		return new ReportsSearchDTO(applicationHeaderService.fetchApplicationsByLoggedUser(agentusername));
	}

}
