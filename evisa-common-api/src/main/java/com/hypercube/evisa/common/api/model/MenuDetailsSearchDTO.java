package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class MenuDetailsSearchDTO {

    /**
     * 
     */

    private Long menuId;

    /**
     * 
     */
    private String menuCode;

    /**
     * 
     */
    private String menuName;

    /**
     * 
     */

    private String moduleCode;
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
