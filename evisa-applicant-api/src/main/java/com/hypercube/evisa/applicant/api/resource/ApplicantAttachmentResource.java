/**
 * 
 */
package com.hypercube.evisa.applicant.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.applicant.api.model.AttachmentDTO;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTOList;
import com.hypercube.evisa.common.api.service.ApplicantAttachmentService;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;

import java.util.*;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ApplicantAttachmentResource {

        /**
         * 
         */
        @Autowired(required = true)
        private ApplicantAttachmentService applicantAttachmentService;
        @Autowired(required = true)
        private ApplicantPersonalDetailsService applicantPersonalDetailsService;

        private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg",
                        "application/pdf");

        /**
         * @param authorization
         * @param applicationNumber
         * @param passport
         * @param photograph
         * @param ticket
         * @param hotelInvitation
         * @return
         */
        @CrossOrigin
        @PostMapping(value = "/v1/api/applicantattachments", consumes = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<ApiResultDTO> applicantAttachments(
                        @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
                        @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
                        @RequestPart("AttachmentDTO") String attachmentDTO,
                        @RequestPart("passport") MultipartFile passport,
                        @RequestPart("photograph") MultipartFile photograph,
                        @RequestPart("ticket") MultipartFile ticket,
                        @RequestPart(value = "hotelInvitation", required = false) MultipartFile hotelInvitation,
                        @RequestPart(value = "transitBook", required = false) MultipartFile transitBook) {
                log.info("ApplicantAttachmentResource-applicantAttachments");
                ApiResultDTO apiResultDTO;

                if (passport.getContentType() == null || photograph.getContentType() == null
                                || ticket.getContentType() == null ||
                                hotelInvitation == null || hotelInvitation.getContentType() == null/*
                                                                                                    * ||
                                                                                                    * transitBook ==
                                                                                                    * null ||
                                                                                                    * transitBook.
                                                                                                    * getContentType()
                                                                                                    * == null
                                                                                                    */) {
                        apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
                        return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.BAD_REQUEST);
                }

                if (!isAllowedFileType(passport.getContentType()) ||
                                !isAllowedFileType(photograph.getContentType()) ||
                                !isAllowedFileType(ticket.getContentType()) ||
                                (hotelInvitation != null && !isAllowedFileType(hotelInvitation.getContentType())) ||
                                (transitBook != null && !isAllowedFileType(transitBook.getContentType()))) {
                        apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
                        return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.BAD_REQUEST);
                }

                try {
                        AttachmentDTO attDTO = new ObjectMapper().readValue(attachmentDTO, AttachmentDTO.class);

                        String fileNumber = applicantPersonalDetailsService
                                        .findFileNumber(attDTO.getApplicationNumber());
                        String username = applicantPersonalDetailsService.findUsername(fileNumber);
                        ApplicantUtil applicantUtil = new ApplicantUtil();
                        if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
                                log.info("User is authenticated and authorized to create a draft application file.");
                        } else {
                                log.error("User is not authorized to create a draft application file.");
                                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                        }

                        apiResultDTO = applicantAttachmentService.applicantAttachments(locale,
                                        attDTO.getApplicationNumber(),
                                        passport, photograph, ticket, hotelInvitation, transitBook);

                } catch (JsonProcessingException e) {
                        apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
                }
                return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.OK);
        }

        /**
         * @param authorization
         * @param applicationNumber
         * @param attachmentType
         * @param file
         * @return
         */
        @CrossOrigin
        @PutMapping(value = "/v1/api/applicantattachment", consumes = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<ApiResultDTO> applicantAttachmentsByAttachmentId(
                        @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
                        @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
                        @RequestPart("AttachmentDTO") String attachmentDTO, @RequestPart("file") MultipartFile file) {
                log.info("ApplicantAttachmentResource-applicantAttachmentsByAttachmentId");
                ApiResultDTO apiResultDTO;

                try {
                        if (file.getContentType() == null) {
                                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                                LocaleConfig.getResourceValue("error.processing", null, locale, null));
                                return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.BAD_REQUEST);
                        }

                        if (!isAllowedFileType(file.getContentType())) {
                                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                                LocaleConfig.getResourceValue("error.processing", null, locale, null));
                                return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.BAD_REQUEST);
                        }

                        AttachmentDTO attDTO = new ObjectMapper().readValue(attachmentDTO, AttachmentDTO.class);
                        
                        ApplicantAttachmentDetails applicant = applicantAttachmentService
                                        .findAttachmentById(attDTO.getAttachmentId());
                        String fileNumber = applicantPersonalDetailsService
                                        .findFileNumber(applicant.getApplicationNumber());
                        String username = applicantPersonalDetailsService.findUsername(fileNumber);
                        ApplicantUtil applicantUtil = new ApplicantUtil();
                        if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
                                log.info("User is authenticated and authorized to create a draft application file.");
                        } else {
                                log.error("User is not authorized to create a draft application file.");
                                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                        }
                        apiResultDTO = applicantAttachmentService.applicantAttachmentsByAttachmentId(locale,
                                        attDTO.getAttachmentId(), attDTO.getApplicationNumber(),
                                        attDTO.getAttachmentType(), file);

                } catch (JsonProcessingException e) {
                        apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
                }
                return new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.OK);
        }

        private boolean isAllowedFileType(String contentType) {
                return ALLOWED_FILE_TYPES.contains(contentType);
        }

        /**
         * @param authorization
         * @param applicationNumber
         * @return
         */
        @CrossOrigin
        @PostMapping(value = "/v1/api/applicantattachments/applicantnumber")
        public ResponseEntity<ApplicantAttachmentDetailsDTOList> applicantAttachmentsByApplicationNumber(
                        @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
                        @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
                        @RequestBody AttachmentDTO attachmentDTO) {
                log.info("ApplicantAttachmentResource-applicantAttachmentsByApplicationNumber");

                String fileNumber = applicantPersonalDetailsService
                                .findFileNumber(attachmentDTO.getApplicationNumber());
                String username = applicantPersonalDetailsService.findUsername(fileNumber);
                ApplicantUtil applicantUtil = new ApplicantUtil();
                if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
                        log.info("User is authenticated and authorized to create a draft application file.");
                } else {
                        log.error("User is not authorized to create a draft application file.");
                        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                return new ResponseEntity<>(
                                applicantAttachmentService
                                                .attachmentsByApplicationNumber(attachmentDTO.getApplicationNumber()),
                                HttpStatus.OK);
        }

        /**
         * @param authorization
         * @param attachmentId
         * @return
         */
        @CrossOrigin
        @PostMapping(value = "/v1/api/applicantattachments/attachmentid")
        public ResponseEntity<Resource> getAttachmentFile(
                        @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
                        @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
                        @RequestBody AttachmentDTO attachmentDTO) {
                log.info("ApplicantAttachmentResource-getAttachmentFile");

                ApplicantAttachmentDetails applicationAttachmentDetails = applicantAttachmentService
                                .getAttachmentFile(attachmentDTO.getAttachmentId());

                ApplicantAttachmentDetails applicant = applicantAttachmentService
                                .findAttachmentById(attachmentDTO.getAttachmentId());
                String fileNumber = applicantPersonalDetailsService
                                .findFileNumber(applicant.getApplicationNumber());
                String username = applicantPersonalDetailsService.findUsername(fileNumber);
                ApplicantUtil applicantUtil = new ApplicantUtil();
                if (applicantUtil.isRequestUserSameAsAuthenticated(username)) {
                        log.info("User is authenticated and authorized to create a draft application file.");
                } else {
                        log.error("User is not authorized to create a draft application file.");
                        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(applicationAttachmentDetails.getFileType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + applicationAttachmentDetails.getFileName()
                                                                + "\"")
                                .body(new ByteArrayResource(applicationAttachmentDetails.getFileData()));
        }

}
