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
@Data
@Entity
@Table(name = "tsys_audit_details")
@NoArgsConstructor
public class AuditDetails {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "audit_seq_id")
    @SequenceGenerator(name = "audit_seq_id", sequenceName = "audit_seq_id", allocationSize = 1, initialValue = 1)
    @Column(name = "audit_id", length = 25)
    private Long auditId;

    /**
     * INSERT, UPDATE, SEARCH, DELETE, SELECT
     */
    @Column(name = "function_code", length = 6, nullable = false)
    private String functionCode;

    /**
     * 
     */
    @Column(name = "audit_type", length = 10, nullable = false)
    private String auditModule;

    /**
     * 
     */
    @Column(name = "audit_value", nullable = true)
    private String auditValue;

    /**
     * 
     */
    @Column(name = "audit_desc", length = 50, nullable = true)
    private String auditDesc;

    /**
     * 
     */
    @Column(name = "created_by", length = 25, nullable = true)
    private String createdBy;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "created_date", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * @param functionCode
     * @param auditModule
     * @param auditValue
     * @param createdBy
     */
    public AuditDetails(String functionCode, String auditModule, String auditValue, String auditDesc,
            String createdBy) {
        super();
        this.functionCode = functionCode;
        this.auditModule = auditModule;
        this.auditValue = auditValue;
        this.auditDesc = auditDesc;
        this.createdBy = createdBy;
    }

}
