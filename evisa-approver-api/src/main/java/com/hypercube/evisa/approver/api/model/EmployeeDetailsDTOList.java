package com.hypercube.evisa.approver.api.model;

import java.util.List;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class EmployeeDetailsDTOList {

	/**
	 * 
	 */
	private List<EmployeeDetails> employeeDetailsDTOs;

	/**
	 * @param employeeDetailsDTOs
	 */
	public EmployeeDetailsDTOList(List<EmployeeDetails> employeeDetailsDTOs) {
		super();
		this.employeeDetailsDTOs = employeeDetailsDTOs;
	}

}
