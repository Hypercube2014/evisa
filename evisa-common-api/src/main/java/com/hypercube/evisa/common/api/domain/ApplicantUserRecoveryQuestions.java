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

@Entity
@Data
@Table(name = "TSYS_PASSWORD_RECOVERY_QUES")
public class ApplicantUserRecoveryQuestions implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1563761850722588783L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tmhb_pwd_recvry_ques_seq")
    @SequenceGenerator(name = "tmhb_pwd_recvry_ques_seq", sequenceName = "tmhb_pwd_reg_recvry_ques_seq", allocationSize = 1)
    @Column(name = "quesid", length = 25, unique = true)
    private Long quesid;

    /**
     * 
     */
    @Column(name = "code", length = 5, nullable = false)
    private String code;

    /**
     * 
     */
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    /**
     * 
     */
    @Column(name = "description_other", length = 512, nullable = false)
    private String descriptionother;

    /**
     * 
     */
    @Column(name = "status", length = 1, nullable = false)
    private String status;

    /**
     * 
     */
    @Column(name = "created_by", length = 25, nullable = false)
    private String createdBy;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "created_date", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @Column(name = "updated_by", length = 25, nullable = true)
    private String updatedBy;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false, updatable = true)
    private Date updatedDate;

}
