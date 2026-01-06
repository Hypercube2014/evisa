package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "TSYS_APPLICANT_USER")
public class ApplicantUserRegisterDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7316820205544707289L;

    /**
     * 
     */
    @Id
    @Column(name = "username", length = 25, nullable = false)
    private String userName;
    
    /**
     * 
     */
    @Column(name = "IS_ORGANISATION")
    private boolean isOrganisation;
    
    /**
     * 
     */
    @Column(name = "ORGANISATION_NAME", length = 255, nullable = true)
    private String organisationName;

    /**
     * 
     */
    @Column(name = "ORGANISATION_CATEGORY", length = 5, nullable = true)
    private String organisationCategory;

    /**
     * 
     */
    @Column(name = "ORGANISATION_SPONSOR", length = 255, nullable = true)
    private String organisationSponsor;

    /**
     * 
     */
    @Column(name = "first_name", length = 250, nullable = true)
    private String firstName;

    /**
     * 
     */
    @Column(name = "last_name", length = 50, nullable = true)
    private String lastName;

    /**
     * 
     */
    @Column(name = "middle_name", length = 60, nullable = true)
    private String middleName;

    /**
     * 
     */
    @Column(name = "gender", length = 1, nullable = true)
    private String gender;

    /**
     * 
     */
    @Column(name = "mobile_number", length = 25, nullable = true)
    private String mobileNumber;

    /**
     * 
     */
    @Column(name = "email_id", length = 50, nullable = false, unique = true)
    private String emailId;

    /**
     * 
     */
    @Column(name = "role", length = 15, nullable = false)
    private String role;

    /**
     * 
     */
    @Column(name = "COUNTRY", length = 3, nullable = false)
    private String country;

    /**
     * 
     */
    @Column(name = "ADDRESS", length = 255, nullable = true)
    private String address;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @Column(name = "updated_by", length = 25, nullable = true)
    private String updatedBy;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * 
     */
    @Column(name = "active", length = 1, nullable = false)
    private String active;
    
    /**
     * 
     */
    @Transient
    private String profilePic;

}
