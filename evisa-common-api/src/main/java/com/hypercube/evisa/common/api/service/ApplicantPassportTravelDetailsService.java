/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import java.util.List;

import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantPassportTravelDetailsDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantPassportTravelDetailsService {

    /**
     * @param applicantPassportTravelDetailsDTO
     * @return
     */
    ApiResultDTO savePassportTravelDetails(String locale,
            ApplicantPassportTravelDetailsDTO applicantPassportTravelDetailsDTO);

    /**
     * @param applicationNumber
     * @return
     */
    ApplicantPassportTravelDetails findPassportTravelInfoByApplicantionNumber(String applicationNumber);

    /**
     * @param applicationList
     * @return
     */
    List<String> findApplications(List<String> applicationList);

}
