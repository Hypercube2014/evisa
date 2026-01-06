package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.repository.AuditDetailsRepository;
import com.hypercube.evisa.common.api.service.AuditDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class AuditDetailsServiceImpl implements AuditDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    AuditDetailsRepository auditDetailsRepository;

    /**
     *
     */
    @Override
    public AuditDetails saveAuditDetails(AuditDetails auditDetails) {
        log.info("::AuditDetailsServiceImpl::saveAuditDetails::");
        return auditDetailsRepository.save(auditDetails);
    }

}
