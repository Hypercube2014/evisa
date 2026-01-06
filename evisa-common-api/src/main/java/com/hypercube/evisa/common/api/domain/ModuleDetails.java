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
@Table(name = "tsys_module_mgmt")
@Data
public class ModuleDetails {

    /**
     * id field to store the id
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tsys_module_mgmt_seq")
    @SequenceGenerator(name = "tsys_module_mgmt_seq", sequenceName = "tsys_module_mgmt_seq", allocationSize = 1)
    @Column(name = "MODULE_ID", length = 25)
    private Long moduleId;

    /**
     * Code field will contain the module code
     */
    @Column(name = "MODULE_CODE", length = 10, nullable = false)
    private String moduleCode;

    /**
     * field will contain module desc
     */
    @Column(name = "MODULE_DESC", length = 100, nullable = false)
    private String moduleDesc;

    /**
     * field will contain module desc in other lang
     */
    @Column(name = "MODULE_DESC_OTHER", length = 100, nullable = true)
    private String moduleDescOther;
    /**
     * 
     */
    @Column(name = "ACTIVE", length = 1, nullable = false)
    private String active;

    /**
     * 
     */
    @Column(name = "ORDERNO", length = 3)
    private int orderNo;

    /**
     * 
     */
    @Column(name = "CREATED_BY", length = 25, nullable = false)
    private String createdBy;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false, insertable = true, updatable = false)
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
    @Column(name = "UPDATED_DATE", nullable = true, insertable = false, updatable = true)
    private Date updateDate;

}
