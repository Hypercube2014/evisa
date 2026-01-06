package com.hypercube.evisa.common.api.service;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;
import com.hypercube.evisa.common.api.model.ApplicantTravelHistorySearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantTravelHistoryServiceFacade {

    /**
     * @param applTravelHistSearchDTO
     * @return
     */
    Page<ApplicantTravelHistory> searchApplicantTravelHistory(ApplicantTravelHistorySearchDTO applTravelHistSearchDTO);

}
