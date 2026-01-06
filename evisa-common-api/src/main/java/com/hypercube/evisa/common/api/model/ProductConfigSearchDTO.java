package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProductConfigSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -9052034406730965996L;
    
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

}
