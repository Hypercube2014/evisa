package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ModuleDetailsDTO {

    /**
     * 
     */
    private Long moduleId;

    /**
     * 
     */
    private String moduleCode;

    /**
     * 
     */
    private String moduleDesc;

    /**
     * 
     */
    private String active;

    /**
     * 
     */
    private String createdBy;

    /**
     * 
     */
    private String updatedBy;

}
