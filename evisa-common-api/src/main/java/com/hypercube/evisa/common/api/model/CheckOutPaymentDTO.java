package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class CheckOutPaymentDTO {

    /**
     * 
     */
    private String description;
    
    /**
     * 
     */
    private String referenceNumber;
    
    /**
     * 
     */
    private String currency;
    
    /**
     * 
     */
    private String successUrl;
    
    /**
     * 
     */
    private String cancelUrl;
    
    /**
     * 
     */
    private long amount;
    
    /**
     * 
     */
    private long quantity;
    
    /**
     * 
     */
    private String instrType;
    /**
     * 
     */
    private String username;
}
