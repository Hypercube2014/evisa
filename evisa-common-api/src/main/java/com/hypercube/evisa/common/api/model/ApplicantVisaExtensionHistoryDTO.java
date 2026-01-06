package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ApplicantVisaExtensionHistoryDTO {

    /**
     * 
     */
    private Long visaExtensionHisId;

    /**
     * 
     */
    private String visaExtensionId;

    /**
     * 
     */
    private String actionBy;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String remarks;

    /**
     * 
     */
    private String role;

}
