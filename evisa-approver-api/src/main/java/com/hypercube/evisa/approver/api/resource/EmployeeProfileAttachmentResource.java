package com.hypercube.evisa.approver.api.resource;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hypercube.evisa.approver.api.domain.EmployeeProfileAttachment;
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
@RestController
@Slf4j
@Data
public class EmployeeProfileAttachmentResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	EmployeeProfileAttachmentService empProfileAttachmentService;

	/**
	 * @param authorization
	 * @param locale
	 * @param username
	 * @param profilepic
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/empprofileattachment", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ApiResultDTO> profileAttachment(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @RequestPart("username") String username,
			@RequestPart("profilepic") MultipartFile profilepic) {
		log.info("UserProfileAttachmentResource-profileAttachment");
		ApiResultDTO apiResultDTO;
		try {
			apiResultDTO = empProfileAttachmentService.saveEmployeeProfileAttachment(locale,
					new EmployeeProfileAttachment(username, profilepic.getBytes()));
			apiResultDTO.setApplicationNumber(Base64.getEncoder().encodeToString(profilepic.getBytes()));
		} catch (IOException e) {
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
					LocaleConfig.getResourceValue("error.processing", null, locale, null));
		}
		return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param username
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/removeprofileattachment/{username}")
	public ResponseEntity<ApiResultDTO> removeProfileAttachment(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("username") String username) {
		log.info("UserProfileAttachmentResource-removeProfileAttachment");
		return new ResponseEntity<>(empProfileAttachmentService.removeProfileAttachment(locale, username),
				HttpStatus.OK);
	}

}
