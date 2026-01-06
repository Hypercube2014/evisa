/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.customsrepoimpl.ApplicantTravelHistoryCustomsRepoImpl;
import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.domain.ApplicationFile;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantPassportTravelDetailsDTO;
import com.hypercube.evisa.common.api.repository.ApplicantPassportTravelDetailsRepository;
import com.hypercube.evisa.common.api.repository.ApplicantPersonalDetailsRepository;
import com.hypercube.evisa.common.api.repository.ApplicationFileRepository;
import com.hypercube.evisa.common.api.service.ApplicantPassportTravelDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicantPassportTravelDetailsServiceImpl implements ApplicantPassportTravelDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    ModelMapper modelMapper;

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantPassportTravelDetailsRepository applicantPassportTravelDetailsRepository;

    @Autowired(required = true)
    private ApplicationFileRepository applicationFileRepository;

    @Autowired(required = true)
    private ApplicantPersonalDetailsRepository applicantPersonalDetailsRepository;

    /**
     * 
     */
    @Override
    public ApiResultDTO savePassportTravelDetails(String locale,
            ApplicantPassportTravelDetailsDTO applicantPassportTravelDetailsDTO) {
        log.info("ApplicantPassportTravelDetailsServiceImpl-savePassportTravelDetails");

        System.out.println("expiryyyyy date -------------" + applicantPassportTravelDetailsDTO.getExpiryDate());
        System.out.println(applicantPassportTravelDetailsDTO.toString());

        if (applicantPassportTravelDetailsDTO.getArrivalDate() != null && applicantPassportTravelDetailsDTO.getDepartureDate() != null) {
            java.time.LocalDate arrivalDate = applicantPassportTravelDetailsDTO.getArrivalDate();
            java.time.LocalDate departedDate = applicantPassportTravelDetailsDTO.getDepartureDate();
            ApplicantPersonalDetails appDetails = applicantPersonalDetailsRepository
                    .findByApplicationNumber(applicantPassportTravelDetailsDTO.getApplicationNumber());
            ApplicationFile appFileDetails = applicationFileRepository
                    .findByFileNumber(appDetails.getFileNumber());
            java.time.LocalDate minAllowedDate;
            if (appFileDetails != null && appFileDetails.isExpressVisa()) {
                minAllowedDate = java.time.LocalDate.now().plusDays(1);
            } else {
                minAllowedDate = java.time.LocalDate.now().plusDays(3);
            }
            if (!arrivalDate.isAfter(minAllowedDate)) {
                return new ApiResultDTO(CommonsConstants.ERROR,
                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
            }

            /*java.time.LocalDate maxAllowedDate = arrivalDate.plusDays(30);

            if (departedDate.isAfter(maxAllowedDate)) {
                return new ApiResultDTO(CommonsConstants.ERROR,
                        LocaleConfig.getResourceValue("error.processing", null, locale, null));
            }*/
            
            applicantPassportTravelDetailsRepository
                    .save(modelMapper.map(applicantPassportTravelDetailsDTO, ApplicantPassportTravelDetails.class));
    
            return new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
        } else {
            return new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.processing", null, locale, null));
        }

    }

    /**
     * 
     */
    @Override
    public ApplicantPassportTravelDetails findPassportTravelInfoByApplicantionNumber(String applicationNumber) {
        log.info("ApplicantPassportTravelDetailsServiceImpl-findPassportTravelInfoByApplicantionNumber");
        return applicantPassportTravelDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    /**
     * 
     */
    @Override
    public List<String> findApplications(List<String> applicationList) {
        log.info("ApplicantPassportTravelDetailsServiceImpl-findApplications");
        return applicantPassportTravelDetailsRepository.findApplications(applicationList);
    }

}
