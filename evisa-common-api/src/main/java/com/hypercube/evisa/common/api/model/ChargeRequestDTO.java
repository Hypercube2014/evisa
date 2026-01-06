package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ChargeRequestDTO {

    /**
     * 
     */
    public enum Currency {
        EUR, DJF, USD;
    }

    /**
     * 
     */
    private String description;
    
    /**
     * 
     */
    private int amount;
    
    /**
     * 
     */
    private Currency currency;
    
    /**
     * 
     */
    private String stripeEmail;
    
    /**
     * 
     */
    private String stripeToken;
    
    /**
     * 
     */
    private String referenceNumber;
    
    /**
     * 
     */
    private String recieptMail;

}
