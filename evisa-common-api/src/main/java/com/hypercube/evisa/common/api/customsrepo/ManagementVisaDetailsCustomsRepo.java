/**
 * 
 */
package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.VisaDetailsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ManagementVisaDetailsCustomsRepo {

    /**
     * @param visaDetailsSearchDTO
     * @return
     */
    Page<ManagementVisaDetails> searchVisaDetails(VisaDetailsSearchDTO visaDetailsSearchDTO);

}
