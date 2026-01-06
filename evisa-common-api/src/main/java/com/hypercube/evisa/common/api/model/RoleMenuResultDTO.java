package com.hypercube.evisa.common.api.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class RoleMenuResultDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6460210742317102840L;
    /**
     * 
     */
    private Long roleId;
    /**
     * 
     */
    private List<MenuDetailsSelectDTO> menuDetails;

    /**
     * 
     */
    private Long moduleId;

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
    private int orderNo;

}
