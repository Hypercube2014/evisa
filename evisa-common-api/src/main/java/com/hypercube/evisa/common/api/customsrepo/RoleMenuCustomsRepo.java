package com.hypercube.evisa.common.api.customsrepo;

import java.util.List;

import com.hypercube.evisa.common.api.model.RoleMenuMapProj;

/**
 * @author SivaSreenivas
 *
 */
public interface RoleMenuCustomsRepo {
    
/**
 * @param roleId
 * @param status
 * @return
 */
List<RoleMenuMapProj> findallRoleMenuMap( Long roleId,  String status);
}
