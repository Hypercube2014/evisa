/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class PreviewDTO {

    /**
     * 
     */
    private ApplicantPersonalDetails applicantPersonalDetails;

    /**
     * 
     */
    private ApplicantPassportTravelDetails applicantPassportTravelDetails;

    /**
     * 
     */
    private ApplicantAttachmentDetailsDTOList applicantAttachmentDetailsDTOList;

    /**
     * 
     */
    private ApplicationTracker applicationTracker;
    
    /**
     * 
     */
    private byte[] photograph;

    /**
     * @param applicantPersonalDetails
     * @param applicantPassportTravelDetails
     * @param applicantAttachmentDetailsDTOList
     */
    public PreviewDTO(ApplicantPersonalDetails applicantPersonalDetails,
            ApplicantPassportTravelDetails applicantPassportTravelDetails,
            ApplicantAttachmentDetailsDTOList applicantAttachmentDetailsDTOList) {
        super();
        this.applicantPersonalDetails = applicantPersonalDetails;
        this.applicantPassportTravelDetails = applicantPassportTravelDetails;
        this.applicantAttachmentDetailsDTOList = applicantAttachmentDetailsDTOList;
    }

    /**
     * @param applicantPersonalDetails
     * @param applicantPassportTravelDetails
     * @param applicationTracker
     */
    public PreviewDTO(ApplicantPersonalDetails applicantPersonalDetails,
            ApplicantPassportTravelDetails applicantPassportTravelDetails, ApplicationTracker applicationTracker) {
        super();
        this.applicantPersonalDetails = applicantPersonalDetails;
        this.applicantPassportTravelDetails = applicantPassportTravelDetails;
        this.applicationTracker = applicationTracker;
    }

}