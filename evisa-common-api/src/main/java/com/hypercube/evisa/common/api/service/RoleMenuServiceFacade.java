package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.RoleMenuResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface RoleMenuServiceFacade {

    /**
     * @param roleDetail
     * @param loggeduser
     * @param locale
     * @return
     */
    ResponseEntity<List<RoleMenuResultDTO>> getRoleMenuDetails(RoleDetails roleDetail, String loggeduser,
            String locale);

}
