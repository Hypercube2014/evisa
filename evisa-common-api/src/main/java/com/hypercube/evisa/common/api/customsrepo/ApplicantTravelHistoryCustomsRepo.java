package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;
import com.hypercube.evisa.common.api.model.ApplicantTravelHistorySearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantTravelHistoryCustomsRepo {

    /**
     * @param applTravelHisSearchDTO
     * @return
     */
    Page<ApplicantTravelHistory> searchApplicantTravelHistory(ApplicantTravelHistorySearchDTO applTravelHisSearchDTO);

}
