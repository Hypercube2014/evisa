package com.hypercube.evisa.approver.api.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.approver.api.model.ApproverApplicationPreviewDTO;
import com.hypercube.evisa.approver.api.model.ApproverHistoryDetailsDTO;
import com.hypercube.evisa.approver.api.service.ApproverCommonServiceFacade;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class ApproverApplicationResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApproverCommonServiceFacade approverCommonServiceFacade;

	/**
	 * @param locale
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationpreview")
	public ResponseEntity<ApproverApplicationPreviewDTO> applicationPreview(HttpServletRequest request,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestParam("applicationNumber") String applicationNumber) {
		log.info("ApproverApplicationResource-applicationPreview");
		return new ResponseEntity<>(approverCommonServiceFacade.applicationPreview(request, locale, applicationNumber),
				HttpStatus.OK);
	}

	/**
	 * @param request
	 * @param locale
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationdeparturepreview")
	public ResponseEntity<ApproverApplicationPreviewDTO> applicationDeparturePreview(HttpServletRequest request,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestParam("applicationNumber") String applicationNumber) {
		log.info("ApproverApplicationResource-applicationDeparturePreview");
		return new ResponseEntity<>(
				approverCommonServiceFacade.applicationDeparturePreview(request, locale, applicationNumber),
				HttpStatus.OK);
	}

	/**
	 * @param id
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/attachment/{id}/{filename}")
	public ResponseEntity<Resource> getAttachment(@PathVariable("id") Long id,
			@PathVariable("filename") String filename) throws IOException {
		log.info("ApproverApplicationResource-getAttachment");
	
		ApplicantAttachmentDetails appAttDtls = approverCommonServiceFacade.getAttachment(id);
		
		System.out.println(appAttDtls.getFileName()+" name");
		System.out.println(appAttDtls.getFileType()+" file upload");
		System.out.println(appAttDtls.getFileData()+" data");
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(appAttDtls.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + appAttDtls.getFileName() + "\"")
				.body(new ByteArrayResource(appAttDtls.getFileData()));
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param approverHistoryDDetaisDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/processapproval")
	public ResponseEntity<ApiResultDTO> processApproval(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApproverHistoryDetailsDTO approverHistoryDDetaisDTO) {
		log.info("ApproverApplicationResource-processApproval");
		return new ResponseEntity<>(approverCommonServiceFacade.processApproval(locale, approverHistoryDDetaisDTO),
				HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param applicationlist
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/deallocateapplications")
	public ResponseEntity<ApiResultDTO> deallocateApplications(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ReportsSearchDTO reportsSearchDTO) {
		log.info("ApproverApplicationResource-deallocateApplications");
		ApiResultDTO apiResultDTO;
		if (reportsSearchDTO.getAgentList() == null || reportsSearchDTO.getAgentList().isEmpty()) {
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "No Applications Selected to Deallocate");
		} else {
			apiResultDTO = approverCommonServiceFacade.deallocateSelectedApplications(reportsSearchDTO.getAgentList());
		}
		return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
	}
}
