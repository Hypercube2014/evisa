package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;
import com.hypercube.evisa.common.api.repository.VisaExtensionCheckRepository;
import com.hypercube.evisa.common.api.service.VisaExtensionCheckService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class VisaExtensionCheckServiceImpl implements VisaExtensionCheckService {
    
    /**
     * 
     */
    @Autowired(required = true)
    private VisaExtensionCheckRepository visaExtensionCheckRepository;

    /**
     *
     */
    @Override
    public Page<VisaExtensionCheck> searchVisaExtensionCheck(VisaExtensionSearchDTO visaExtensionCheckSearchDTO) {
        log.info("VisaExtensionCheckServiceImpl-searchVisaExtensionCheck");
        
        System.out.println(visaExtensionCheckRepository.searchVisaExtensionCheck(visaExtensionCheckSearchDTO));
        return visaExtensionCheckRepository.searchVisaExtensionCheck(visaExtensionCheckSearchDTO);
    }

}
