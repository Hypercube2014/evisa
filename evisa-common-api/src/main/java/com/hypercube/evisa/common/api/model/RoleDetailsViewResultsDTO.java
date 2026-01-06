package com.hypercube.evisa.common.api.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class RoleDetailsViewResultsDTO {

    /**
     * 
     */
    private Long roleId;

    /**
     * 
     */
    private String roleCode;

    /**
     * 
     */
    private String roleDesc;

    /**
    * 
    */
    private String status;

    /**
    * 
    */
    private Date createdDate;

    /**
    * 
    */
    private String createdBy;

    /**
     * @param roleId
     * @param roleCode
     * @param roleDesc
     * @param status
     * @param createdDate
     * @param createdBy
     */
    public RoleDetailsViewResultsDTO(Long roleId, String roleCode, String roleDesc, String status, Date createdDate,
            String createdBy) {
        super();
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

}
