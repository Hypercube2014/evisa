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
import com.hypercube.evisa.common.api.domain.MenuDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchResultsDTO;
import com.hypercube.evisa.common.api.repository.MenuManagementRepository;
import com.hypercube.evisa.common.api.service.MenuManagementService;
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
public class MenuManagementServiceImpl implements MenuManagementService {

    /**
     * 
     */
    @Autowired(required = true)
    private MenuManagementRepository menuManagementRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> insertMenuDetails(MenuDetails menuDetails, String loggeduser, String locale) {
        log.info("::MenuManagementServiceImpl::MenuDetails:: {}", menuDetails.getMenuId());

        ApiResultDTO apiResultDTO;
        if (menuManagementRepository.existsByMenuCode(menuDetails.getMenuCode())) {
            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.MENU_MGMT, new ObjectMapper().writeValueAsString(menuDetails),
                        "Menu Code Already Exist", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("insertMenuDetails-JsonProcessingException {}", jpe.getCause());
            }
            List<Object> objArray = new ArrayList<>();
            objArray.add(menuDetails.getMenuCode());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {
            menuDetails = menuManagementRepository.save(menuDetails);

            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT, CommonsConstants.MENU_MGMT,
                                new ObjectMapper().writeValueAsString(menuDetails), "Menu Details Insert", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("insertMenuDetails-JsonProcessingException {}", jpe.getCause());
            }

            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
            apiResultDTO.setId(menuDetails.getMenuId());
        }
        return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> updateMenuDetails(MenuDetails menuDetails, String loggeduser, String locale) {
        log.info("::MenuManagementServiceImpl::updateMenuDetails:: {}", menuDetails.getMenuId());

        ApiResultDTO apiResultDTO;
        if (menuManagementRepository.existsByMenuId(menuDetails.getMenuId())) {
            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE, CommonsConstants.MENU_MGMT,
                                new ObjectMapper().writeValueAsString(menuDetails), "Menu Code Updated", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("updateMenuDetails-JsonProcessingException {}", jpe.getCause());
            }

            menuManagementRepository.save(menuDetails);

            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("update.success", null, locale, null));
        } else {
            try {
                commonQueueUtilService
                        .sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE, CommonsConstants.MENU_MGMT,
                                new ObjectMapper().writeValueAsString(menuDetails), "code.notexist", loggeduser));
            } catch (JsonProcessingException jpe) {
                log.error("updateMenuDetails-JsonProcessingException {}", jpe.getCause());
            }

            List<Object> objArray = new ArrayList<>();
            objArray.add(menuDetails.getMenuCode());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.invalid.request.notexist", null, locale, null));
        }
        return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);

    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ApiResultDTO> deleteMenudetails(Long menuId, String loggeduser, String locale) {
        log.info("::MenuManagementServiceImpl::deleteMenuDetails:: {}", menuId);

        commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.DELETE,
                CommonsConstants.MENU_MGMT, null, "Deleted id: " + menuId, loggeduser));

        menuManagementRepository.deleteById(menuId);
        return new ResponseEntity<>(new ApiResultDTO(CommonsConstants.SUCCESS,
                LocaleConfig.getResourceValue("delete.success", null, locale, null)), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<MenuDetails> viewMenuDetails(Long menuId, String loggeduser, String locale) {

        log.info("::MenuManagementServiceImpl::viewMenuDetails:: {}", menuId);

        return new ResponseEntity<MenuDetails>(menuManagementRepository.findByMenuId(menuId), HttpStatus.OK);

    }

    /**
     * 
     */
    @Override
    public ResponseEntity<Page<MenuDetailsSearchResultsDTO>> searchMenuDetails(
            MenuDetailsSearchDTO menuDetailsSearchDTO, String loggeduser, String locale) {
        log.info("::MenuManagementServiceImpl::searchMenuDetails:: {}", menuDetailsSearchDTO.getMenuId());

        return new ResponseEntity<Page<MenuDetailsSearchResultsDTO>>(
                menuManagementRepository.searchMenuDetails(menuDetailsSearchDTO), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<List<MenuDetails>> getActiveMenu(String loggeduser, String locale) {
        log.info("MenuManagementServiceImpl:: getActiveMenu");

        return new ResponseEntity<List<MenuDetails>>(menuManagementRepository.findByStatus("Y"), HttpStatus.OK);
    }

}
