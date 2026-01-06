package com.hypercube.evisa.approver.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class PasswordCheckDTO {

	/**
	 * 
	 */
	private String username;

	/**
	 * 
	 */
	private boolean match;

}
