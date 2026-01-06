/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ApplicantPersonalDetailsDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -676637726516385813L;

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
    private String appliedFor;

    /**
     * 
     */
    private String title;

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
    private String middleName;

    /**
     * 
     */
    private String gender;

    /**
     * 
     */
    private LocalDate dateOfBirth;
    
    /**
     * 
     */
    private String maritalStatus;

    /**
     * 
     */
    private String birthPlace;
    
    /**
     * 
     */
    private String profession;
    
    /**
     * 
     */
    private String originCountry;

    /**
     * 
     */
    private String birthCountry;

    /**
     * 
     */
    private String nationality;

    /**
     * 
     */
    private String currentNationality;

    /**
     * 
     */
    private String emailAddress;

    /**
     * 
     */
    private String phoneNumber;

    /**
     * 
     */
    private String preferredLanguage;

    /**
     * 
     */
    private String residentialAddress;

    /**
     * 
     */
    private String djiboutiAddress;
    
  
    /**
     * 
     */
    private String residenceCountry;

    /**
     * 
     */
    private String residenceCity;

    /**
     * @param applicationNumber
     * @param givenName
     * @param dateOfBirth
     * @param emailAddress
     */
    public ApplicantPersonalDetailsDTO(String applicationNumber, String givenName, LocalDate dateOfBirth,
            String emailAddress) {
        this.applicationNumber = applicationNumber;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.emailAddress = emailAddress;

    }

}
