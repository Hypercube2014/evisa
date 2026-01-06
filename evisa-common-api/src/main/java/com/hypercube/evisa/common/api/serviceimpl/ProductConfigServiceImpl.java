package com.hypercube.evisa.common.api.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.domain.ProductConfig;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ProductConfigDTO;
import com.hypercube.evisa.common.api.model.ProductConfigSearchDTO;
import com.hypercube.evisa.common.api.repository.ProductConfigRepository;
import com.hypercube.evisa.common.api.service.ProductConfigService;
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
public class ProductConfigServiceImpl implements ProductConfigService {

    /**
     * 
     */
    @Autowired(required = true)
    private ProductConfigRepository productConfigRepository;

    /**
     * 
     */
    @Autowired(required = true)
    CommonQueueUtilService commonQueueUtilService;

    /**
     * 
     */
    @Autowired(required = true)
    private ModelMapper modelMapper;

    /**
     *
     */
    @Override
    public String getConfigValueByConfigCode(String configCode) {
        log.info("ProductConfigServiceImpl-getConfigValueByConfigCode");
        return productConfigRepository.getConfigValueByConfigCode(configCode);
    }

    /**
     *
     */
    @Override
    public ProductConfig findByConfigId(Long configId) {
        log.info("ProductConfigServiceImpl-findByConfigId");
        return productConfigRepository.findByConfigId(configId);
    }

    /**
     *
     */
    @Override
    public ApiResultDTO updateProductConfig(ProductConfigDTO productConfigDTO) {
        log.info("ProductConfigServiceImpl-updateProductConfig");

        ApiResultDTO apiResultDTO;

        /* find product config by id */
        ProductConfig productConfig = productConfigRepository.findByConfigId(productConfigDTO.getConfigId());

        if (productConfig == null) {
            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                        CommonsConstants.PRODUCT_MGMT, new ObjectMapper().writeValueAsString(productConfigDTO),
                        "id.notexist", productConfigDTO.getUpdatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("updateProductConfig-JsonProcessingException {}", jpe.getCause());
            }
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "ID Doesn't Exist");
        } else {
            if (productConfig.getConfigCode().equals(productConfigDTO.getConfigCode())) {

                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.PRODUCT_MGMT, new ObjectMapper().writeValueAsString(productConfigDTO),
                            "update.success", productConfigDTO.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("updateProductConfig-JsonProcessingException {}", jpe.getCause());
                }

                productConfigRepository.save(modelMapper.map(productConfigDTO, ProductConfig.class));
                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Updated Details Successfully!");
            } else {
                try {
                    commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.UPDATE,
                            CommonsConstants.PRODUCT_MGMT, new ObjectMapper().writeValueAsString(productConfigDTO),
                            "code.modified", productConfigDTO.getUpdatedBy()));
                } catch (JsonProcessingException jpe) {
                    log.error("updateProductConfig-JsonProcessingException {}", jpe.getCause());
                }
                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "Config Code can't be modified");
            }
        }

        return apiResultDTO;
    }

    /**
     *
     */
    @Override
    public ApiResultDTO createProductConfig(ProductConfigDTO productConfigDTO) {
        log.info("ProductConfigServiceImpl-updateProductConfig");

        ApiResultDTO apiResultDTO;

        /* check config code is unique */
        boolean result = productConfigRepository.existsByConfigCode(productConfigDTO.getConfigCode());

        if (result) {
            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.PRODUCT_MGMT, new ObjectMapper().writeValueAsString(productConfigDTO),
                        "code.exist", productConfigDTO.getCreatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("updateProductConfig-JsonProcessingException {}", jpe.getCause());
            }
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "Code Already Exists");
        } else {
            try {
                commonQueueUtilService.sendAuditDetailsToQueue(new AuditDetails(CommonsConstants.INSERT,
                        CommonsConstants.PRODUCT_MGMT, new ObjectMapper().writeValueAsString(productConfigDTO),
                        "save.success", productConfigDTO.getCreatedBy()));
            } catch (JsonProcessingException jpe) {
                log.error("updateProductConfig-JsonProcessingException {}", jpe.getCause());
            }
            productConfigRepository.save(modelMapper.map(productConfigDTO, ProductConfig.class));
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Saved Details Successfully!");
        }

        return apiResultDTO;
    }

    /**
     *
     */
    @Override
    public Page<ProductConfig> searchProductConfig(ProductConfigSearchDTO productConfigSearchDTO) {
        log.info("ProductConfigServiceImpl-searchProductConfig");
        return productConfigRepository.searchProductConfig(productConfigSearchDTO);
    }

}
