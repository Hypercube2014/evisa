package com.hypercube.evisa.common.api.service;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ProductConfig;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ProductConfigDTO;
import com.hypercube.evisa.common.api.model.ProductConfigSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ProductConfigService {
    
    /**
     * @param configCode
     * @return
     */
    String getConfigValueByConfigCode(String configCode);
    
    /**
     * @param configCode
     * @return
     */
    ProductConfig findByConfigId(Long configId);

    /**
     * @param productConfigDTO
     * @return
     */
    ApiResultDTO updateProductConfig(ProductConfigDTO productConfigDTO);

    /**
     * @param productConfigDTO
     * @return
     */
    ApiResultDTO createProductConfig(ProductConfigDTO productConfigDTO);

    /**
     * @param productConfigSearchDTO
     * @return
     */
    Page<ProductConfig> searchProductConfig(ProductConfigSearchDTO productConfigSearchDTO);

}
