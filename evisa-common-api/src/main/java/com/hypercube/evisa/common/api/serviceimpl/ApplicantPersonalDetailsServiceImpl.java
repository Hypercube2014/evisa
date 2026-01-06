/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsDTO;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsSearchDTO;
import com.hypercube.evisa.common.api.repository.ApplicantPersonalDetailsRepository;
import com.hypercube.evisa.common.api.service.ApplicantPersonalDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ApplicantPersonalDetailsServiceImpl implements ApplicantPersonalDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    ModelMapper modelMapper;

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantPersonalDetailsRepository applicantPersonalDetailsRepository;

    /**
     * 
     */
    @Override
    public ApiResultDTO saveApplicantPersonalDetails(String locale,
            ApplicantPersonalDetailsDTO applicantPersonalDetailsDTO) {
        log.info("ApplicantPersonalDetailsServiceImpl-saveApplicantPersonalDetails");

        ApplicantPersonalDetails applicantPersonalDetails = applicantPersonalDetailsRepository
                .save(modelMapper.map(applicantPersonalDetailsDTO, ApplicantPersonalDetails.class));
        return 
                new ApiResultDTO(CommonsConstants.SUCCESS, LocaleConfig.getResourceValue("save.success", null, locale, null),
                        applicantPersonalDetails.getApplicationNumber());
    }

    /**
     * 
     */
    @Override
    public ApplicantPersonalDetails findByApplicationNumber(String applicationNumber) {
        log.info("ApplicantPersonalDetailsServiceImpl-findByApplicationNumber");
        return applicantPersonalDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    /**
     * 
     */
    @Override
    public Page<ApplicantPersonalDetailsSearchDTO> searchApplications(
            ApplicantPersonalDetailsSearchDTO applicantPersonalDetailsSearchDTO) {
        log.info("ApplicantPersonalDetailsServiceImpl-searchApplications");
        return applicantPersonalDetailsRepository.searchApplications(applicantPersonalDetailsSearchDTO);
    }

    /**
     * 
     */
    @Override
    public List<String> findApplications(String fileNumber) {
        log.info("ApplicantPersonalDetailsServiceImpl-findApplications");
        return applicantPersonalDetailsRepository.findApplications(fileNumber);
    }

    /**
     * 
     */
    @Override
    public String findUsername(String fileNumber) {
        log.info("ApplicantPersonalDetailsServiceImpl-findUsername");
        return applicantPersonalDetailsRepository.findUsername(fileNumber);
    }

    /**
     * 
     */
    @Override
    public String findFileNumber(String applicationNumber) {
        log.info("ApplicantPersonalDetailsServiceImpl-findUsername");
        return applicantPersonalDetailsRepository.findFileNumber(applicationNumber);
    }

}
