package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ModuleDetailsSearchDTO {

    /**
     * 
     */
    private String moduleCode;

    /**
     * 
     */
    private String active;

    /**
     * 
     */
    private String moduleDesc;

    /**
     * 
     */
    private int pageSize;

    /**
     * 
     */
    private int pageNumber;

}
