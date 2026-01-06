package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantVisaExtensionCustomsRepo {
    
    /**
     * @param visaExtensionSearchDTO
     * @return
     */
    Page<ApplicantVisaExtension> searchApplicantVisaExtension(VisaExtensionSearchDTO visaExtensionSearchDTO);

}
