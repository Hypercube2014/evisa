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
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "tsys_role_mgmt")
@Data
public class RoleDetails {

    /**
     * id field to store the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tsys_role_mgmt_seq")
    @SequenceGenerator(name = "tsys_role_mgmt_seq", sequenceName = "tsys_role_mgmt_seq", allocationSize = 1)
    @Column(name = "role_id", length = 25)
    private Long roleId;

    /**
     * Code field will contain the role code
     */
    @Column(name = "role_code", length = 10, nullable = false, unique = true)
    private String roleCode;
    
    /**
     * field will contain the role description
     */
    @Column(name = "role_desc", length = 256, nullable = false)
    private String roleDesc;

    /**
     * field will contain the role description other
     */
    @Column(name = "role_desc_other", length = 256, nullable = true)
    private String roleDescOther;

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
