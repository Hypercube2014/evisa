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
@Table(name = "tsys_role_menu_privilege")
@Data
public class RoleMenuPrivilege {

    /**
     * id field to store the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tsys_role_menu_seq")
    @SequenceGenerator(name = "tsys_role_menu_seq", sequenceName = "tsys_role_menu_seq", allocationSize = 1)
    @Column(name = "roleMenu_Id", length = 25, unique = true)
    private Long roleMenuId;

    /**
     * 
     */
    @Column(name = "role_id", length = 25)
    private Long roleId;

    /**
     * 
     */
    @Column(name = "menu_id", length = 25)
    private Long menuId;

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
