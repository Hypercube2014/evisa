package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ApplicantTravelHistoryCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantTravelHistoryRepository
        extends JpaRepository<ApplicantTravelHistory, Long>, ApplicantTravelHistoryCustomsRepo {

}
