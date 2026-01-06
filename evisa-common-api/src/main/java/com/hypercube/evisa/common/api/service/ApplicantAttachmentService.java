package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTOList;
import com.hypercube.evisa.common.api.model.GenericGroupCountDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantAttachmentService {

    /**
     * @param locale
     * @param applicationNumber
     * @param passport
     * @param photograph
     * @param ticket
     * @param hotelInvitation
     * @return
     */
    ApiResultDTO applicantAttachments(String locale, String applicationNumber, MultipartFile passport,
            MultipartFile photograph, MultipartFile ticket, MultipartFile hotelInvitation,MultipartFile transitBook );

    /**
     * @param applicationNumber
     * @return
     */
    ApplicantAttachmentDetailsDTOList attachmentsByApplicationNumber(String applicationNumber);

    /**
     * @param attachmentId
     * @return
     */
    ApplicantAttachmentDetails getAttachmentFile(Long attachmentId);

    /**
     * @param locale
     * @param attachmentId
     * @param applicationNumber
     * @param attachmentType
     * @param file
     * @return
     */
    ApiResultDTO applicantAttachmentsByAttachmentId(String locale, Long attachmentId, String applicationNumber,
            String attachmentType, MultipartFile file);

    /**
     * @param applicationList
     * @return
     */
    List<GenericGroupCountDTO> findApplications(List<String> applicationList);

    /**
     * @param id
     * @return
     */
    ApplicantAttachmentDetails findAttachmentById(Long id);

    /**
     * @param applicationNumber
     * @param attachType
     * @return
     */
    List<ApplicantAttachmentDetails> findAttchDtlsByAppNoAndAttchType(String applicationNumber, String attachType);

}
