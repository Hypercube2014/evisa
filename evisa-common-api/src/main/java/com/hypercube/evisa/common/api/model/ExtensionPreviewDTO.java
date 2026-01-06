package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ExtensionPreviewDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1662202435601997222L;

    /**
     * 
     */
    // private ApplicationHeader applicationHeader;

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
    private AttachmentUrlList applicationAttachmentDetailsList;

    /**
     * 
     */
    private ApplicantVisaExtension applicantVisaExtension;

    /**
     * @param applicationHeader
     * @param applicantPersonalDetails
     * @param applicantPassportTravelDetails
     * @param applicationAttachmentDetailsList
     * @param applicantVisaExtension
     */
    public ExtensionPreviewDTO(ApplicantPersonalDetails applicantPersonalDetails,
            ApplicantPassportTravelDetails applicantPassportTravelDetails,
            AttachmentUrlList applicationAttachmentDetailsList, ApplicantVisaExtension applicantVisaExtension) {
        super();
        // this.applicationHeader = applicationHeader;
        this.applicantPersonalDetails = applicantPersonalDetails;
        this.applicantPassportTravelDetails = applicantPassportTravelDetails;
        this.applicationAttachmentDetailsList = applicationAttachmentDetailsList;
        this.applicantVisaExtension = applicantVisaExtension;
    }

}
