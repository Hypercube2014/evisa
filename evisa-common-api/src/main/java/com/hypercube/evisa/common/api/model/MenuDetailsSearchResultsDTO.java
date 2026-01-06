package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class MenuDetailsSearchResultsDTO {

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
    private String moduleCode;

    /**
     * @param menuId
     * @param menuCode
     * @param menuName
     * @param menuNameOther
     * @param status
     * @param moduleCode
     */
    public MenuDetailsSearchResultsDTO(Long menuId, String menuCode, String menuName, String menuNameOther, String status,
            String moduleCode) {
        super();
        this.menuId = menuId;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuNameOther = menuNameOther;
        this.status = status;
        this.moduleCode = moduleCode;
    }

}
