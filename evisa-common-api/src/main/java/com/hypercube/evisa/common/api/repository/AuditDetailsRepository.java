package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.AuditDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface AuditDetailsRepository extends JpaRepository<AuditDetails, Long> {
    
    /**
     * @param auditId
     * @return
     */
    AuditDetails findByAuditId(Long auditId);

}
