package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_APP_PERSONAL")
public class ApplicantPersonalDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7985078270228291906L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "djibevisa_applicant_no_seq")
    @GenericGenerator(name = "djibevisa_applicant_no_seq", strategy = "com.hypercube.evisa.common.api.util.DatePrefixedSequenceIdGenerator", parameters = {
            @Parameter(name = "valuePrefix", value = "A"), })
    @Column(name = "APPLICATION_NUMBER", length = 25)
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "FILE_NUMBER", length = 25, nullable = false)
    private String fileNumber;

    /**
     * 
     */
    @Column(name = "APPLIED_FOR", length = 2, nullable = false)
    private String appliedFor;

    /**
     * 
     */
    @Column(name = "TITLE", length = 4, nullable = false)
    private String title;

    /**
     * 
     */
    @Column(name = "SURNAME", length = 50, nullable = false)
    private String surname;

    /**
     * 
     */
    @Column(name = "GIVEN_NAME", length = 50, nullable = false)
    private String givenName;

    /**
     * 
     */
    @Column(name = "MIDDLE_NAME", length = 100, nullable = true)
    private String middleName;

    /**
     * 
     */
    @Column(name = "GENDER", length = 1, nullable = false)
    private String gender;

    /**
     * 
     */
    @Column(name = "DOB", nullable = false)
    private LocalDate dateOfBirth;
    
    /**
     * 
     */
    @Column(name = "MARITAL_STATUS", length = 1, nullable = false)
    private String maritalStatus;

    /**
     * 
     */
    @Column(name = "BIRTH_PLACE", length = 50, nullable = false)
    private String birthPlace;
    
    /**
     * 
     */
    @Column(name = "PROFESSION", length = 50, nullable = false)
    private String profession;
    
    /**
     * 
     */
    @Column(name = "ORIGIN_COUNTRY", length = 3, nullable = false)
    private String originCountry;

    /**
     * 
     */
    @Column(name = "BIRTH_COUNTRY", length = 3, nullable = false)
    private String birthCountry;

    /**
     * 
     */
    @Column(name = "NATIONALITY", length = 4, nullable = false)
    private String nationality;

    /**
     * 
     */
    @Column(name = "CURRENT_NATIONALITY", length = 3, nullable = false)
    private String currentNationality;

    /**
     * 
     */
    @Column(name = "EMAIL_ADDRESS", length = 255, nullable = false)
    private String emailAddress;

    /**
     * 
     */
    @Column(name = "PHONE_NUMBER", length = 12, nullable = false)
    private String phoneNumber;

    /**
     * 
     */
    @Column(name = "PREFERRED_LANG", length = 3, nullable = false)
    private String preferredLanguage;

    /**
     * 
     */
    @Column(name = "RESIDENTIAL_ADDRESS", length = 255, nullable = false)
    private String residentialAddress;

    /**
     * 
     */
    @Column(name = "DJIBOUTI_ADDRESS", length = 255, nullable = false)
    private String djiboutiAddress;
    

    /**
     * 
     */
    @Column(name = "RESIDENCE_COUNTRY", length = 3, nullable = false)
    private String residenceCountry;

    /**
     * 
     */
    @Column(name = "RESIDENCE_CITY", length = 25, nullable = false)
    private String residenceCity;

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
