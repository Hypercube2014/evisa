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
public class MenuDetailsViewResultsDTO {

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
    private String menuNameOther;

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
     * 
     */
    private String moduleCode;

    /**
     * @param menuId
     * @param menuCode
     * @param menuName
     * @param menuNameOther
     * @param status
     * @param createdDate
     * @param createdBy
     * @param moduleCode
     */
    public MenuDetailsViewResultsDTO(Long menuId, String menuCode, String menuName, String menuNameOther, String status,
            Date createdDate, String createdBy, String moduleCode) {
        super();
        this.menuId = menuId;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuNameOther = menuNameOther;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.moduleCode = moduleCode;
    }

}
