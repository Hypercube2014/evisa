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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "master_code_details", uniqueConstraints = @UniqueConstraint(columnNames = { "code", "code_type" }))
@Data
@NoArgsConstructor
public class MasterCodeDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6598557548478437095L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "master_code_details_seq")
    @SequenceGenerator(name = "master_code_details_seq", sequenceName = "master_code_details_seq", allocationSize = 1)
    @Column(name = "id", length = 25)
    private Long id;

    /**
     * 
     */
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    /**
     * 
     */
    @Column(name = "description", length = 150, nullable = false)
    private String description;

    /**
     * 
     */
    @Column(name = "description_other", length = 150, nullable = false)
    private String descriptionOther;

    /**
     * 
     */
    @Column(name = "code_type", length = 5, nullable = false)
    private String codeType;

    /**
     * 
     */
    @Column(name = "active", length = 1, nullable = false)
    private String active;

    /**
     * 
     */
    @Column(name = "created_by", length = 20, nullable = false)
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
    @Column(name = "updated_by", length = 20, nullable = true)
    private String updatedBy;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * @param id
     * @param code
     * @param description
     * @param descriptionOther
     * @param active
     */
    public MasterCodeDetails(Long id, String code, String description, String descriptionOther, String active) {
        super();
        this.id = id;
        this.code = code;
        this.description = description;
        this.descriptionOther = descriptionOther;
        this.active = active;
    }

}
