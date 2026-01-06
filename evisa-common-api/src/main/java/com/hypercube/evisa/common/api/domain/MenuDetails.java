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
@Table(name = "tsys_menu_mgmt")
@Data
public class MenuDetails {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tsys_menu_mgmt_seq")
    @SequenceGenerator(name = "tsys_menu_mgmt_seq", sequenceName = "tsys_menu_mgmt_seq", allocationSize = 1)
    @Column(name = "menu_id", length = 25)
    private Long menuId;

    /**
     * 
     */

    @Column(name = "menu_code", length = 7, nullable = false, unique = true)
    private String menuCode;

    /**
     * 
     */
    @Column(name = "menu_name", length = 100, nullable = false)
    private String menuName;
    /**
     * 
     */
    @Column(name = "menu_name_other", length = 100, nullable = true)
    private String menuNameOther;

    /**
     * 
     */
    @Column(name = "module_code", length = 10, nullable = false)
    private String moduleCode;
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
