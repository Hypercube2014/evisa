package com.hypercube.evisa.common.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;
import com.hypercube.evisa.common.api.model.ApplicantTravelHistorySearchDTO;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantTravelHistoryService;
import com.hypercube.evisa.common.api.service.ApplicantTravelHistoryServiceFacade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ApplicantTravelHistoryServiceFacadeImpl implements ApplicantTravelHistoryServiceFacade {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantTravelHistoryService applTravelHistService;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantPersonalDetailsService applPersonalDtlsService;

    /**
     *
     */
    @Override
    public Page<ApplicantTravelHistory> searchApplicantTravelHistory(
            ApplicantTravelHistorySearchDTO applTravelHistSearchDTO) {
        log.info("ApplicantTravelHistoryServiceFacadeImpl-searchApplicantTravelHistory");

        ApplicantPersonalDetails applPersonalDtls = applPersonalDtlsService
                .findByApplicationNumber(applTravelHistSearchDTO.getApplicationNumber());

        applTravelHistSearchDTO.setBirthCountry(applPersonalDtls.getBirthCountry());
        applTravelHistSearchDTO.setDob(applPersonalDtls.getDateOfBirth());
        applTravelHistSearchDTO.setGivenName(applPersonalDtls.getGivenName());
        applTravelHistSearchDTO.setSurname(applPersonalDtls.getSurname());
        applTravelHistSearchDTO.setGender(applPersonalDtls.getGender());
        applTravelHistSearchDTO.setSubmittedDate(applPersonalDtls.getCreatedDate());

        return applTravelHistService.searchApplicantTravelHistory(applTravelHistSearchDTO);
    }

}
