package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class RoleDetailsSearchResultsDTO {

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
    private String roleDescOther;

    /**
    * 
    */
    private String status;

    /**
     * @param roleId
     * @param roleCode
     * @param roleDesc
     * @param roleDescOther
     * @param status
     */
    public RoleDetailsSearchResultsDTO(Long roleId, String roleCode, String roleDesc, String roleDescOther, String status) {
        super();
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
        this.roleDescOther = roleDescOther;
        this.status = status;
    }

}
