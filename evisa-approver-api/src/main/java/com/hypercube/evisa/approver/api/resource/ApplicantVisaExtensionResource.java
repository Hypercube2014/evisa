package com.hypercube.evisa.approver.api.resource;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionHistoryDTO;
import com.hypercube.evisa.common.api.model.DashboardDTO;
import com.hypercube.evisa.common.api.model.ExtensionPreviewDTO;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionFileUploadService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionServiceFacade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class ApplicantVisaExtensionResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantVisaExtensionServiceFacade applicantVisaExtensionServiceFacade;

	@Autowired(required = true)
	private ApplicantVisaExtensionFileUploadService appVisaExtensionUploadService;

	/**
	 * @param locale
	 * @param authorization
	 * @param extensionId
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/visaextension/{extensionId}")
	public ResponseEntity<ApplicantVisaExtension> fetchApplicantVisaExtension(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("extensionId") String extensionId) {
		log.info("ApplicantVisaExtensionResource-fetchApplicantVisaExtension");

		return new ResponseEntity<>(applicantVisaExtensionServiceFacade.fetchApplicantVisaExtension(extensionId),
				HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param visaExtensionSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchapplicantvisaextension")
	public ResponseEntity<Page<ApplicantVisaExtension>> searchApplicantVisaExtension(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody VisaExtensionSearchDTO visaExtensionSearchDTO) {
		log.info("ApplicantVisaExtensionResource-searchApplicantVisaExtension");

		return new ResponseEntity<>(
				applicantVisaExtensionServiceFacade.searchApplicantVisaExtension(visaExtensionSearchDTO),
				HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param approverHistoryDDetaisDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/visaextension/processapproval")
	public ResponseEntity<ApiResultDTO> visaExtensionProcessApproval(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicantVisaExtensionHistoryDTO applicantVisaExtensionHistoryDTO) {
		log.info("ApplicantVisaExtensionResource-visaExtensionProcessApproval");
		return new ResponseEntity<>(applicantVisaExtensionServiceFacade.visaExtensionProcessApproval(locale,
				applicantVisaExtensionHistoryDTO), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param loggeduser
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/nextsetofextensionfiles/{loggeduser}")
	public ResponseEntity<ApiResultDTO> processNextSetOfExtensionFiles(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("loggeduser") String loggeduser) {
		log.info("ApplicantVisaExtensionResource-nextSetOfFiles");
		return new ResponseEntity<>(
				applicantVisaExtensionServiceFacade.processNextSetOfExtensionFiles(locale, loggeduser), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/visaextension/preview")
	public ResponseEntity<ExtensionPreviewDTO> extensionPreview(HttpServletRequest request,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestParam("extensionNumber") String extensionNumber) {
		log.info("ApplicantVisaExtensionResource-extensionPreview {}", extensionNumber);
		return new ResponseEntity<>(
				applicantVisaExtensionServiceFacade.extensionPreview(request, locale, extensionNumber), HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param loggeduser
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/dmdashboard/extension/{loggeduser}/{period}")
	public ResponseEntity<DashboardDTO> decisionMakerExtensionDashboard(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("loggeduser") String loggeduser, @PathVariable("period") String period) {
		log.info("ApplicantVisaExtensionResource-decisionMakerExtensionDashboard");
		return new ResponseEntity<>(
				applicantVisaExtensionServiceFacade.decisionMakerExtensionDashboard(loggeduser, period), HttpStatus.OK);
	}

//    @CrossOrigin
//    @GetMapping(value = "/v1/api/visaextension/fileupload/{fileid}")
//    public ResponseEntity<Resource> getExtensionUploadFile(
//            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization, @PathVariable("fileid") Long fileId) {
//        System.out.println("getExtensionUploadFile ------------------------------------------------------------------------------------------");
//        ApplicantVisaExtensionFileUpload fileUpload = appVisaExtensionUploadService.findByFileId(fileId);
//
//        if (fileUpload == null) {
//        	System.out.println("Dans if fileUpload ------------------------------------------------------------------------------------------");
//            return null;
//        } else {
//        	System.out.println("Dans else fileUpload ------------------------------------------------------------------------------------------");
//            try {
//            	System.out.println("Dans try fileUpload ------------------------------------------------------------------------------------------");
//                return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileUpload.getFileType()))
//                        .header(HttpHeaders.CONTENT_DISPOSITION,
//                                "attachment; filename=\"" + fileUpload.getFileName() + "\"")
//                        .body(new ByteArrayResource(CommonsUtil.decompressBytes(fileUpload.getFileData())));
//            } catch (IOException ioe) {
//                log.info("ApplicantVisaExtensionResource-getExtensionUploadFile {}", ioe.getCause());
//                return null;
//            }
//        }
//
//    }

}
