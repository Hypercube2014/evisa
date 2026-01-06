/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ApiResultDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5157790348047942563L;

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
    private Long id;

    /**
     * 
     */
    private String applicationNumber;

    /**
     * 
     */
    private String amountDetails;

    /**
     * 
     */
    List<String> errorDetails;

    /**
     * @param apiStatusCode
     * @param apiStatusDesc
     */
    public ApiResultDTO(String apiStatusCode, String apiStatusDesc) {
        super();
        this.apiStatusCode = apiStatusCode;
        this.apiStatusDesc = apiStatusDesc;
    }

    /**
     * @param apiStatusCode
     * @param apiStatusDesc
     * @param applicationNumber
     */
    public ApiResultDTO(String apiStatusCode, String apiStatusDesc, String applicationNumber) {
        super();
        this.apiStatusCode = apiStatusCode;
        this.apiStatusDesc = apiStatusDesc;
        this.applicationNumber = applicationNumber;
    }

    /**
     * @param apiStatusCode
     * @param apiStatusDesc
     * @param errorDetails
     */
    public ApiResultDTO(String apiStatusCode, String apiStatusDesc, List<String> errorDetails) {
        super();
        this.apiStatusCode = apiStatusCode;
        this.apiStatusDesc = apiStatusDesc;
        this.errorDetails = errorDetails;
    }

    /**
     * @param apiStatusCode
     * @param apiStatusDesc
     * @param applicationNumber
     * @param amountDetails
     */
    public ApiResultDTO(String apiStatusCode, String apiStatusDesc, String applicationNumber, String amountDetails) {
        super();
        this.apiStatusCode = apiStatusCode;
        this.apiStatusDesc = apiStatusDesc;
        this.applicationNumber = applicationNumber;
        this.amountDetails = amountDetails;
    }

}
