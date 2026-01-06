package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SivaSreenivas
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VisaExtensionSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -6059558631508983730L;
    
    /**
     * 
     */
    private String applicationNumber;
    
    /**
     * 
     */
    private String fileNumber;
    
    /**
     * 
     */
    private String extensionId;
    
    /**
     * 
     */
    private String documentStatus;
    
    /**
     * 
     */
    private String extensionStatus;

}
