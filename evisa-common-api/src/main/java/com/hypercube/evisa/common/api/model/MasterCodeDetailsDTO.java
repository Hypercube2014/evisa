package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MasterCodeDetailsDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6598557548478437095L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String descriptionOther;

    /**
     * 
     */
    private String codeType;

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
