package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ProductConfig;
import com.hypercube.evisa.common.api.model.ProductConfigSearchDTO;


/**
 * @author SivaSreenivas
 *
 */
public interface ProductConfigCustomsRepo {
    
    /**
     * @param productConfigSearchDTO
     * @return
     */
    Page<ProductConfig> searchProductConfig(ProductConfigSearchDTO productConfigSearchDTO);

}
