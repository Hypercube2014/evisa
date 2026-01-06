/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplicantAttachmentDetailsDTOList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 696834348084585599L;
    /**
     * 
     */
    private List<ApplicantAttachmentDetailsDTO> applicantAttachmentDTOs;

    /**
     * @param applicantAttachmentDTOs
     */
    public ApplicantAttachmentDetailsDTOList(List<ApplicantAttachmentDetailsDTO> applicantAttachmentDTOs) {
        super();
        this.applicantAttachmentDTOs = applicantAttachmentDTOs;
    }

}
