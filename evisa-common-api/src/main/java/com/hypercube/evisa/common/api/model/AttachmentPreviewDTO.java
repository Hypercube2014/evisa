package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class AttachmentPreviewDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -663488113606588046L;

    /**
     * 
     */
    private String docType;

    /**
     * 
     */
    private String attachmentUrl;

    /**
     * @param docType
     * @param attachmentUrl
     */
    public AttachmentPreviewDTO(String docType, String attachmentUrl) {
        super();
        this.docType = docType;
        this.attachmentUrl = attachmentUrl;
    }

}
