/**
 * 
 */
package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "TEVI_MGC_VISA_DETAILS")
@Data
@NoArgsConstructor
public class ManagementVisaDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2685333565937962554L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "visa_details_seq")
    @SequenceGenerator(name = "visa_details_seq", sequenceName = "visa_details_seq", allocationSize = 1)
    @Column(name = "VISA_ID", length = 25)
    private Long visaId;

    /**
     * 
     */
    @Column(name = "VISA_TYPE", length = 25, nullable = false, unique = true)
    private String visaType;

    /**
     * 
     */
    @Column(name = "VISA_DESCRIPTION", length = 100, nullable = false)
    private String visaDescription;

    /**
     * 
     */
    @Column(name = "VISA_DESCRIPTION_OTHER", length = 100, nullable = false)
    private String visaDescriptionOther;
    
    /**
     * 
     */
    @Column(name = "VISA_DURATION", length = 4, nullable = false)
    private int visaDuration;
    
    /**
     * 
     */
    @Column(name = "ENTRY_TYPE", length = 1, nullable = false)
    private String entryType;

    /**
     * 
     */
    @Column(name = "VISA_DURATION_FROM", nullable = true)
    private Date visaDurationFrom;

    /**
     * 
     */
    @Column(name = "VISA_DURATION_TO", nullable = true)
    private Date visaDurationTo;

    /**
     * 
     */
    @Column(name = "VISA_FEE", length = 25, nullable = false)
    private Long visaFee;

    /**
     * 
     */
    @Column(name = "EXPRESS_VISA_ALLOWED")
    private boolean isExpressVisaAllowed;

    /**
     * 
     */
    @Column(name = "EXPRESS_VISA_FEE", length = 25, nullable = true)
    private Long expressVisaFee;

    /**
     * 
     */
    @Column(name = "CURRENCY", length = 3, nullable = false)
    private String currency;

    /**
     * 
     */
    @Column(name = "CREATED_BY", length = 25, nullable = false)
    private String createdBy;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @Column(name = "UPDATED_BY", length = 25, nullable = true)
    private String updatedBy;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * 
     */
    @Column(name = "STATUS", length = 1, nullable = false)
    private String status;

    /**
     * @param visaId
     * @param visaType
     * @param visaDescription
     * @param visaDescriptionOther
     * @param visaFee
     * @param isExpressVisaAllowed
     * @param status
     */
    public ManagementVisaDetails(Long visaId, String visaType, String visaDescription, String visaDescriptionOther,
            Long visaFee, boolean isExpressVisaAllowed, String status) {
        super();
        this.visaId = visaId;
        this.visaType = visaType;
        this.visaDescription = visaDescription;
        this.visaDescriptionOther = visaDescriptionOther;
        this.visaFee = visaFee;
        this.isExpressVisaAllowed = isExpressVisaAllowed;
        this.status = status;
    }

}
