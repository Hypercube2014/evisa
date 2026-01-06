package com.hypercube.evisa.approver.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ApproverHistoryDetailsDTO {

	/**
	 * 
	 */
	private Long historyId;

	/**
	 * 
	 */
	private String fileNumber;

	/**
	 * 
	 */
	private String applicationNumber;

	/**
	 * 
	 */
	private String extensionNumber;

	/**
	 * 
	 */
	private String approver;

	/**
	 * 
	 */
	private String status;

	/**
	 * 
	 */
	private String visaStatus;

	/**
	 * 
	 */
	private String approverRole;

	/**
	 * 
	 */
	private String remarks;

}
