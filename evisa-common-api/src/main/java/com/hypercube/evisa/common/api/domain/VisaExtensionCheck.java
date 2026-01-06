package com.hypercube.evisa.common.api.domain;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Data
@Table(name = "vevi_visa_extension_check")
@NoArgsConstructor
public class VisaExtensionCheck {

    /**
     * 
     */
    @Id
    @Column(name = "application_number")
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "username")
    private String username;

    /**
     * 
     */
    @Column(name = "file_number")
    private String fileNumber;

    /**
     * 
     */
    @Column(name = "given_name")
    private String givenName;

    /**
     * 
     */
    @Column(name = "email_address")
    private String emailAddress;

    /**
     * 
     */
    @Column(name = "passport_no")
    private String passportNo;

    /**
     * 
     */
    @Column(name = "visa_expiry")
    private Date visaExpiry;

}
