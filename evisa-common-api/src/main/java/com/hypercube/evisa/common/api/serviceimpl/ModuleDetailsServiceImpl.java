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
import com.hypercube.evisa.common.api.domain.ModuleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailSearchResultsDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailsSearchDTO;
import com.hypercube.evisa.common.api.repository.ModuleDetailsRepository;
import com.hypercube.evisa.common.api.service.ModuleDetailsService;
import com.hypercube.evisa.common.api.util.CommonQueueUtilService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ModuleDetailsServiceImpl implements ModuleDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    ModuleDetailsRepository moduleDetailsRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Override
    public ResponseEntity<Page<ModuleDetailSearchResultsDTO>> searchModuleCodedetails(
            ModuleDetailsSearchDTO moduleDetailsSearchDTO, String loggeduser) {
        log.info("::ModuleDetailsServiceImpl::searchModuleCodedetails::");

        return new ResponseEntity<>(moduleDetailsRepository.searchModuleDetails(moduleDetailsSearchDTO), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ModuleDetails> viewModuleCodedetails(Long moduleId, String loggeduser, String locale) {
        log.info("::ModuleDetailsServiceImpl::viewModuleCodedetails:: {}", moduleId);

        return new ResponseEntity<ModuleDetails>(moduleDetailsRepository.findByModuleId(moduleId), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> saveModuleDetails(ModuleDetails moduleDetails, String locale,
            String loggeduser) {
        log.info("::ModuleDetailsServiceImpl::saveModuleDetails::");

        ApiResultDTO apiResultDTO;

        /* Verify the code already exists in System */
        boolean coderesult = moduleDetailsRepository.existsByModuleCode(moduleDetails.getModuleCode());

        boolean orderresult = moduleDetailsRepository.existsByOrderNo(moduleDetails.getOrderNo());

        /* verify the order number exists only when id is null */
        if (orderresult && moduleDetails.getModuleId() == null) {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.MODULE_MGMT, new ObjectMapper().writeValueAsString(moduleDetails),
                        "orderno.already.exists", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("saveModuleDetails-JsonProcessingException {}", jpe.getCause());
            }

            List<Object> objArray = new ArrayList<>();
            objArray.add(moduleDetails.getOrderNo());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        }
        /* verify the code exists only when id is null */
        else if (coderesult && moduleDetails.getModuleId() == null) {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.MODULE_MGMT, new ObjectMapper().writeValueAsString(moduleDetails),
                        "modulecode.already.exists", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("saveModuleDetails-JsonProcessingException {}", jpe.getCause());
            }

            List<Object> objArray = new ArrayList<>();
            objArray.add(moduleDetails.getModuleCode());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {

            moduleDetails = moduleDetailsRepository.save(moduleDetails);

            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT, CommonsConstants.MODULE_MGMT,
                                new ObjectMapper().writeValueAsString(moduleDetails), "save.success", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("saveModuleDetails-JsonProcessingException {}", jpe.getCause());
            }

            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
            apiResultDTO.setId(moduleDetails.getModuleId());
        }

        return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
    }

    /**
     * delete module details
     */
    @Override
    public ResponseEntity<ApiResultDTO> deleteModuleCodedetails(Long moduleId, String loggeduser, String locale) {
        log.info("::ModuleDetailsServiceImpl::deleteModuleCodedetails::");

        commonQueueUtilService.sendAuditDetailsToQueue(
                new AuditDetails(CommonsConstants.DELETE, CommonsConstants.MODULE_MGMT, null, null, loggeduser));

        moduleDetailsRepository.deleteById(moduleId);
        return new ResponseEntity<>(new ApiResultDTO(CommonsConstants.SUCCESS,
                LocaleConfig.getResourceValue("delete.success", null, locale, null)), HttpStatus.OK);
    }

    /**
     * update module code
     */
    @Override
    public ResponseEntity<ApiResultDTO> updateModuleDetails(ModuleDetails moduleDetails, String loggeduser,
            String locale) {
        log.info("::ModuleDetailsServiceImpl::updateModuleDetails:: {}", moduleDetails.getModuleId());

        ApiResultDTO apiResultDTO;

        ModuleDetails modDtls = moduleDetailsRepository.findByOrderNo(moduleDetails.getOrderNo());

        if (null != modDtls && !modDtls.getModuleId().equals(moduleDetails.getModuleId())) {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                        CommonsConstants.MODULE_MGMT, new ObjectMapper().writeValueAsString(moduleDetails),
                        "error.already.exists", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("updateModuleDetails-JsonProcessingException {}", jpe.getCause());
            }

            List<Object> objArray = new ArrayList<>();
            objArray.add(moduleDetails.getOrderNo());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {
            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE, CommonsConstants.MODULE_MGMT,
                                new ObjectMapper().writeValueAsString(moduleDetails), "update.success", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("updateModuleDetails-JsonProcessingException {}", jpe.getCause());
            }
            moduleDetailsRepository.save(moduleDetails);
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("update.success", null, locale, null));
        }
        return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<List<ModuleDetails>> getActiveModules(String loggeduser, String locale) {
        log.info("ModuleDetailsServiceImpl:: getActiveModules");

        return new ResponseEntity<List<ModuleDetails>>(moduleDetailsRepository.findByActive("Y"), HttpStatus.OK);

    }

}
