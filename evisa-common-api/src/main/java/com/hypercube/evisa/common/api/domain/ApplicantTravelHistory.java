package com.hypercube.evisa.common.api.domain;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "vevi_travel_history")
public class ApplicantTravelHistory {
    
    /**
     * 
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 
     */
    @Column(name = "application_number")
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "surname")
    private String surname;
    
    /**
     * 
     */
    @Column(name = "given_name")
    private String givenName;

    /**
     * 
     */
    @Column(name = "gender")
    private String gender;

    /**
     * 
     */
    @Column(name = "dob")
    private LocalDate dob;

    /**
     * 
     */
    @Column(name = "birth_country")
    private String birthCountry;

    /**
     * 
     */
    @Column(name = "passport_no")
    private String passportNumber;
    
    /**
     * 
     */
    @Column(name = "submitted_date")
    private Date submittedDate;

    /**
     * 
     */
    @Column(name = "arrived_date")
    private LocalDate arrivedDate;

    /**
     * 
     */
    @Column(name = "departed_date")
    private LocalDate departedDate;

}
