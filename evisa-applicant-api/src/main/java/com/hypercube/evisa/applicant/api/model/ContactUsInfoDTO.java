package com.hypercube.evisa.applicant.api.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author newrta
 *
 */
@Data
public class ContactUsInfoDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5507377427370716118L;

    /**
     * 
     */
    private String subject;
    
    /**
     * 
     */
    private String contactName;
    
    /**
     * 
     */
    private String contactEmail;

    /**
     * 
     */
    private String contactComment;

}
