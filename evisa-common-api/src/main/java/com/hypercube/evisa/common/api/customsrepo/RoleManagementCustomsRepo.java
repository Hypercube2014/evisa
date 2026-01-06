package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.model.RoleDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchResultsDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface RoleManagementCustomsRepo {

    /**
     * @param roleDetailsSearchDTO
     * @return
     */
    Page<RoleDetailsSearchResultsDTO> searchRoleDetails(RoleDetailsSearchDTO roleDetailsSearchDTO);

}
