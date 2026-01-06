package com.hypercube.evisa.applicant.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.applicant.api.domain.ApplicationVisaDetails;
import com.hypercube.evisa.applicant.api.model.ApplicationVisaSearchDTO;
import com.hypercube.evisa.applicant.api.repository.ApplicationVisaDetailsRepository;
import com.hypercube.evisa.applicant.api.service.ApplicationVisaDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicationVisaDetailsServiceImpl implements ApplicationVisaDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    ApplicationVisaDetailsRepository applicationVisaDetailsRepository;

    /**
     * 
     */
    @Override
    public Page<ApplicationVisaDetails> searchDraftApplicationFile(ApplicationVisaSearchDTO applicationVisaSearchDTO) {
        log.info("ApplicationVisaDetailsServiceImpl-searchDraftApplicationFile");
        return applicationVisaDetailsRepository.searchApplicationVisa(applicationVisaSearchDTO);
    }

}
