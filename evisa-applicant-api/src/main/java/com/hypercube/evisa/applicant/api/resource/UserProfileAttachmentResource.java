/**
 * 
 */
package com.hypercube.evisa.applicant.api.resource;

import java.io.IOException;

import java.io.Serializable;

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

import com.hypercube.evisa.applicant.api.service.ApplicantAuthenticationServiceFacade;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.UserProfileAttachment;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.service.UserProfileAttachmentService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class UserProfileAttachmentResource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1184931457790566987L;

    /**
     * 
     */
    @Autowired(required = true)
    UserProfileAttachmentService userProfileAttachmentService;

    @Autowired(required = true)
    ApplicantAuthenticationServiceFacade applicantAuthenticationServiceFacade;

    /**
     * @param authorization
     * @param locale
     * @param username
     * @param profilepic
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/profileattachment", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResultDTO> profileAttachment(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @RequestPart("username") String username,
            @RequestPart("profilepic") MultipartFile profilepic) {
        log.info("UserProfileAttachmentResource-profileAttachment");
        ApiResultDTO apiResultDTO;
        /*if (applicantAuthenticationServiceFacade.isAuthorized(username)) {
            try {
                apiResultDTO = userProfileAttachmentService.saveUserProfileAttachment(locale,
                        new UserProfileAttachment(username, profilepic.getContentType(), profilepic.getBytes()));
            } catch (IOException e) {
                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
            }

            return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<ApiResultDTO>(HttpStatus.FORBIDDEN);
        }*/

        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(username)) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            apiResultDTO = userProfileAttachmentService.saveUserProfileAttachment(locale,
                    new UserProfileAttachment(username, profilepic.getContentType(), profilepic.getBytes()));
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
    @GetMapping(value = "/v1/api/removeprofilepic/{username}")
    public ResponseEntity<ApiResultDTO> removeProfileAttachment(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("username") String username) {
        log.info("UserProfileAttachmentResource-removeProfileAttachment");

        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(username)) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        /*if (applicantAuthenticationServiceFacade.isAuthorized(username)) {
            return new ResponseEntity<>(userProfileAttachmentService.removeProfilePic(username), HttpStatus.OK);
        } else {
            return new ResponseEntity<ApiResultDTO>(HttpStatus.FORBIDDEN);
        }*/

        return new ResponseEntity<>(userProfileAttachmentService.removeProfilePic(username), HttpStatus.OK);
    }

}
