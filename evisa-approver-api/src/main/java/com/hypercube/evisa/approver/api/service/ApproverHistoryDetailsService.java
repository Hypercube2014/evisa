package com.hypercube.evisa.approver.api.service;

import java.util.List;

import com.hypercube.evisa.common.api.domain.ApproverHistoryDetails;

/**
 * @author SivaSreenivas
 *
 */
public interface ApproverHistoryDetailsService {

	/**
	 * @param approverHistoryDetails
	 * @return
	 */
	ApproverHistoryDetails saveApproverHistoryDetails(ApproverHistoryDetails approverHistoryDetails);

	/**
	 * @param applicationNumber
	 * @return
	 */
	List<ApproverHistoryDetails> findAllApplicationHistory(String applicationNumber);

}
