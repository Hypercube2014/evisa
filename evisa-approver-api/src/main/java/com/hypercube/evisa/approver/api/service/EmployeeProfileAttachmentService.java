package com.hypercube.evisa.approver.api.service;

import com.hypercube.evisa.approver.api.domain.EmployeeProfileAttachment;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface EmployeeProfileAttachmentService {

	/**
	 * @param userProfileAttachment
	 * @return
	 */
	ApiResultDTO saveEmployeeProfileAttachment(String locale, EmployeeProfileAttachment userProfileAttachment);

	/**
	 * @param username
	 * @return
	 */
	EmployeeProfileAttachment getEmployeeProfileAttachment(String username);

	/**
	 * @param locale
	 * @param username
	 * @return
	 */
	ApiResultDTO removeProfileAttachment(String locale, String username);

}
