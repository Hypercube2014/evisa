/**
 * 
 */
package com.hypercube.evisa.approver.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeDetailsCustomsRepo {

	/**
	 * @param employeeDetailsSearchDTO
	 * @return
	 */
	Page<EmployeeDetails> searchEmployeeDetails(EmployeeDetailsSearchDTO employeeDetailsSearchDTO);

}
