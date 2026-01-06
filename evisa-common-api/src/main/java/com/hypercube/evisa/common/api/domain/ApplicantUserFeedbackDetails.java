package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name ="TSYS_USER_FEEDBACK")
public class ApplicantUserFeedbackDetails implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7466201890838733424L;
    
    /**
     * 
     */
    @Id
    @Column(name = "FEEDBACK_ID", length=25)
    private Long feedbackId;
    
    /**
     * 
     */
    @Column(name = "FEEDBACK_TYPE", length=2, nullable = false)
    private String feedbackType;
    
    /**
     * 
     */
    @Column(name = "DESCRIPTION", length=255, nullable = false)
    private String description;
    
    /**
     * 
     */
    @Column(name = "STATUS", length=1, nullable = false)
    private String status;
    
    /**
     * 
     */
    @Column(name = "REPLY", length=255, nullable = true)
    private String reply;
    
    /**
     * 
     */
    @Column(name = "CREATED_BY", length=25, nullable = false)
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
    @Column(name = "UPDATED_BY", length=25, nullable = true)
    private String updatedBy;
    
    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", insertable = false, updatable = true)
    private Date updatedDate;

}
