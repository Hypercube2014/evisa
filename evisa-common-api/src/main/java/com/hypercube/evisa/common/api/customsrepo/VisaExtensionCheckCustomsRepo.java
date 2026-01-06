package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface VisaExtensionCheckCustomsRepo {

    /**
     * @param visaExtensionCheckSearchDTO
     * @return
     */
    Page<VisaExtensionCheck> searchVisaExtensionCheck(VisaExtensionSearchDTO visaExtensionCheckSearchDTO);

}
