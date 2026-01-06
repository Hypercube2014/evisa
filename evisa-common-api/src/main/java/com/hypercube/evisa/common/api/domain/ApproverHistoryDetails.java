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

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "TEVI_APP_APPROVALHISTORY")
@Data
public class ApproverHistoryDetails {
    
    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "approval_history_seq")
    @SequenceGenerator(name = "approval_history_seq", sequenceName = "approval_history_seq", allocationSize = 1)
    @Column(name = "HISTORY_ID", length = 25)
    private Long historyId;
    
    /**
     * 
     */
    @Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
    private String applicationNumber;
    
    /**
     * 
     */
    @Column(name = "APPROVER", length = 25, nullable = false)
    private String approver;
    
    /**
     * 
     */
    @Column(name = "STATUS", length = 3, nullable = false)
    private String status;
    
    /**
     * 
     */
    @Column(name = "APPROVER_ROLE", length = 10, nullable = false)
    private String approverRole;
    
    /**
     * 
     */
    @Column(name = "REMARKS", length = 255, nullable = true)
    private String remarks;
    
    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "ACTION_DATE", insertable = true, updatable = false)
    private Date actionDate;

}
