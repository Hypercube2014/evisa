/**
 * 
 */
package com.hypercube.evisa.applicant.api.resource;

import java.io.Serializable;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantPassportTravelDetailsDTO;
import com.hypercube.evisa.common.api.service.ApplicantPassportTravelDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ApplicantPassportTravelResource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2518283064400972775L;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantPassportTravelDetailsService applicantPassportTravelDetailsService;

    @Autowired(required = true)
    private ApplicantPersonalDetailsService applicantPersonalDetailsService;

    /**
     * @param locale
     * @param authorization
     * @param applicantPassportTravelDetailsDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/applicanttravelinfo")
    public ResponseEntity<ApiResultDTO> createApplicantTravelDetails(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody ApplicantPassportTravelDetailsDTO applicantPassportTravelDetailsDTO) {
        log.info("ApplicantPassportTravelResource-createApplicantTravelDetails");

        String fileNumber = applicantPersonalDetailsService.findFileNumber(applicantPassportTravelDetailsDTO.getApplicationNumber());
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
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

        return new ResponseEntity<>(applicantPassportTravelDetailsService.savePassportTravelDetails(locale,
                applicantPassportTravelDetailsDTO), HttpStatus.OK);
    }

    /**
     * @param locale
     * @param authorization
     * @param applicationnumber
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/applicanttravelinfo/{applicationnumber}")
    public ResponseEntity<ApplicantPassportTravelDetails> findPassportTravelInfoByApplicantionNumber(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @PathVariable("applicationnumber") String applicationnumber) {
        log.info("ApplicantResource-findPassportTravelInfoByApplicantionNumber");

        String fileNumber = applicantPersonalDetailsService.findFileNumber(applicationnumber);
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
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

        return new ResponseEntity<>(
                applicantPassportTravelDetailsService.findPassportTravelInfoByApplicantionNumber(applicationnumber),
                HttpStatus.OK);
    }

}
