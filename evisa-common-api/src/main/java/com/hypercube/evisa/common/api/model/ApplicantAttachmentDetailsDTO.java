package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
public class ApplicantAttachmentDetailsDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 407689739159554951L;

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

    /**
     * 
     */
    private String fileName;

    /**
     * 
     */
    private String fileType;

    /**
     * 
     */
    private long fileSize;

    /**
     * @param attachmentId
     * @param applicationNumber
     * @param attachmentType
     * @param fileName
     * @param fileType
     * @param fileSize
     */
    public ApplicantAttachmentDetailsDTO(Long attachmentId, String applicationNumber, String attachmentType,
            String fileName, String fileType, long fileSize) {
        super();
        this.attachmentId = attachmentId;
        this.applicationNumber = applicationNumber;
        this.attachmentType = attachmentType;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

}
