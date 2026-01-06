package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.VisaExtensionCheckCustomsRepo;
import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface VisaExtensionCheckRepository
        extends JpaRepository<VisaExtensionCheck, String>, VisaExtensionCheckCustomsRepo {
	
	

}
