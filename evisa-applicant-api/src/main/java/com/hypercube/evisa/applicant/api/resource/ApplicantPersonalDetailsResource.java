/**
 * 
 */
package com.hypercube.evisa.applicant.api.resource;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsDTO;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsSearchDTO;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.common.api.service.VisaCheckOverstayService;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;


/**
 * @author Ayanleh abdousalam
 *
 */
@RestController
@Data
@Slf4j
public class ApplicantPersonalDetailsResource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2518283064400972775L;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantPersonalDetailsService applicantPersonalDetailsService;
    
    @Autowired(required = true)
    private VisaCheckOverstayService visacheckOverstayService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param authorization
     * @param locale
     * @param applicantPersonalDetailsDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/applicantpersonalinfo")
    public ResponseEntity<ApiResultDTO> createApplicantPersonalDetails(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody ApplicantPersonalDetailsDTO applicantPersonalDetailsDTO) {
        log.info("ApplicantPersonalDetailsResource-createApplicantPersonalDetails");
        System.out.println(applicantPersonalDetailsDTO.toString());
        
        String username;
        if (applicantPersonalDetailsDTO.getApplicationNumber() != null) {
            String fileNumber1 = applicantPersonalDetailsService.findFileNumber(applicantPersonalDetailsDTO.getApplicationNumber());
            String fileNumber2 = applicantPersonalDetailsDTO.getFileNumber();
            if (fileNumber1 == null && fileNumber2 == null || fileNumber1.isEmpty() && fileNumber2.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                if (fileNumber1.equals(fileNumber2)) {
                    username = applicantPersonalDetailsService.findUsername(fileNumber1);
                } else {
                    log.info("ApplicantPersonalDetailsResource-createApplicantPersonalDetails NOT FOUND 1" + fileNumber1 + " " + fileNumber2);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        } else {
            String fileNumber = applicantPersonalDetailsDTO.getFileNumber();
            if (fileNumber == null || fileNumber.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            username = applicantPersonalDetailsService.findUsername(fileNumber);
            if (username == null || username.isEmpty()) {
                log.info("ApplicantPersonalDetailsResource-createApplicantPersonalDetails NOT FOUND 2");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(username)) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(
                applicantPersonalDetailsService.saveApplicantPersonalDetails(locale, applicantPersonalDetailsDTO),
                HttpStatus.OK);
    }

    /**
     * @param locale
     * @param authorization
     * @param applicationnumber
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/applicantpersonalinfo/{applicationnumber}")
    public ResponseEntity<ApplicantPersonalDetails> findApplicantionNumber(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @PathVariable("applicationnumber") String applicationnumber) {
        log.info("ApplicantPersonalDetailsResource-findApplicantionNumber");

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

        return new ResponseEntity<>(applicantPersonalDetailsService.findByApplicationNumber(applicationnumber),
                HttpStatus.OK);
    }

    /**
     * @param authorization
     * @param applicantPersonalDetailsSearchDTO
     * @return
     */
    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/v1/api/seachapplications")
    public Page<ApplicantPersonalDetailsSearchDTO> searchApplications(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody ApplicantPersonalDetailsSearchDTO applicantPersonalDetailsSearchDTO) {
        log.info("ApplicantPersonalDetailsResource-searchApplications");

        String username;
        if (applicantPersonalDetailsSearchDTO.getApplicationNumber() != null) {
            String fileNumber1 = applicantPersonalDetailsService.findFileNumber(applicantPersonalDetailsSearchDTO.getApplicationNumber());
            String fileNumber2 = applicantPersonalDetailsSearchDTO.getFileNumber();
            if (fileNumber1 == null && fileNumber2 == null || fileNumber1.isEmpty() && fileNumber2.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                if (fileNumber1.equals(fileNumber2)) {
                    username = applicantPersonalDetailsService.findUsername(fileNumber1);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        } else {
            String fileNumber = applicantPersonalDetailsSearchDTO.getFileNumber();
            if (fileNumber == null || fileNumber.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            username = applicantPersonalDetailsService.findUsername(fileNumber);
            if (username == null || username.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(username)) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return applicantPersonalDetailsService.searchApplications(applicantPersonalDetailsSearchDTO);
    }

    public static class SqlRequest {
        private String sql;
        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }
}
