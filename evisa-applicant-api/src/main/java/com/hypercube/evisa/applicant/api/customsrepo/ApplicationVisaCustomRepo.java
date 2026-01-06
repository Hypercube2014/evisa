package com.hypercube.evisa.applicant.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.applicant.api.domain.ApplicationVisaDetails;
import com.hypercube.evisa.applicant.api.model.ApplicationVisaSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicationVisaCustomRepo {
    
    /**
     * @param applicationVisaSearchDTO
     * @return
     */
    Page<ApplicationVisaDetails> searchApplicationVisa(ApplicationVisaSearchDTO applicationVisaSearchDTO);

}
