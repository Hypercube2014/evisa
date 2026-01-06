/**
 * 
 */
package com.hypercube.evisa.approver.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.domain.EmployeeProfileAttachment;

/**
 * @author SivaSreenivas
 *
 */
@Repository
@Transactional
public interface EmployeeProfileAttachmentRepository extends JpaRepository<EmployeeProfileAttachment, String> {

	/**
	 * @param username
	 * @return
	 */
	EmployeeProfileAttachment findByUsername(String username);

}
