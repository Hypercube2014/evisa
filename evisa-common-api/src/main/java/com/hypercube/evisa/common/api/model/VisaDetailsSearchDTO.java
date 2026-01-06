/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class VisaDetailsSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -6764452074659022679L;

    /**
     * 
     */
    private String visaType;

    /**
     * 
     */
    private String visaDescription;

    /**
     * 
     */
    private String isExpressVisaAllowed;
    
    /**
     * 
     */
    private String status;

}
