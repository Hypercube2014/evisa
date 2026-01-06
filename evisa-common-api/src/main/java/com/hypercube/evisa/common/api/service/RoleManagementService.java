package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchResultsDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface RoleManagementService {

    /**
     * @param roleDetail
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> createRoleDetails(RoleDetails roleDetail, String loggeduser, String locale);

    /**
     * @param roleDetail
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> updateRoleDetails(RoleDetails roleDetail, String loggeduser, String locale);

    /**
     * @param roleDetailsSearchDTO
     * @param loggeduser
     * @return
     */
    ResponseEntity<Page<RoleDetailsSearchResultsDTO>> searchRoleDetails(RoleDetailsSearchDTO roleDetailsSearchDTO,
            String loggeduser, String locale);

    /**
     * @param roleId
     * @param loggeduser
     * @return
     */
    ResponseEntity<RoleDetails> viewRoleDetails(Long roleId, String loggeduser, String locale);

    /**
     * @param roleId
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> deleteRoledetails(Long roleId, String loggeduser, String locale);

    /**
     * @param loggeduser
     * @return
     */
    ResponseEntity<List<RoleDetails>> getActiveRoles(String loggeduser, String locale);

    /**
     * @param rolecode
     * @return
     */
    RoleDetails getRoleDetailsbyRoleCode(String rolecode);

}
