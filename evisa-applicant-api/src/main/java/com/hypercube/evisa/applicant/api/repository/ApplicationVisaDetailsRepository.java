package com.hypercube.evisa.applicant.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.applicant.api.customsrepo.ApplicationVisaCustomRepo;
import com.hypercube.evisa.applicant.api.domain.ApplicationVisaDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicationVisaDetailsRepository
        extends JpaRepository<ApplicationVisaDetails, String>, ApplicationVisaCustomRepo {

}
