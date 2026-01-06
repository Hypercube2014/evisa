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
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "TSYS_PRODUCT_CONFIG")
@Data
@NoArgsConstructor
public class ProductConfig {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_config_seq")
    @SequenceGenerator(name = "product_config_seq", sequenceName = "product_config_seq", allocationSize = 1)
    @Column(name = "CONFIG_ID", length = 25)
    private Long configId;

    /**
     * 
     */
    @Column(name = "CONFIG_CODE", length = 15, unique = true)
    private String configCode;

    /**
     * 
     */
    @Column(name = "CONFIG_VALUE", length = 50, nullable = false)
    private String configValue;

    /**
     * 
     */
    @Column(name = "CONFIG_DESC", length = 255, nullable = false)
    private String configDesc;

    /**
     * 
     */
    @Column(name = "SYSTEM", length = 10, nullable = false)
    private String system;

    /**
     * 
     */
    @Column(name = "STATUS", length = 1, nullable = false)
    private String status;

    /**
     * 
     */
    @Column(name = "CREATED_BY", length = 25, nullable = false)
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
    @Column(name = "UPDATED_BY", length = 25, nullable = true)
    private String updatedBy;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * @param configId
     * @param configCode
     * @param configValue
     * @param configDesc
     * @param system
     * @param status
     */
    public ProductConfig(Long configId, String configCode, String configValue, String configDesc, String system,
            String status) {
        super();
        this.configId = configId;
        this.configCode = configCode;
        this.configValue = configValue;
        this.configDesc = configDesc;
        this.system = system;
        this.status = status;
    }

}
