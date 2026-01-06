package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class RoleMenuMapProj {

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
    private String moduleDesc;

    /**
     * 
     */
    private boolean selected;

    /**
     * 
     */
    private Long menuId;

    /**
     * 
     */
    private int orderNo;

    /**
     * @param menuCode
     * @param menuName
     * @param moduleCode
     * @param moduleDesc
     * @param selected
     * @param menuId
     */
    public RoleMenuMapProj(String menuCode, String menuName, String moduleCode, String moduleDesc, boolean selected,
            Long menuId) {
        super();
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.moduleCode = moduleCode;
        this.moduleDesc = moduleDesc;
        this.selected = selected;
        this.menuId = menuId;
    }

}
