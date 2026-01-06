package com.hypercube.evisa.common.api.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class AgentTrackerDTO {

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
    private long closed;

    /**
     * 
     */
    private long arrival;

    /**
     * 
     */
    private long departure;

    /**
     * 
     */
    private BigDecimal average;

    /**
     * 
     */
    private String totalAverage;
    /**
     * 
     */
    private String username;

}
