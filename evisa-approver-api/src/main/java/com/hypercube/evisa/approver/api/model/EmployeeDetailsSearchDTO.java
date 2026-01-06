package com.hypercube.evisa.approver.api.model;




import com.hypercube.evisa.common.api.model.GenericSearchDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SivaSreenivas
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeDetailsSearchDTO extends GenericSearchDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4791285466184140888L;

	/**
	 * 
	 */
	private String username;

	/**
	 * 
	 */
	private String emailId;

	/**
	 * 
	 */
	private String fullName;

	/**
	 * 
	 */
	private String role;

}
