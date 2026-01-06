/**
 * 
 */
package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_MGC_RISK")
public class ManagementRiskDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6566224280514039272L;
    
    /**
     * 
     */
    @Id
    @Column(name = "RISK_ID", length = 25, nullable = false)
    private Long riskId;
    
    /**
     * 
     */
    @Column(name = "RISK_CODE", length = 25, nullable = false)
    private String riskCode;
    
    /**
     * 
     */
    @Column(name = "RISK_LANE", length = 1, nullable = false)
    private String riskane;
    
    /**
     * 
     */
    @Column(name = "RISK_DESCRIPTION", length = 25, nullable = false)
    private String riskDescription;

}
