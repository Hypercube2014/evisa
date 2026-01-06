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
public class MasterCodeList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6651900430436126503L;

    /**
     * 
     */
    private List<MasterCodeResultDTO> masterCodeResultDTOs;

    /**
     * @param masterCodeResultDTOs
     */
    public MasterCodeList(List<MasterCodeResultDTO> masterCodeResultDTOs) {
        super();
        this.masterCodeResultDTOs = masterCodeResultDTOs;
    }

}
