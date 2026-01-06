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
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "master_codetype_details")
@Data
@NoArgsConstructor
public class MasterCodeTypeDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6598557548478437095L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "master_codetype_details_seq")
    @SequenceGenerator(name = "master_codetype_details_seq", sequenceName = "master_codetype_details_seq", allocationSize = 1)
    @Column(name = "id", length = 25)
    private Long id;

    /**
     * 
     */
    @Column(name = "code_type", length = 5, nullable = false, unique = true)
    private String codeType;

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
    @Column(name = "active", length = 1, nullable = false)
    private String active;

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
     * @param codeType
     * @param description
     * @param descriptionOther
     * @param active
     */
    public MasterCodeTypeDetails(Long id, String codeType, String description, String descriptionOther, String active) {
        super();
        this.id = id;
        this.codeType = codeType;
        this.description = description;
        this.descriptionOther = descriptionOther;
        this.active = active;
    }

}
