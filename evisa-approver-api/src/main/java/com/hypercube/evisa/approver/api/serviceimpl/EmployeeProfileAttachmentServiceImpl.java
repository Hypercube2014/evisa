/**
 * 
 */
package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeProfileAttachment;
import com.hypercube.evisa.approver.api.repository.EmployeeProfileAttachmentRepository;
import com.hypercube.evisa.approver.api.service.EmployeeProfileAttachmentService;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class EmployeeProfileAttachmentServiceImpl implements EmployeeProfileAttachmentService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeProfileAttachmentRepository userProfileAttachmentRepository;

	/**
	 * 
	 */
	@Override
	public ApiResultDTO saveEmployeeProfileAttachment(String locale, EmployeeProfileAttachment userProfileAttachment) {
		log.info("UserProfileAttachmentServiceImpl-saveEmployeeProfileAttachment");
		userProfileAttachmentRepository.save(userProfileAttachment);

		return new ApiResultDTO(CommonsConstants.SUCCESS,
				LocaleConfig.getResourceValue("save.success", null, locale, null),
				Base64.getEncoder().encodeToString(userProfileAttachment.getFileData()));
	}

	/**
	 * 
	 */
	@Override
	public EmployeeProfileAttachment getEmployeeProfileAttachment(String username) {
		log.info("UserProfileAttachmentServiceImpl-getEmployeeProfileAttachment");
		return userProfileAttachmentRepository.findByUsername(username);
	}

	/**
	 * 
	 */
	@Override
	public ApiResultDTO removeProfileAttachment(String locale, String username) {
		log.info("UserProfileAttachmentServiceImpl-getEmployeeProfileAttachment");
		userProfileAttachmentRepository.deleteById(username);
		return new ApiResultDTO(CommonsConstants.SUCCESS,
				LocaleConfig.getResourceValue("remove.success", null, locale, null), "");
	}

}
