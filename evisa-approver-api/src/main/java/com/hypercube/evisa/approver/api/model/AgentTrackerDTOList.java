package com.hypercube.evisa.approver.api.model;

import java.util.List;

import com.hypercube.evisa.common.api.model.DashboardDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class AgentTrackerDTOList {

	/**
	 * 
	 */
	private List<DashboardDTO> agentTrackerDTOs;

	/**
	 * @param agentTrackerDTOs
	 */
	public AgentTrackerDTOList(List<DashboardDTO> agentTrackerDTOs) {
		super();
		this.agentTrackerDTOs = agentTrackerDTOs;
	}

}
