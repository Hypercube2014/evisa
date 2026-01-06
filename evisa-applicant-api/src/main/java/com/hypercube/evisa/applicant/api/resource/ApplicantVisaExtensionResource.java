package com.hypercube.evisa.applicant.api.resource;

import java.io.IOException;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionFileUploadService;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionServiceFacade;
import com.hypercube.evisa.applicant.api.service.ApplicantAuthenticationServiceFacade;
import com.hypercube.evisa.common.api.constants.CommonsConstants;

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

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVisaExtensionFileUploadService appVisaExtensionUploadService;

    @Autowired(required = true)
    ApplicantAuthenticationServiceFacade applicantAuthenticationServiceFacade;

    /**
     * @param request
     * @param locale
     * @param authorization
     * @param applicantVisaExtension
     * @param file
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/applyvisaextension")
    public ResponseEntity<ChargeResponseDTO> applyVisaExtension(HttpServletRequest request,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestPart("ApplicantVisaExtension") String applicantVisaExtension,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        log.info("ApplicantVisaExtensionResource-applyVisaExtension {}", applicantVisaExtension);
        ChargeResponseDTO chargeResponseDTO;

        try {
            chargeResponseDTO = applicantVisaExtensionServiceFacade.applyVisaExtension(request,
                    new ObjectMapper().readValue(applicantVisaExtension, ApplicantVisaExtension.class), file);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            log.error("applyVisaExtension-JsonProcessingException -- {}", jpe);
            chargeResponseDTO = new ChargeResponseDTO("ERROR", "Internal Error While Processing, Contact ADMIN", null);
        } catch (IOException ioe) {
            log.error("applyVisaExtension-IOException -- {}", ioe.getCause());
            chargeResponseDTO = new ChargeResponseDTO("ERROR", "Internal Error While Processing, Contact ADMIN", null);
        }
        return new ResponseEntity<>(chargeResponseDTO, HttpStatus.OK);
    }

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
     * @param visaExtensionCheckSearchDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/searchavailableextensions")
    public ResponseEntity<Page<VisaExtensionCheck>> searchVisaExtensionCheck(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody VisaExtensionSearchDTO visaExtensionCheckSearchDTO) {
        log.info("ApplicantVisaExtensionResource-applicantVisaExtensionServiceFacade");

        return new ResponseEntity<>(
                applicantVisaExtensionServiceFacade.searchVisaExtensionCheck(visaExtensionCheckSearchDTO),
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
                log.info("--------------------------------------------- search applicant visa extension --------------------------------");
        log.info("ApplicantVisaExtensionResource-searchApplicantVisaExtension");
        if (applicantAuthenticationServiceFacade.isAuthorized(visaExtensionSearchDTO.getLoggedUser())) {
            return new ResponseEntity<>(
                applicantVisaExtensionServiceFacade.searchApplicantVisaExtension(visaExtensionSearchDTO),
                HttpStatus.OK);
        }else{
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      
    }

//    /**
//     * @param authorization
//     * @param fileId
//     * @return
//     */
//    @CrossOrigin
//    @GetMapping(value = "/v1/api/visaextension/fileupload/{fileid}")
//    public ResponseEntity<Resource> getExtensionUploadFile(
//            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization, @PathVariable("fileid") Long fileId) {
//
//        ApplicantVisaExtensionFileUpload fileUpload = appVisaExtensionUploadService.findByFileId(fileId);
//
//        if (fileUpload == null) {
//            return null;
//        } else {
//
//            try {
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
