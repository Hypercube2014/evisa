package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionHistory;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantVisaExtensionHistoryService {

    /**
     * @param applicantVisaExtensionHistory
     * @return
     */
    ApplicantVisaExtensionHistory saveApplicantVisaExtensionHistory(
            ApplicantVisaExtensionHistory applicantVisaExtensionHistory);

}
