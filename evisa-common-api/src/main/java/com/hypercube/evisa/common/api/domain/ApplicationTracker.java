package com.hypercube.evisa.common.api.domain;

import java.time.LocalDate;
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
@Table(name = "vevi_application_tracker")
@NoArgsConstructor
public class ApplicationTracker {

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
    @Column(name = "USERNAME")
    private String username;
    
    /**
     * 
     */
    @Column(name = "IS_EXPRESS_VISA")
    private boolean isExpressVisa;
    
    /**
     * 
     */
    @Column(name = "APPLICANT_TYPE")
    private String applicantType;

    /**
     * 
     */
    @Column(name = "GIVEN_NAME")
    private String givenName;

    /**
     * 
     */
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    /**
     * 
     */
    @Column(name = "SURNAME")
    private String surname;

    /**
     * 
     */
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
    
    /**
     * 
     */
    @Column(name = "VISA_TYPE")
    private String visaType;
    
    /**
     * 
     */
    @Column(name = "VISA_DURATION")
    private int visaDuration;
    
    /**
     * 
     */
    @Column(name = "entry_type")
    private String entryType;
    
    /**
     * 
     */
    @Column(name = "visa_description_other")
    private String vistaDescOther;
    
    /**
     * 
     */
    @Column(name = "PASSPORT_NO")
    private String passportNumber;

    /**
     * 
     */
    @Column(name = "SUBMITTED_DATE")
    private Date submittedDate;
    
    /**
     * 
     */
    @Column(name = "ASSIGNED_TO")
    private String assignedTo;

    /**
     * 
     */
    @Column(name = "CLOSED_DATE")
    private Date closedDate;

    /**
     * 
     */
    @Column(name = "CLOSED_DATE_FOR_AVG_STAT")
    private Date closedDateForAvgStat;
    
    /**
     * 
     */
    @Column(name = "ALLOCATED_DATE")
    private Date allocatedDate;

    /**
     * 
     */
    @Column(name = "DOCUMENT_STATUS")
    private String documentStatus;

    /**
     * 
     */
    @Column(name = "VISA_STATUS")
    private String visaStatus;
    
    /**
     * 
     */
    @Column(name = "arr_dep_indicator")
    private String arrDepIndicator;
    
    /**
     * 
     */
    @Column(name = "arr_dep_id")
    private Long arrDepId;

      /**
     * 
     */
    @Column(name = "arrival_date")
    private LocalDate arrivalDate;
    
    
    /**
     * 
     */
    @Column(name = "travel_purpose")
    private String travelPurpose;
    
    /**
     * 
     */
    @Column(name = "nationality")
    private String nationality;
    
    /**
     * 
     */
    @Column(name = "dob")
    private Date dateOfBirth;

    /**
     * @param fileNumber
     * @param username
     * @param isExpressVisa
     * @param documentStatus
     */
    public ApplicationTracker(String fileNumber, String username, boolean isExpressVisa, String documentStatus) {
        super();
        this.fileNumber = fileNumber;
        this.username = username;
        this.isExpressVisa = isExpressVisa;
        this.documentStatus = documentStatus;
    }

}
