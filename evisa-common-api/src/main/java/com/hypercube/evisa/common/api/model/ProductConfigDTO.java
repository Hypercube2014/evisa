package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ProductConfigDTO {

    /**
     * 
     */
    private Long configId;

    /**
     * 
     */
    private String configCode;

    /**
     * 
     */
    private String configValue;

    /**
     * 
     */
    private String configDesc;

    /**
     * 
     */
    private String system;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String createdBy;

    /**
     * 
     */
    private String updatedBy;

}
