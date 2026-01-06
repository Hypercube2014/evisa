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
@Data
@Entity
@Table(name = "vevi_application_overstay")
@NoArgsConstructor
public class ApplicationOverStayDetails {

    /**
     * 
     */
    @Id
    @Column(name = "APPLICATION_NUMBER")
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "FILE_NUMBER")
    private String fileNumber;

    /**
     * 
     */
    @Column(name = "VISA_EXPIRY")
    private Date visaExpiry;

    /**
     * 
     */
    @Column(name = "APPROVER")
    private String approver;
    
    
    @Column(name = "given_name")
    private String surname;
    
    @Column(name = "middle_name")
    private String middle_name;
    
    @Column(name = "username")
    private String username;

}
