package com.hypercube.evisa.common.api.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.domain.MasterCodeDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;
import com.hypercube.evisa.common.api.repository.MasterCodeDetailsRepository;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
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
public class MasterCodeDetailsServiceImpl implements MasterCodeDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    private MasterCodeDetailsRepository masterCodeDetailsRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Override
    public ApiResultDTO createMasterCode(String locale, MasterCodeDetails masterCodeDetails) {
        log.info("MasterCodeDetailsServiceImpl-createMasterCode");

        boolean result = masterCodeDetailsRepository.existsByCodeAndCodeType(masterCodeDetails.getCode(),
                masterCodeDetails.getCodeType());
        ApiResultDTO apiResultDTO;
        if (result) {
            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.MASTER_MGMT, new ObjectMapper().writeValueAsString(masterCodeDetails),
                        "code.already.exists", masterCodeDetails.getCreatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("createMasterCode-JsonProcessingException {}", jpe.getCause());
            }
            List<Object> objArray = new ArrayList<>();
            objArray.add(masterCodeDetails.getCode());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.MASTER_MGMT, new ObjectMapper().writeValueAsString(masterCodeDetails),
                        "save.success", masterCodeDetails.getCreatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("createMasterCode-JsonProcessingException {}", jpe.getCause());
            }

            masterCodeDetailsRepository.save(masterCodeDetails);
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
        }

        return apiResultDTO;
    }

    /**
     * 
     */
    @Override
    public MasterCodeResultDTO findMasterCodesByCodeTypeAndCode(String codeType, String code) {
        log.info("MasterCodeDetailsServiceImpl-findMasterCodesByCodeTypeAndCode");
        return masterCodeDetailsRepository.findByCodeTypeAndCode(codeType, code);
    }

    /**
     * 
     */
    @Override
    public Optional<MasterCodeDetails> findMasterCodeDetailsById(Long id) {
        log.info("MasterCodeDetailsServiceImpl-findMasterCodeDetailsById");
        return masterCodeDetailsRepository.findById(id);
    }

    /**
     * 
     */
    @Override
    public List<MasterCodeResultDTO> findActiveMasterCodesByCodeType(String codeType, String active) {
        log.info("MasterCodeDetailsServiceImpl-findActiveMasterCodesByCodeType");
        return masterCodeDetailsRepository.findByCodeTypeAndActive(codeType, active);
    }

    /**
     * 
     */
    @Override
    public Page<MasterCodeDetails> searchMasterCodes(String locale, MasterCodeSearchDTO masterCodeSearchDTO) {
        log.info("MasterCodeDetailsServiceImpl-searchMasterCodes");
        return masterCodeDetailsRepository.searchMasterCodes(locale, masterCodeSearchDTO);
    }

    /**
     * 
     */
    @Override
    public ApiResultDTO modifyMasterCode(String locale, MasterCodeDetails masterCodeDetails) {
        log.info("MasterCodeDetailsServiceImpl-modifyMasterCode");

        ApiResultDTO apiResultDTO;
        if (masterCodeDetails.getId() == null) {
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.invalid.request.mandatory", null, locale, null));
        } else {
            if (masterCodeDetailsRepository.existsById(masterCodeDetails.getId())) {

                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.MASTER_MGMT, new ObjectMapper().writeValueAsString(masterCodeDetails),
                            "update.success", masterCodeDetails.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("modifyMasterCode-JsonProcessingException {}", jpe.getCause());
                }

                masterCodeDetailsRepository.save(masterCodeDetails);

                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                        LocaleConfig.getResourceValue("update.success", null, locale, null));
            } else {
                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.MASTER_MGMT, new ObjectMapper().writeValueAsString(masterCodeDetails),
                            "id.notexist", masterCodeDetails.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("modifyMasterCode-JsonProcessingException {}", jpe.getCause());
                }
                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                        LocaleConfig.getResourceValue("error.invalid.request.notexist", null, locale, null));
            }
        }

        return apiResultDTO;
    }

    /**
     *
     */
    @Override
    public List<MasterCodeResultDTO> getMasterCodeLists(List<String> codeList) {
        log.info("MasterCodeDetailsServiceImpl-getMasterCodeLists");
        return masterCodeDetailsRepository.getMasterCodeLists(codeList);
    }

}
