package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ApplicantUserRegisterDetailsDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8696306525173841470L;

    /**
     * 
     */
    private String userName;
    
    /**
     * 
     */
    private String secretKey;

    /**
     * 
     */
    private boolean isOrganisation;

    /**
     * 
     */
    private String organisationName;

    /**
     * 
     */
    private String organisationCategory;

    /**
     * 
     */
    private String organisationSponsor;

    /**
     * 
     */
    private String firstName;

    /**
     * 
     */
    private String lastName;

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
    private String mobileNumber;

    /**
     * 
     */
    private String identificationNumber;

    /**
     * 
     */
    private String emailId;

    /**
     * 
     */
    private String role;

    /**
     * 
     */
    private String country;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    private String createdBy;

    /**
     * 
     */
    private String updatedBy;

    /**
     * 
     */
    private String active;

}
