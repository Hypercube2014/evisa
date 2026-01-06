package com.hypercube.evisa.common.api.model;

import java.util.List;

import com.hypercube.evisa.common.api.model.AttachmentPreviewDTO;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class AttachmentUrlList {

    /**
     * 
     */
    List<AttachmentPreviewDTO> attchPreviewDTOList;

    /**
     * @param attchPreviewDTOList
     */
    public AttachmentUrlList(List<AttachmentPreviewDTO> attchPreviewDTOList) {
        super();
        this.attchPreviewDTOList = attchPreviewDTOList;
    }

}
