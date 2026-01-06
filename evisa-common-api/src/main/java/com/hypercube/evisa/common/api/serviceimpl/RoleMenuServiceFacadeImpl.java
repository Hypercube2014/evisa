package com.hypercube.evisa.common.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.RoleMenuResultDTO;
import com.hypercube.evisa.common.api.service.RoleMenuService;
import com.hypercube.evisa.common.api.service.RoleMenuServiceFacade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class RoleMenuServiceFacadeImpl implements RoleMenuServiceFacade {

    /**
     * 
     */
    @Autowired(required = true)
    RoleMenuService roleMenuService;

    /**
     * 
     */
    @Override
    public ResponseEntity<List<RoleMenuResultDTO>> getRoleMenuDetails(RoleDetails roleDetail, String loggeduser,
            String locale) {
        log.info("::RoleManagementServiceImpl::getRoleMenuDetails:: {}", roleDetail.getRoleId());

        return new ResponseEntity<>(roleMenuService.findallRoleMenuMap(roleDetail.getRoleId()), HttpStatus.OK);

    }

}
