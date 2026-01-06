package com.hypercube.evisa.common.api.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class RoleMenuDTO {

    /**
     * 
     */
    private Long roleId;

    /**
     * 
     */
    private List<Long> menuId;

    /**
     * 
     */
    private String createdBy;

    /**
     * 
     */
    @CreationTimestamp
    private Date createdDate;

}
