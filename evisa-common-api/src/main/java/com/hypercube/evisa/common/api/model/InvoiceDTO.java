package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class InvoiceDTO {

    /**
     * 
     */
    private String statusCode;

   
    /**
     * 
     */
    private String statusDesc;

    /**
     * 
     */
    private String receiptNumber;

    /**
     * 
     */
    private long amountPaid;
    
    /**
     * 
     */
    private String currency;
    
    /**
     * 
     */
    private String paymentMethod;
    
    /**
     * 
     */
    private String datePaid;

}
