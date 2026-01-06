package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.AuditDetails;

/**
 * 
 * @author SivaSreenivas
 *
 */
public interface AuditDetailsService {

    /**
     * @param auditDetails
     * @return
     */
    AuditDetails saveAuditDetails(AuditDetails auditDetails);

}
