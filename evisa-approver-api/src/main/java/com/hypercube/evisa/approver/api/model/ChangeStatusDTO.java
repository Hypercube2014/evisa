package com.hypercube.evisa.approver.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ChangeStatusDTO {

	/**
	 * 
	 */
	private String loggeduser;

	/**
	 * 
	 */
	private String loggedUserRole;

	/**
	 * 
	 */
	private String username;

	/**
	 * 
	 */
	private boolean status;

	/**
	 * 
	 */
	private String reasons;

	/**
	 * 
	 */
	private String remarks;

}
