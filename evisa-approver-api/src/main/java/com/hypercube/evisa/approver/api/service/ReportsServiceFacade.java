package com.hypercube.evisa.approver.api.service;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ReportsServiceFacade {

	/**
	 * @param genericSearchDTO
	 * @return
	 */
	Page<AgentTrackerDTO> agentTracker(GenericSearchDTO genericSearchDTO);

	/**
	 * @param reportsSearchDTO
	 * @return
	 */
	Page<AgentTrackerDTO> performanceReport(ReportsSearchDTO reportsSearchDTO);

	/**
	 * @param loggeduser
	 * @param role
	 * @param oprtype
	 * @return
	 */
	ReportsSearchDTO getAgentListBasedOnOprTypeAndRole(String loggeduser, String role, String oprtype);

	/**
	 * @param reportsSearchDTO
	 * @return
	 */
	Page<AgentTrackerDTO> processReport(ReportsSearchDTO reportsSearchDTO);

	/**
	 * @param agentusername
	 * @return
	 */
	ReportsSearchDTO fetchApplicationsByLoggedUser(String agentusername);

}
