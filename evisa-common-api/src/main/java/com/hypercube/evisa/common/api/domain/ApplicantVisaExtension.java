package com.hypercube.evisa.common.api.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionFileUploadDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Data
@Table(name = "TEVI_APP_VISA_EXTENSION")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantVisaExtension {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "visa_extension_id_seq")
    @GenericGenerator(name = "visa_extension_id_seq", strategy = "com.hypercube.evisa.common.api.util.DatePrefixedSequenceIdGenerator", parameters = {
            @Parameter(name = "valuePrefix", value = "E"), })
    @Column(name = "visa_extension_id", length = 25)
    private String visaExtensionId;

    /**
     * 
     */
    @Column(name = "username", length = 25, nullable = false)
    private String username;

    /**
     * 
     */
    @Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "DAYS_OF_EXTENSION", length = 3)
    private int daysOfExtension;

    /**
     * 
     */
    @Column(name = "PENALITY_AMOUNT", length = 15)
    private Long penalityAmount;

    /**
     * 
     */
    @Column(name = "TOTAL_AMOUNT", length = 15, nullable = false)
    private Long totalAmount;

    /**
     * 
     */
    @Column(name = "REASON_FOR_EXTENSION", length = 5 , nullable = false)
    private String reasonsForExtension;
    
    /**
     * 
     */
    @Column(name = "OTHER_REMARKS", nullable = true)
    private String otherRemarks;
    
    /**
     * 
     */
    @Column(name ="PREVIOUS_VISA_EXPIRY", nullable = false)
    private Date previousExpiryDate;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "REQUESTED_DATE", insertable = true, updatable = false)
    private Date requestedDate;

    /**
     * 
     */
    @Column(name = "DOCUMENT_STATUS", length = 3, nullable = true)
    private String documentStatus;

    /**
     * 
     */
    @Column(name = "EXTENSION_STATUS", length = 3, nullable = true)
    private String extensionStatus;
    
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
    @Column(name = "assigned_to", length = 25, nullable = true)
    private String assignedTo;

    /**
     * 
     */
    @Column(name = "visa_type", length = 25, nullable = false)
    private String visaType;
    
    /**
     * 
     */
    @Column(name ="NEW_VISA_EXPIRY", nullable = true)
    private Date newExpiryDate;
    
    /**
     * 
     */
    @Transient
    private List<ApplicantVisaExtensionFileUploadDTO> fileUploads;
    
    /**
     * @param visaExtensionId
     * @param username
     * @param applicationNumber
     * @param daysOfExtension
     * @param totalAmount
     * @param extensionStatus
     * @param documentStatus
     */
    public ApplicantVisaExtension(String visaExtensionId, String username, String applicationNumber,
            int daysOfExtension, Long totalAmount, String extensionStatus, String documentStatus) {
        super();
        this.visaExtensionId = visaExtensionId;
        this.username = username;
        this.applicationNumber = applicationNumber;
        this.daysOfExtension = daysOfExtension;
        this.totalAmount = totalAmount;
        this.extensionStatus = extensionStatus;
        this.documentStatus = documentStatus;
    }

}
