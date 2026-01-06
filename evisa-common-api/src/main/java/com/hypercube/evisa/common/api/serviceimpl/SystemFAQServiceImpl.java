package com.hypercube.evisa.common.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.domain.SystemFAQ;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.SystemFAQPrintDTO;
import com.hypercube.evisa.common.api.model.SystemFAQSearchDTO;
import com.hypercube.evisa.common.api.repository.SystemFAQRepository;
import com.hypercube.evisa.common.api.service.SystemFAQService;
import com.hypercube.evisa.common.api.util.CommonQueueUtilService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class SystemFAQServiceImpl implements SystemFAQService {

    /**
     * 
     */
    @Autowired(required = true)
    private SystemFAQRepository systemFAQRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Override
    public Page<SystemFAQ> searchSystemFaq(String locale, SystemFAQSearchDTO systemFAQSearchDTO) {
        log.info("SystemFAQServiceImpl-searchSystemFaq");
        return systemFAQRepository.searchSystemFaq(locale, systemFAQSearchDTO);
    }

    /**
     * 
     */
    @Override
    public ApiResultDTO saveSystemFaq(String locale, SystemFAQ systemFaq) {
        log.info("SystemFAQServiceImpl-saveSystemFaq");

        try {
            commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                    CommonsConstants.FAQ_MGMT, new ObjectMapper().writeValueAsString(systemFaq), "save.success",
                    systemFaq.getCreatedBy()));
        } catch (JsonProcessingException jpe) {
            log.error("saveSystemFaq-JsonProcessingException {}", jpe.getCause());
        }

        systemFAQRepository.save(systemFaq);

        /* notify to regenerate FAQ file */
        log.info("saveSystemFaq-alerted to update FAQ JSON File");
        commonQueueUtilService.notifyFAQToQueue();

        return new ApiResultDTO(CommonsConstants.SUCCESS,
                LocaleConfig.getResourceValue("save.success", null, locale, null));
    }

    /**
     * 
     */
    @Override
    public SystemFAQ getSystemFaqById(Long faqId) {
        log.info("SystemFAQServiceImpl-getSystemFaqById");
        return systemFAQRepository.findByFaqId(faqId);
    }

    /**
     * 
     */
    @Override
    public ApiResultDTO modifySystemFaq(String locale, SystemFAQ systemFaq) {
        log.info("SystemFAQServiceImpl-modifySystemFaq");
        ApiResultDTO apiResultDTO;

        if (systemFaq.getFaqId() == null) {

            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                        CommonsConstants.FAQ_MGMT, new ObjectMapper().writeValueAsString(systemFaq), "id.mandatory",
                        systemFaq.getUpdatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("modifySystemFaq-JsonProcessingException {}", jpe.getCause());
            }

            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.invalid.request.mandatory", null, locale, null));
        } else {
            boolean result = systemFAQRepository.existsById(systemFaq.getFaqId());

            if (result) {
                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.FAQ_MGMT, new ObjectMapper().writeValueAsString(systemFaq),
                            "update.success", systemFaq.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("modifySystemFaq-JsonProcessingException {}", jpe.getCause());
                }
                systemFAQRepository.save(systemFaq);
                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                        LocaleConfig.getResourceValue("update.success", null, locale, null));

                /* notify to regenerate FAQ file */
                log.info("modifySystemFaq-alerted to update FAQ JSON File");
                commonQueueUtilService.notifyFAQToQueue();
            } else {
                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.FAQ_MGMT, new ObjectMapper().writeValueAsString(systemFaq), "id.notexist",
                            systemFaq.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("modifySystemFaq-JsonProcessingException {}", jpe.getCause());
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
    public List<SystemFAQPrintDTO> findAllActiveFAQ(String status) {
        log.info("SystemFAQServiceImpl-findAllActiveFAQ");
        return systemFAQRepository.findByStatus(status);
    }

}
