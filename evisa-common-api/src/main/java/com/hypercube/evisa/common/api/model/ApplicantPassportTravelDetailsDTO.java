/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ApplicantPassportTravelDetailsDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7138136371506668154L;

    /**
     * 
     */
    private String applicationNumber;

    /**
     * 
     */
    private String passportNumber;

    /**
     * 
     */
    private String issuedCountry;

    /**
     * 
     */
    private LocalDate issuedDate;

    /**
     * 
     */
    private LocalDate expiryDate;

    /**
     * 
     */
    private String travelPurpose;

    /**
     * 
     */
    private LocalDate arrivalDate;

    /**
     * 
     */
    private LocalDate departureDate;

    /**
     * 
     */
    private String djiboutiAddress;

    /**
     * 
     */
    private String arrivalLocation;

    /**
     * 
     */
    private String carrier;

    /**
     * 
     */
    private String flightNumber;

    /**
     * 
     */
    private int visitDuration;

    /**
     * 
     */
    private String lastVisitedCountries;

    /**
     * 
     */
    private String additionalInformation;

}
