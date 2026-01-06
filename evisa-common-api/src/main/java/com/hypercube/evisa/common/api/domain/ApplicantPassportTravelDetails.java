package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_APP_PASSPORT_TRAVEL")
public class ApplicantPassportTravelDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8722070268223903473L;

    /**
     * 
     */
    @Id
    @Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "PASSPORT_NO", length = 25, nullable = false)
    private String passportNumber;

    /**
     * 
     */
    @Column(name = "ISSUED_COUNTRY", length = 3, nullable = false)
    private String issuedCountry;

    /**
     * 
     */
    @Column(name = "ISSUED_DATE", nullable = false)
    private LocalDate issuedDate;

    /**
     * 
     */
    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDate expiryDate;

    /**
     * 
     */
    @Column(name = "TRAVEL_PURPOSE", length = 10, nullable = false)
    private String travelPurpose;

    /**
     * 
     */
    @Column(name = "ARRIVAL_DATE", nullable = false)
    private LocalDate arrivalDate;

    /**
     * 
     */
    @Column(name = "DEPARTURE_DATE", nullable = true)
    private LocalDate departureDate;

    /**
     * 
     */
    @Column(name = "DJIBOUTI_ADDRESS", length = 255, nullable = true)
    private String djiboutiAddress;

    /**
     * 
     */
    @Column(name = "ARRIVAL_LOCATION", length = 25, nullable = false)
    private String arrivalLocation;

    /**
     * 
     */
    @Column(name = "CARRIER", length = 10, nullable = true)
    private String carrier;

    /**
     * 
     */
    @Column(name = "FLIGHT_NO", length = 10, nullable = true)
    private String flightNumber;

    /**
     * 
     */
    @Column(name = "VISIT_DURATION", length = 3, nullable = true)
    private int visitDuration;

    /**
     * 
     */
    @Column(name = "LAST_VISITED_COUNTRIES", length = 40, nullable = true)
    private String lastVisitedCountries;
    
    /**
     * 
     */
    @Column(name = "ADDITIONAL_INFORMATION", length = 512, nullable = true)
    private String additionalInformation;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", insertable = false, updatable = true)
    private Date updatedDate;

}
