package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.ApplicantUserLoginHistory;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantUserLoginHistoryService {

    /**
     * @param userLoginHistory
     * @return
     */
    ApplicantUserLoginHistory saveUserLoginHistory(ApplicantUserLoginHistory userLoginHistory);
}
