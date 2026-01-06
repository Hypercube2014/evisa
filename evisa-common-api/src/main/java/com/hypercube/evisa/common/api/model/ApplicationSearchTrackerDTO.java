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
public class ApplicationSearchTrackerDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -5375669001567340307L;

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
    private String givenName;

    /**
     * 
     */
    private String middleName;

    /**
     * 
     */
    private String surname;

    /**
     * 
     */
    private String emailAddress;

    /**
     * 
     */
    private String docStatus;

    /**
     * 
     */
    private String visaStatus;
    
    /**
     * 
     */
    private String passportNo;
    
    /**
     * 
     */
    private String expressVisa;
    
    /**
     * 
     */
    private String applicantType;
    
    /**
     * 
     */
    private String arrDepIndicator;
    

}
