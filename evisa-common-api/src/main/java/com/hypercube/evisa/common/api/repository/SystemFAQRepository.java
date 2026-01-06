/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.SystemFAQCustomsRepo;
import com.hypercube.evisa.common.api.domain.SystemFAQ;
import com.hypercube.evisa.common.api.model.SystemFAQPrintDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface SystemFAQRepository extends JpaRepository<SystemFAQ, Long>, SystemFAQCustomsRepo {

    /**
     * @param faqId
     * @return
     */
    SystemFAQ findByFaqId(Long faqId);

    /**
     * @param yes
     * @return
     */
    List<SystemFAQPrintDTO> findByStatus(String status);

}
