package com.hypercube.evisa.common.api.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SivaSreenivas
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PaymentSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 232379717712165950L;
    
    /**
     * 
     */
    private String fileNumber;
    
    /**
     * 
     */
    private String status;
    
    /**
     * 
     */
    private String instrType;
    
    
    private Date date=new Date();

}
