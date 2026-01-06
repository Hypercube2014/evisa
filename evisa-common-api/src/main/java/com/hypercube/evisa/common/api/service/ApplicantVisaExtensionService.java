package com.hypercube.evisa.common.api.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantVisaExtensionService {

    /**
     * @param applicationVisaExtension
     * @return
     */
    ApplicantVisaExtension saveApplicantVisaExtension(ApplicantVisaExtension applicationVisaExtension);
    
    /**
     * @param referenceNumber
     */
    void updatePaymentDetails(String referenceNumber);

    /**
     * @param visaExtensionSearchDTO
     * @return
     */
    Page<ApplicantVisaExtension> searchApplicantVisaExtension(VisaExtensionSearchDTO visaExtensionSearchDTO);

    /**
     * @param extensionId
     * @return
     */
    ApplicantVisaExtension fetchApplicantVisaExtension(String extensionId);

    /**
     * @param loggeduser
     * @return
     */
    int checkExtensionPendingAllocatedList(String loggeduser);

    /**
     * @param b
     * @param of
     * @return
     */
    List<String> pendingExtensionVisaProcessList(PageRequest pageRequest);

    /**
     * @param loggeduser
     * @param extensionVisaAppList
     */
    void allocateSubmittedExtensionApplications(String loggeduser, List<String> extensionVisaAppList);

    /**
     * @param applicationNumber
     * @return
     */
    Date getNewExipredDate(String applicationNumber);

    /**
     * @param loggeduser
     * @param period
     * @return
     */
    Map<String, Long> decisionMakerExtensionDashboard(String loggeduser, String period);

    /**
     * @param applicationNumber
     * @return
     */
    String getExtensionTypeByApplicationNumber(String applicationNumber);

}
