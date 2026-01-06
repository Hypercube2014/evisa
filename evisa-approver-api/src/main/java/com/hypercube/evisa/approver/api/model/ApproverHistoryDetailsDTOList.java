package com.hypercube.evisa.approver.api.model;

import java.util.List;

import com.hypercube.evisa.common.api.domain.ApproverHistoryDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ApproverHistoryDetailsDTOList {

	/**
	 * 
	 */
	private List<ApproverHistoryDetails> ApproverHistoryDetailsDTOs;

	/**
	 * @param approverHistoryDetailsDTOs
	 */
	public ApproverHistoryDetailsDTOList(List<ApproverHistoryDetails> approverHistoryDetailsDTOs) {
		super();
		ApproverHistoryDetailsDTOs = approverHistoryDetailsDTOs;
	}

}