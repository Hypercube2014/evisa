package com.hypercube.evisa.applicant.api.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class AttachmentDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3593545186825417210L;

    /**
     * 
     */
    private Long attachmentId;

    /**
     * 
     */
    private String applicationNumber;

    /**
     * 
     */
    private String attachmentType;

}
