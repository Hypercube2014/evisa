package com.hypercube.evisa.applicant.api.model;

import java.io.Serializable;

import com.hypercube.evisa.common.api.domain.ApplicationTracker;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class CheckVisaResultDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1280094979109755064L;

    /**
     * 
     */
    private String apiStatusCode;

    /**
     * 
     */
    private String apiStatusDesc;

    /**
     * 
     */
    private ApplicationTracker applicationTracker;

    /**
     * 
     */
    private String applicationResult;

    /**
     * 
     */
    private String approverRemarks;

    /**
     * @param apiStatusCode
     * @param apiStatusDesc
     */
    public CheckVisaResultDTO(String apiStatusCode, String apiStatusDesc) {
        super();
        this.apiStatusCode = apiStatusCode;
        this.apiStatusDesc = apiStatusDesc;
    }

}
