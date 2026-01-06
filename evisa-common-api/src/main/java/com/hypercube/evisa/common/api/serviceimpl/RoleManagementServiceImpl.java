package com.hypercube.evisa.common.api.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchResultsDTO;
import com.hypercube.evisa.common.api.repository.RoleManagementRepository;
import com.hypercube.evisa.common.api.service.RoleManagementService;
import com.hypercube.evisa.common.api.util.CommonQueueUtilService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 */
@Service
@Slf4j
@Data
public class RoleManagementServiceImpl implements RoleManagementService {

    /**
     * 
     */
    @Autowired(required = true)
    private RoleManagementRepository roleManagementRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> createRoleDetails(RoleDetails roleDetail, String loggeduser, String locale) {
        log.info("::RoleManagementServiceImpl::createRoleDetails:: {}", roleDetail.getRoleId());

        ApiResultDTO apiResultDTO;

        if (roleManagementRepository.existsByRoleCode(roleDetail.getRoleCode())) {

            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT, CommonsConstants.ROLE_MGMT,
                                new ObjectMapper().writeValueAsString(roleDetail), "error.already.exists", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("createRoleDetails-JsonProcessingException {}", jpe.getCause());
            }

            List<Object> objArray = new ArrayList<>();
            objArray.add(roleDetail.getRoleCode());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {

            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT, CommonsConstants.ROLE_MGMT,
                                new ObjectMapper().writeValueAsString(roleDetail), "save.success", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("createRoleDetails-JsonProcessingException {}", jpe.getCause());
            }

            RoleDetails roleDetails = roleManagementRepository.save(roleDetail);

            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
            apiResultDTO.setId(roleDetails.getRoleId());
        }

        return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> updateRoleDetails(RoleDetails roleDetail, String loggeduser, String locale) {
        log.info("::RoleManagementServiceImpl::updateRoleDetails:: {}", roleDetail.getRoleId());

        ApiResultDTO apiResultDTO;

        if (roleManagementRepository.existsByRoleId(roleDetail.getRoleId())) {

            RoleDetails roleDetails = roleManagementRepository.save(roleDetail);

            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE, CommonsConstants.ROLE_MGMT,
                                new ObjectMapper().writeValueAsString(roleDetail), "update.success", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("updateRoleDetails-JsonProcessingException {}", jpe.getCause());
            }

            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("update.success", null, locale, null));
            apiResultDTO.setId(roleDetails.getRoleId());
        } else {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                        CommonsConstants.ROLE_MGMT, new ObjectMapper().writeValueAsString(roleDetail),
                        "error.invalid.request.notexist", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("updateRoleDetails-JsonProcessingException {}", jpe.getCause());
            }

            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.invalid.request.notexist", null, locale, null));
        }

        return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);

    }

    /**
     * 
     */
    @Override
    public ResponseEntity<Page<RoleDetailsSearchResultsDTO>> searchRoleDetails(
            RoleDetailsSearchDTO roleDetailsSearchDTO, String loggeduser, String locale) {
        log.info("::RoleManagementServiceImpl::searchRoleDetails:: {}", roleDetailsSearchDTO.getRoleId());

        return new ResponseEntity<Page<RoleDetailsSearchResultsDTO>>(
                roleManagementRepository.searchRoleDetails(roleDetailsSearchDTO), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<RoleDetails> viewRoleDetails(Long roleId, String loggeduser, String locale) {
        log.info("::RoleManagementServiceImpl::viewRoleDetails:: {}", roleId);

        return new ResponseEntity<>(roleManagementRepository.findByRoleId(roleId), HttpStatus.OK);

    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> deleteRoledetails(Long roleId, String loggeduser, String locale) {
        log.info("::RoleManagementServiceImpl::deleteRoledetails::");

        commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.DELETE,
                CommonsConstants.ROLE_MGMT, null, "Deleted id: " + roleId, loggeduser));

        roleManagementRepository.deleteById(roleId);
        return new ResponseEntity<>(new ApiResultDTO(CommonsConstants.SUCCESS,
                LocaleConfig.getResourceValue("delete.success", null, locale, null)), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<List<RoleDetails>> getActiveRoles(String loggeduser, String locale) {
        log.info("RoleManagementServiceImpl:: getActiveRoles");

        return new ResponseEntity<List<RoleDetails>>(roleManagementRepository.findByStatus("Y"), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public RoleDetails getRoleDetailsbyRoleCode(String rolecode) {
        log.info("RoleManagementServiceImpl:: getRoleDetailsbyRoleCode");

        return roleManagementRepository.findByRoleCode(rolecode);
    }
}