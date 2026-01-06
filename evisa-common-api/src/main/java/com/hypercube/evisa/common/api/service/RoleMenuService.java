package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.RoleMenuDTO;
import com.hypercube.evisa.common.api.model.RoleMenuResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface RoleMenuService {

    /**
     * @param roleMenuDetails
     * @param loggeduser
     * @param locale
     * @return
     */
    ResponseEntity<ApiResultDTO> updateRoleMenuDetails(RoleMenuDTO roleMenuDetails, String loggeduser,
            String locale);

    /**
     * @param roleId
     * @return
     */
    List<RoleMenuResultDTO> findallRoleMenuMap(Long roleId);

}
