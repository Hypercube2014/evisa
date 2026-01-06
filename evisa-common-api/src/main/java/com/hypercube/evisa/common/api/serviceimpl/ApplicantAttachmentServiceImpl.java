/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.customsrepoimpl.ApplicantTravelHistoryCustomsRepoImpl;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTOList;
import com.hypercube.evisa.common.api.model.GenericGroupCountDTO;
import com.hypercube.evisa.common.api.repository.ApplicantAttachmentRepository;
import com.hypercube.evisa.common.api.service.ApplicantAttachmentService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Service
@Slf4j
public class ApplicantAttachmentServiceImpl implements ApplicantAttachmentService {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantAttachmentRepository applicantAttachmentRepository;
    

    /**
     * 
     */
    @Override
    public ApiResultDTO applicantAttachments(String locale, String applicationNumber, MultipartFile passport,
            MultipartFile photograph, MultipartFile ticket, MultipartFile hotelInvitation, MultipartFile transitBook ) {
        log.info("ApplicantAttachmentServiceImpl-applicantAttachments");

        List<ApplicantAttachmentDetails> attachList = new ArrayList<ApplicantAttachmentDetails>();
        ApiResultDTO apiResultDTO;
        try {
            attachList.add(new ApplicantAttachmentDetails(null, applicationNumber, "PP",
                    StringUtils.cleanPath(passport.getOriginalFilename()), passport.getContentType(),
                    passport.getBytes(), passport.getBytes().length / 1024));

            attachList.add(new ApplicantAttachmentDetails(null, applicationNumber, "PG",
                    StringUtils.cleanPath(photograph.getOriginalFilename()), photograph.getContentType(),
                    photograph.getBytes(), photograph.getBytes().length / 1024));

            attachList.add(new ApplicantAttachmentDetails(null, applicationNumber, "TK",
                    StringUtils.cleanPath(ticket.getOriginalFilename()), ticket.getContentType(), ticket.getBytes(),
                    ticket.getBytes().length / 1024));

            if (hotelInvitation != null) {
                attachList.add(new ApplicantAttachmentDetails(null, applicationNumber, "HI",
                        StringUtils.cleanPath(hotelInvitation.getOriginalFilename()), hotelInvitation.getContentType(),
                        hotelInvitation.getBytes(), hotelInvitation.getBytes().length / 1024));
            }
            

            if (transitBook != null) {
                attachList.add(new ApplicantAttachmentDetails(null, applicationNumber, "TS",
                        StringUtils.cleanPath(transitBook.getOriginalFilename()), transitBook.getContentType(),
                        transitBook.getBytes(), transitBook.getBytes().length / 1024));
            }


            applicantAttachmentRepository.saveAll(attachList);
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
        } catch (IOException exe) {
            log.error("applicantAttachments-IOException {}", exe);
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.processing", null, locale, null));
        }

        return apiResultDTO;
    }

    /**
     * 
     */
    @Override
    public ApplicantAttachmentDetailsDTOList attachmentsByApplicationNumber(String applicationNumber) {
        log.info("ApplicantAttachmentServiceImpl-attachmentsByApplicationNumber");
        return new ApplicantAttachmentDetailsDTOList(
                applicantAttachmentRepository.findByApplicationNumber(applicationNumber));
    }

    /**
     * 
     */
    @Override
    public ApplicantAttachmentDetails getAttachmentFile(Long attachmentId) {
        log.info("ApplicantAttachmentServiceImpl-applicantAttachments");
        return applicantAttachmentRepository.findByAttachmentId(attachmentId);
    }

    /**
     * 
     */
    @Override
    public ApiResultDTO applicantAttachmentsByAttachmentId(String locale, Long attachmentId, String applicationNumber,
            String attachmentType, MultipartFile file) {
        log.info("ApplicantAttachmentServiceImpl-applicantAttachmentsByAttachmentId");
        ApiResultDTO apiResultDTO;
        try {
            applicantAttachmentRepository.save(new ApplicantAttachmentDetails(attachmentId, applicationNumber,
                    attachmentType, StringUtils.cleanPath(file.getOriginalFilename()), file.getContentType(),
                    file.getBytes(), file.getBytes().length / 1024));
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
        } catch (IOException exe) {
            log.error("applicantAttachments-IOException {}", exe);
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.processing", null, locale, null));
        }

        return apiResultDTO;

    }

    /**
     * 
     */
    @Override
    public List<GenericGroupCountDTO> findApplications(List<String> applicationList) {
        log.info("ApplicantAttachmentServiceImpl-findApplications");
        return applicantAttachmentRepository.findApplications(applicationList);
    }

    /**
     * 
     */
    @Override
    public ApplicantAttachmentDetails findAttachmentById(Long id) {
        log.info("ApplicantAttachmentServiceImpl-findAttachmentById");
        return applicantAttachmentRepository.findByAttachmentId(id);
    }

    /**
     *
     */
    @Override
    public List<ApplicantAttachmentDetails> findAttchDtlsByAppNoAndAttchType(String applicationNumber, String attachType) {
        log.info("ApplicantAttachmentServiceImpl-findAttchDtlsByAppNoAndAttchType");
        return applicantAttachmentRepository.findAttchDtlsByAppNoAndAttchType(applicationNumber, attachType);
    }

}
