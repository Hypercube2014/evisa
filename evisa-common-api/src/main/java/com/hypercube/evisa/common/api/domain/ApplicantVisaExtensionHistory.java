package com.hypercube.evisa.common.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "TEVI_APP_VISA_EXTENSION_HISTORY")
public class ApplicantVisaExtensionHistory {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "visa_extension_his_id_seq")
    @SequenceGenerator(name = "visa_extension_his_id_seq", sequenceName = "visa_extension_his_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "visa_extension_his_id", length = 25)
    private Long visaExtensionHisId;

    /**
     * 
     */
    @Column(name = "visa_extension_id", length = 25, nullable = false)
    private String visaExtensionId;

    /**
     * 
     */
    @Column(name = "action_by", length = 25, nullable = false)
    private String actionBy;
    
    /**
     * 
     */
    @Column(name = "role", length = 25, nullable = false)
    private String role;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "action_date", insertable = true, updatable = false)
    private Date actionDate;

    /**
     * 
     */
    @Column(name = "STATUS", length = 3, nullable = false)
    private String status;

    /**
     * 
     */
    @Column(name = "REMARKS", nullable = true)
    private String remarks;

    /**
     * @param visaExtensionId
     * @param actionBy
     * @param status
     * @param remarks
     */
    public ApplicantVisaExtensionHistory(String visaExtensionId, String actionBy, String status, String remarks) {
        super();
        this.visaExtensionId = visaExtensionId;
        this.actionBy = actionBy;
        this.status = status;
        this.remarks = remarks;
    }

}
