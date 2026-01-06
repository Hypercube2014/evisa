package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ModuleDetailSearchResultsDTO {

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
    private String moduleDescOther;

    /**
     * 
     */
    private String active;

    /**
     * 
     */
    private int orderNo;

    /**
     * @param moduleId
     * @param moduleCode
     * @param moduleDesc
     * @param moduleDescOther
     * @param active
     * @param orderNo
     */
    public ModuleDetailSearchResultsDTO(Long moduleId, String moduleCode, String moduleDesc, String moduleDescOther,
            String active, int orderNo) {
        super();
        this.moduleId = moduleId;
        this.moduleCode = moduleCode;
        this.moduleDesc = moduleDesc;
        this.moduleDescOther = moduleDescOther;
        this.active = active;
        this.orderNo = orderNo;
    }

}
