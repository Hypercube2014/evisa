package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class RoleDetailsSearchDTO {

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
    private int pageSize;

    /**
     * 
     */
    private int pageNumber;

}
