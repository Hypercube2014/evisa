package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Getter
@Setter
public class ApplicantVisaExtensionFileUploadDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1345927659150616686L;

    /**
     * 
     */
    private Long fileId;

    /**
     * 
     */
    private String visaExtensionId;

    /**
     * 
     */
    private String fileName;

    /**
     * 
     */
    private Long fileSize;

    /**
     * @param fileId
     * @param visaExtensionId
     * @param fileName
     * @param fileSize
     */
    public ApplicantVisaExtensionFileUploadDTO(Long fileId, String visaExtensionId, String fileName, Long fileSize) {
        super();
        this.fileId = fileId;
        this.visaExtensionId = visaExtensionId;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

}
