/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.VisaDetailsSearchDTO;
import com.hypercube.evisa.common.api.repository.ManagementVisaDetailsRepository;
import com.hypercube.evisa.common.api.service.ManagementVisaDetailsService;
import com.hypercube.evisa.common.api.util.CommonQueueUtilService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
@Service
public class ManagementVisaDetailsServiceImpl implements ManagementVisaDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    private ManagementVisaDetailsRepository managementVisaDetailsRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Override
    public ApiResultDTO createVisaDetails(String locale, ManagementVisaDetails managementVisaDetails) {
        log.info("ManagementVisaDetailsServiceImpl-createVisaDetails");
        boolean result = managementVisaDetailsRepository.existsByVisaType(managementVisaDetails.getVisaType());

        ApiResultDTO apiResultDTO;
        if (result) {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.VISA_MGMT, new ObjectMapper().writeValueAsString(managementVisaDetails),
                        "visatype.already.exists", managementVisaDetails.getCreatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("createVisaDetails-JsonProcessingException {}", jpe.getCause());
            }

            List<Object> objArray = new ArrayList<>();
            objArray.add(managementVisaDetails.getVisaType());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.VISA_MGMT, new ObjectMapper().writeValueAsString(managementVisaDetails),
                        "save.success", managementVisaDetails.getCreatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("createVisaDetails-JsonProcessingException {}", jpe.getCause());
            }

            managementVisaDetailsRepository.save(managementVisaDetails);
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
        }

        return apiResultDTO;
    }

    /**
     * 
     */
    @Override
    public ApiResultDTO modifyVisaDetails(String locale, ManagementVisaDetails managementVisaDetails) {
        log.info("ManagementVisaDetailsServiceImpl-modifyVisaDetails");
        ApiResultDTO apiResultDTO;
        if (managementVisaDetails.getVisaId() == null) {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                        CommonsConstants.VISA_MGMT, new ObjectMapper().writeValueAsString(managementVisaDetails),
                        "id.mandatory", managementVisaDetails.getUpdatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("modifyVisaDetails-JsonProcessingException {}", jpe.getCause());
            }

            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.invalid.request.mandatory", null, locale, null));
        } else {
            if (managementVisaDetailsRepository.existsById(managementVisaDetails.getVisaId())) {

                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.VISA_MGMT, new ObjectMapper().writeValueAsString(managementVisaDetails),
                            "update.success", managementVisaDetails.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("modifyVisaDetails-JsonProcessingException {}", jpe.getCause());
                }

                managementVisaDetailsRepository.save(managementVisaDetails);

                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                        LocaleConfig.getResourceValue("update.success", null, locale, null));
            } else {

                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.VISA_MGMT, new ObjectMapper().writeValueAsString(managementVisaDetails),
                            "id.notexist", managementVisaDetails.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("modifyVisaDetails-JsonProcessingException {}", jpe.getCause());
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
    public List<MasterCodeResultDTO> getActiveVisaTypes(String status) {
        log.info("ManagementVisaDetailsServiceImpl-getActiveVisaTypes");
        return managementVisaDetailsRepository.findByStatus(status);
    }

    /**
     * 
     */
    @Override
    public List<MasterCodeResultDTO> getActiveVisaTypeAndExpressVisa(boolean expressVisa, String status) {
        log.info("ManagementVisaDetailsServiceImpl-getActiveVisaTypeAndExpressVisa");
        return managementVisaDetailsRepository.findByExpressVisaAndStatus(expressVisa, status);
    }

    /**
     * 
     */
    @Override
    public MasterCodeResultDTO getVisaDetailsByVisaType(String visaType) {
        log.info("ManagementVisaDetailsServiceImpl-getVisaDetailsByVisaType");
        return managementVisaDetailsRepository.getVisaDetailsByVisaType(visaType);
    }

    /**
     * 
     */
    @Override
    public ManagementVisaDetails getVisaCompleteDetailsByVisaType(String visaType) {
        log.info("ManagementVisaDetailsServiceImpl-getVisaCompleteDetailsByVisaType");
        return managementVisaDetailsRepository.findByVisaType(visaType);
    }

    /**
     * 
     */
    @Override
    public ManagementVisaDetails findVisaDetailsById(Long visaId) {
        log.info("ManagementVisaDetailsServiceImpl-findVisaDetailsById");
        return managementVisaDetailsRepository.findByVisaId(visaId);
    }

    /**
     * 
     */
    @Override
    public Page<ManagementVisaDetails> searchVisaDetails(String locale, VisaDetailsSearchDTO visaDetailsSearchDTO) {
        log.info("ManagementVisaDetailsServiceImpl-searchVisaDetails");
        return managementVisaDetailsRepository.searchVisaDetails(visaDetailsSearchDTO);
    }

    /**
     *
     */
    @Override
    public List<MasterCodeResultDTO> findExtensionVisaTypes() {
        log.info("ManagementVisaDetailsServiceImpl-findExtensionVisaTypes");
        return managementVisaDetailsRepository.findActiveExtensionVisaTypes();
    }

}
