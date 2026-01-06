package com.hypercube.evisa.approver.api.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "vevi_overstay_departed")
@NoArgsConstructor
public class OverstayDeparted {
	
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
    @Column(name = "approver")
    private String approver;

    @Column(name = "action_date")
    private String action_date;
    
    @Column(name = "AMOUNT", length = 25, nullable = false)
	private double amount;
    /**
     * 
     */
    @Column(name = "visa_expiry")
    private Date visaExpiry;
	

}
