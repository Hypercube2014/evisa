/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsDTO;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantPersonalDetailsService {

    /**
     * @param applicantPersonalDetailsDTO
     * @return
     */
    ApiResultDTO saveApplicantPersonalDetails(String locale,
            ApplicantPersonalDetailsDTO applicantPersonalDetailsDTO);

    /**
     * @param applicationNumber
     * @return
     */
    ApplicantPersonalDetails findByApplicationNumber(String applicationNumber);

    /**
     * @param applicantPersonalDetailsSearchDTO
     * @return
     */
    Page<ApplicantPersonalDetailsSearchDTO> searchApplications(
            ApplicantPersonalDetailsSearchDTO applicantPersonalDetailsSearchDTO);

    /**
     * @param fileNumber
     * @return
     */
    List<String> findApplications(String fileNumber);

    /**
     * @param fileNumber
     * @return
     */
    String findUsername(String fileNumber);

    /**
     * @param applicationNumber
     * @return
     */
    String findFileNumber(String applicationNumber);   

}
