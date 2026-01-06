package com.hypercube.evisa.applicant.api.resource;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hypercube.evisa.applicant.api.domain.ApplicationVisaDetails;
import com.hypercube.evisa.applicant.api.model.ApplicationFileDTO;
import com.hypercube.evisa.applicant.api.model.ApplicationVisaSearchDTO;
import com.hypercube.evisa.applicant.api.model.CheckVisaResultDTO;
import com.hypercube.evisa.applicant.api.service.ApplicantAuthenticationServiceFacade;
import com.hypercube.evisa.applicant.api.service.ApplicantCommonServiceFacade;
import com.hypercube.evisa.applicant.api.service.ApplicantFileService;
import com.hypercube.evisa.applicant.api.service.ApplicationVisaDetailsService;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicationFile;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicationSearchTrackerDTO;
import com.hypercube.evisa.common.api.model.PreviewDTO;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ApplicantFileResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3709558243471682780L;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantFileService applicationFileService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationVisaDetailsService applicationVisaDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantCommonServiceFacade applicantCommonServiceFacade;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationTrackerService applicationTrackerService;

	@Autowired(required = true)
	ApplicantAuthenticationServiceFacade applicantAuthenticationServiceFacade;

	@Autowired(required = true)
	private ApplicantPersonalDetailsService applicantPersonalDetailsService;

	/**
	 * @param authorization
	 * @param locale
	 * @param applicantFileDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationfile")
	public ResponseEntity<ApplicationFile> createDraftApplicationFile(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationFileDTO applicantFileDTO) {
		log.info("ApplicantFileResource-createDraftApplicationFile");

		return applicationFileService.createDraftApplicationFile(applicantFileDTO);

	}

	/**
	 * @param authorization
	 * @param applicationVisaSearchDTO
	 * @return
	 */
	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/searchapplicationfile")
	public Page<ApplicationVisaDetails> searchDraftApplicationFile(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationVisaSearchDTO applicationVisaSearchDTO) {
		log.info("ApplicantFileResource-searchDraftApplicationFile");

		String username = applicationVisaSearchDTO.getLoggedUser();
		ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		// if (applicantAuthenticationServiceFacade.isAuthorized(u)) {

		return applicationVisaDetailsService.searchDraftApplicationFile(applicationVisaSearchDTO);
		// }else{
		// throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not
		// authorized");
		// }

	}

	// /**
	// * @param authorization
	// * @param applicationVisaSearchDTO
	// * @return Page<ApplicationVisaDetails>
	// */
	// @CrossOrigin
	// @ResponseStatus(code = HttpStatus.OK)
	// @PostMapping(value = "/v1/api/searchapplicationfile")
	// public Page<ApplicationVisaDetails> searchDraftApplicationFile(
	// @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
	// @RequestHeader("username") String username,
	// @RequestBody ApplicationVisaSearchDTO applicationVisaSearchDTO) {

	// log.info("ApplicantFileResource-searchDraftApplicationFile, User: {}",
	// username);

	// // Check if the user is authorized
	// if (applicantAuthenticationServiceFacade.isAuthorized(username)) {
	// return
	// applicationVisaDetailsService.searchDraftApplicationFile(applicationVisaSearchDTO);
	// } else {
	// throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not
	// authorized");
	// }
	// }

	/**
	 * @param locale
	 * @param authorization
	 * @param fileNumber
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationfile/{fileNumber}")
	public ResponseEntity<ApplicationFile> findApplicationsByFileNumber(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("fileNumber") String fileNumber) {
		log.info("ApplicantFileResource-findApplicationsByFileNumber");

		String username;
		if (fileNumber == null || fileNumber.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			username = applicantPersonalDetailsService.findUsername(fileNumber);
		}
		ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return applicationFileService.findApplicationsByFileNumber(fileNumber);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param applicationFileDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationfile/submission")
	public ResponseEntity<ApiResultDTO> applicationSubmission(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationFileDTO applicationFileDTO) {
		log.info("ApplicantFileResource-applicationSubmission");

		String fileNumber = applicationFileDTO.getFileNumber();
		String username;
		if (fileNumber == null || fileNumber.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			username = applicantPersonalDetailsService.findUsername(fileNumber);
		}
		ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return applicantCommonServiceFacade.applicationSubmission(locale, applicationFileDTO);
	}

	/**
	 * @param authorization
	 * @param applicationSearchTrackerDTO
	 * @return
	 */
	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/searchapplicationtracker")
	public Page<ApplicationTracker> searchApplicationTracker(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
		log.info("Entered the search application tracker");
		log.info("ApplicantFileResource-searchApplicationTracker");
		System.out.println(
				applicationSearchTrackerDTO.getDocStatus() + "Logusser" +
						applicationSearchTrackerDTO.getLoggedUser());
						
		String username = applicationSearchTrackerDTO.getLoggedUser();
		ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		if (applicantAuthenticationServiceFacade.isAuthorized(applicationSearchTrackerDTO.getLoggedUser())) {
			return applicationTrackerService.searchApplicationInTracker(applicationSearchTrackerDTO);
		} else {
			return Page.empty();
		}
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationpreview")
	public ResponseEntity<PreviewDTO> applicationPreview(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestParam("applicationNumber") String applicationNumber) {
		log.info("ApplicantFileResource-applicationPreview");

		String fileNumber = applicantPersonalDetailsService.findFileNumber(applicationNumber);
		String username;
		if (fileNumber == null || fileNumber.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			username = applicantPersonalDetailsService.findUsername(fileNumber);
		}
		ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(applicantCommonServiceFacade.applicationPreview(locale, applicationNumber),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/application/download/{applicationNumber}")
	public ResponseEntity<Resource> applicationDownload(HttpServletRequest request,
			@PathVariable("applicationNumber") String applicationNumber) {
		log.info("ApplicantFileResource-applicationDownload");

		String fileNumber = applicantPersonalDetailsService.findFileNumber(applicationNumber);
		String username;
		if (fileNumber == null || fileNumber.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			username = applicantPersonalDetailsService.findUsername(fileNumber);
		}
		/*ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}*/

		return applicantCommonServiceFacade.applicationDownload(applicationNumber, request);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/application/checkstatus/{applicationNumber}")
	public ResponseEntity<CheckVisaResultDTO> checkVisaStatus(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("applicationNumber") String applicationNumber) {
		log.info("ApplicantFileResource-checkVisaStatus");

		String fileNumber = applicantPersonalDetailsService.findFileNumber(applicationNumber);
		String username;
		if (fileNumber == null || fileNumber.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			username = applicantPersonalDetailsService.findUsername(fileNumber);
		}
		ApplicantUtil applicantUtil = new ApplicantUtil();
		if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
			log.info("User is authenticated and authorized to create a draft application file.");
		} else {
			log.error("User is not authorized to create a draft application file.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(applicantCommonServiceFacade.checkVisaStatus(locale, applicationNumber),
				HttpStatus.OK);
	}

}
