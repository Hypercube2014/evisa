/**
 * 
 */
package com.hypercube.evisa.applicant.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ApplicationFileDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1496341196029091026L;
    
    /**
     * 
     */
    private String fileNumber;
    
    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private boolean isExpressVisa;

    /**
     * 
     */
    private int totalGroupApplications;

    /**
     * 
     */
    private String applicantType;

    /**
     * 
     */
    private String visaType;
    
    /**
     * 
     */
    private String status;

}
