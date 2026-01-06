package com.hypercube.evisa.applicant.api.service;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.applicant.api.domain.ApplicationVisaDetails;
import com.hypercube.evisa.applicant.api.model.ApplicationVisaSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicationVisaDetailsService {

    /**
     * @param applicationVisaSearchDTO
     * @return
     */
    Page<ApplicationVisaDetails> searchDraftApplicationFile(ApplicationVisaSearchDTO applicationVisaSearchDTO);
    
}
