package com.hypercube.evisa.common.api.model;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SivaSreenivas
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicantTravelHistorySearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 3291928664752438883L;
    
    /**
     * 
     */
    private String applicationNumber;

    /**
     * 
     */
    private String surname;
    
    /**
     * 
     */
    private String givenName;

    /**
     * 
     */
    private String gender;

    /**
     * 
     */
    private LocalDate dob;
    
    /**
     * 
     */
    private String birthCountry;
    
    /**
     * 
     */
    private Date submittedDate;

}
