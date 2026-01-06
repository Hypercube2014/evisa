package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class DashboardDTO {

    /**
     * 
     */
    private long totalAllocated;

    /**
     * 
     */
    private long pending;

    /**
     * 
     */
    private long approved;

    /**
     * 
     */
    private long closed;
    
    /**
     * 
     */
    private long rejected;
    
    /**
     * 
     */
    private long validation;
    
    /**
     * 
     */
    private String username;

}
