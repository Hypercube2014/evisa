package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_APPLICATION_HEADER")
@NoArgsConstructor
@DynamicUpdate
public class ApplicationHeader implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6095536867230164758L;

    /**
     * 
     */
    @Id
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
    @Column(name = "DOCUMENT_STATUS", length = 3, nullable = true)
    private String documentStatus;

    /**
     * 
     */
    @Column(name = "VISA_STATUS", length = 3, nullable = true)
    private String visaStatus;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "SUBMITTED_DATE", insertable = true, updatable = false)
    private Date submittedDate;

    /**
     * 
     */
    @Column(name = "ASSIGNED_TO", length = 30, nullable = true)
    private String assignedTo;

    /**
     * 
     */
    @Column(name = "ALLOCATED_DATE", nullable = true)
    private Date allocatedDate;

    /**
     * 
     */
    @Column(name = "CLOSED_DATE", nullable = true)
    private Date closedDate;

    /**
     * 
     */
    @Column(name = "CLOSED_DATE_FOR_AVG_STAT", nullable = true)
    private Date closedDateForAvgStat;

    /**
     * 
     */
    @Column(name = "VISA_VALID_FROM", nullable = true)
    private Date visaValidFrom;

    /**
     * 
     */
    @Column(name = "VISA_VALID_TO", nullable = true)
    private Date visaValidTo;

    /**
     * 
     */
    @Column(name = "VISA_VALID_DURATION", length = 4)
    private int visaValidDuration;

    /**
     * 
     */
    @Column(name = "ORGANISATION_NAME", length = 50, nullable = true)
    private String organisationName;

    /**
     * 
     */
    @Column(name = "BUSINESS_ADDRESS", length = 255, nullable = true)
    private String bussinessName;

    /**
     * 
     */
    @Column(name = "SPONSOR_NAME", length = 150, nullable = true)
    private String sponserName;

    /**
     * 
     */
    @Column(name = "ORGANISATION_CATEGORY", length = 5, nullable = true)
    private String organisationCategory;

    /**
     * 
     */
    @Column(name = "arr_dep_indicator", length = 2, nullable = true)
    private String arrDepIndicator;

    /**
     * 
     */
    @Column(name = "arr_dep_id", length = 25, nullable = true)
    private Long arrDepId;

    /**
     * 
     */
    @Column(name = "extension_applied", length = 1, nullable = true)
    private String extensionApplied;
    
    /**
     * 
     */
    @Column(name = "VISA_EXPIRY", nullable = true)
    private Date visaExpiry;
    
    @Column(name = "statuspayement_penality",length = 1, nullable = true)
    private String statuspayementPenality;

    /**
     * @param applicationNumber
     * @param fileNumber
     * @param documentStatus
     * @param extensionApplied
     */
    public ApplicationHeader(String applicationNumber, String fileNumber, String documentStatus, String visaStatus,
            String extensionApplied) {
        super();
        this.applicationNumber = applicationNumber;
        this.fileNumber = fileNumber;
        this.documentStatus = documentStatus;
        this.visaStatus = visaStatus;
        this.extensionApplied = extensionApplied;
    }

}
