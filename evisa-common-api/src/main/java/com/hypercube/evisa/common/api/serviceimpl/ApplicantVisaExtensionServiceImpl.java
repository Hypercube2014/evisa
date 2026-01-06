package com.hypercube.evisa.common.api.serviceimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;
import com.hypercube.evisa.common.api.repository.ApplicantVisaExtensionRepository;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionService;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ApplicantVisaExtensionServiceImpl implements ApplicantVisaExtensionService {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVisaExtensionRepository applicantVisaExtensionRepo;

    /**
     *
     */
    @Override
    public ApplicantVisaExtension saveApplicantVisaExtension(ApplicantVisaExtension applicationVisaExtension) {
        log.info("ApplicantVisaExtensionServiceImpl-saveApplicantVisaExtension");
        return applicantVisaExtensionRepo.save(applicationVisaExtension);
    }

    /**
     *
     */
    @Override
    public void updatePaymentDetails(String referenceNumber) {
        log.info("ApplicantVisaExtensionServiceImpl-updatePaymentDetails");
        applicantVisaExtensionRepo.updatePaymentDetails(referenceNumber);
    }

    /**
     *
     */
    @Override
    public Page<ApplicantVisaExtension> searchApplicantVisaExtension(VisaExtensionSearchDTO visaExtensionSearchDTO) {
        log.info("ApplicantVisaExtensionServiceImpl-searchApplicantVisaExtension");
        return applicantVisaExtensionRepo.searchApplicantVisaExtension(visaExtensionSearchDTO);
    }

    /**
     *
     */
    @Override
    public ApplicantVisaExtension fetchApplicantVisaExtension(String extensionId) {
        log.info("ApplicantVisaExtensionServiceImpl-fetchApplicantVisaExtension");
        return applicantVisaExtensionRepo.findByVisaExtensionId(extensionId);
    }

    /**
     *
     */
    @Override
    public int checkExtensionPendingAllocatedList(String loggeduser) {
        log.info("ApplicantVisaExtensionServiceImpl-checkExtensionPendingAllocatedList");
        return applicantVisaExtensionRepo.checkExtensionPendingAllocatedList(loggeduser);
    }

    /**
     *
     */
    @Override
    public List<String> pendingExtensionVisaProcessList(PageRequest pageable) {
        log.info("ApplicantVisaExtensionServiceImpl-pendingExtensionVisaProcessList");
        return applicantVisaExtensionRepo.pendingExtensionVisaProcessList(pageable);
    }

    /**
     *
     */
    @Override
    public void allocateSubmittedExtensionApplications(String loggeduser, List<String> extensionVisaAppList) {
        log.info("ApplicantVisaExtensionServiceImpl-allocateSubmittedExtensionApplications");
        applicantVisaExtensionRepo.allocateSubmittedExtensionApplications(loggeduser, extensionVisaAppList);
    }

    /**
     *
     */
    @Override
    public Date getNewExipredDate(String applicationNumber) {
        log.info("ApplicantVisaExtensionServiceImpl-getNewExipredDate");
        return applicantVisaExtensionRepo.getNewExipredDate(applicationNumber);
    }

    /**
     *
     */
    @Override
    public Map<String, Long> decisionMakerExtensionDashboard(String loggeduser, String period) {
        log.info("ApplicantVisaExtensionServiceImpl-decisionMakerExtensionDashboard");

        Map<String, Long> mapResult = new HashMap<>();
        GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(period);

        long approvedCount = 0;
        long rejectedCount = 0;
        long pendingCount = 0;
        long validationCount = 0;

        /* get the pending extension applications */
        List<CountDTO> countDTOs = applicantVisaExtensionRepo.decisionMakerCount(loggeduser,
                genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());
        
        for (CountDTO countDTO : countDTOs) {

            if ("APR".equals(countDTO.getStatusCode())) {
                approvedCount = countDTO.getCount();
            } else if ("REJ".equals(countDTO.getStatusCode())) {
                rejectedCount = countDTO.getCount();
            } else if ("PEN".equals(countDTO.getStatusCode())) {
                pendingCount = countDTO.getCount();
            } else if ("VAL".equals(countDTO.getStatusCode())) {
                validationCount = countDTO.getCount();
            }
        }

        mapResult.put(CommonsConstants.PENDING, pendingCount);
        mapResult.put(CommonsConstants.CLOSED, approvedCount + rejectedCount);
        mapResult.put(CommonsConstants.REJECTED, rejectedCount);
        mapResult.put(CommonsConstants.VALIDATION, validationCount);

        return mapResult;
    }

    /**
     *
     */
    @Override
    public String getExtensionTypeByApplicationNumber(String applicationNumber) {
        log.info("ApplicantVisaExtensionServiceImpl-getExtensionTypeByApplicationNumber");
        return applicantVisaExtensionRepo.getExtensionTypeByApplicationNumber(applicationNumber);
    }

}
