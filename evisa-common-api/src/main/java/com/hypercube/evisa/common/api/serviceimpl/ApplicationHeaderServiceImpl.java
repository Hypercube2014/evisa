package com.hypercube.evisa.common.api.serviceimpl;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicationHeader;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.DMDashboardDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;
import com.hypercube.evisa.common.api.repository.ApplicationHeaderRepository;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ApplicationHeaderServiceImpl implements ApplicationHeaderService {

    /**
     *
     */
    @Autowired(required = true)
    private ApplicationHeaderRepository applicationHeaderRepository;

    /**
     * 
     */
    @Override
    public void saveAllApplicationHeader(List<ApplicationHeader> applicationHeaderList) {
        log.info("ApplicationHeaderServiceImpl-saveAllApplicationHeader");
        applicationHeaderRepository.saveAll(applicationHeaderList);
    }

    /**
     * 
     */
    @Override
    public int checkPendingAllocatedList(String loggeduser) {
        log.info("ApplicationHeaderServiceImpl-checkPendingAllocatedList");
        return applicationHeaderRepository.checkPendingAllocatedList(loggeduser);
    }

    /**
     * 
     */
    @Override
    public void allocateSubmittedApplications(String loggeduser, List<String> applicationList) {
        log.info("ApplicationHeaderServiceImpl-allocateSubmittedApplications");
        applicationHeaderRepository.allocateSubmittedApplications(loggeduser, applicationList);
    }

    /**
     *
     */
    @Override
    public void updateApprovalDetails(ApplicationHeader applicationHeader) {
        log.info("ApplicationHeaderServiceImpl-updateApprovalDetails");
        applicationHeaderRepository.save(applicationHeader);
    }

    /**
     *
     */
    @Override
    public ApplicationHeader getHeaderApplication(String applicationNumber) {
        log.info("ApplicationHeaderServiceImpl-getHeaderApplication");
        return applicationHeaderRepository.getOne(applicationNumber);
    }

    /**
     *
     */
    @Override
    public void deallocateApplications(String username) {
        log.info("ApplicationHeaderServiceImpl-deallocateApplications");
        applicationHeaderRepository.deallocateApplications(username);
    }

    /**
     *
     */
    @Override
    public Map<String, Long> decisionMakerDashboard(String loggeduser, String period) {
        log.info("ApplicationHeaderServiceImpl-decisionMakerDashboard");

        Map<String, Long> mapResult = new HashMap<>();
        GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(period);

        long allocateCount = 0;
        long pendingCount = 0;
        long approvedCount = 0;
        long rejectedCount = 0;
        long validationCount = 0;

        /* /* get the DM applications dashboard count
        List<CountDTO> countDTOs = applicationHeaderRepository.decisionMakerCount(loggeduser,
                genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate()); */

        
        // get the DM applications dashboard allocated count
         List<DMDashboardDTO> allocatedDTO =
         applicationHeaderRepository.decisionMakerCount(loggeduser, genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());
         log.info("ApplicationHeaderServiceImpl-decisionMakerDashboard Merde " + allocatedDTO.toString());
        /* for (CountDTO countDTO : countDTOs) {

            allocateCount = countDTO.getCount();

            if ("CLS".equals(countDTO.getDocumentStatusCode()) && "APR".equals(countDTO.getStatusCode())) {
                approvedCount = countDTO.getCount();
                System.out.println("APR COUNT" + approvedCount);
            } else if ("CLS".equals(countDTO.getDocumentStatusCode()) && "REJ".equals(countDTO.getStatusCode())) {
                rejectedCount = countDTO.getCount();
                System.out.println("REJ COUNT" + rejectedCount);
            } else if ("PEN".equals(countDTO.getDocumentStatusCode()) && "UP".equals(countDTO.getStatusCode())) {
                pendingCount = countDTO.getCount();
                System.out.println("PEN COUNT" + pendingCount);
            } else if ("VAL".equals(countDTO.getDocumentStatusCode()) && "UP".equals(countDTO.getStatusCode())) {
                validationCount = countDTO.getCount();
                System.out.println("VAL COUNT" + validationCount);
            }
        } */

        allocateCount = allocatedDTO.size() > 0 ? allocatedDTO.get(0).getAllocated() : 0;
        pendingCount = allocatedDTO.size() > 0  ? allocatedDTO.get(0).getPending() : 0;
        approvedCount = allocatedDTO.size() > 0 ? allocatedDTO.get(0).getApproved() : 0;
        rejectedCount = allocatedDTO.size() > 0  ? allocatedDTO.get(0).getRejected() : 0;
        validationCount = allocatedDTO.size() > 0  ? allocatedDTO.get(0).getValidation() : 0;

        mapResult.put(CommonsConstants.ALLOCATED, allocateCount);
        mapResult.put(CommonsConstants.PENDING, pendingCount);
        mapResult.put(CommonsConstants.APPROVED, approvedCount);
        /*mapResult.put(CommonsConstants.CLOSED, approvedCount + rejectedCount);*/
        mapResult.put(CommonsConstants.REJECTED, rejectedCount);
        mapResult.put(CommonsConstants.VALIDATION, validationCount);

        return mapResult;
    }

    /**
     *
     */
    @Override
    public Page<AgentTrackerDTO> agentTrackerDetails(GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderServiceImpl-agentTrackerDetails");
        return applicationHeaderRepository.agentTrackerDetails(genericSearchDTO);
    }

    /**
     *
     */
    @Override
    public Page<AgentTrackerDTO> performanceReport(ReportsSearchDTO reportsSearchDTO) {
        log.info("ApplicationHeaderServiceImpl-performanceReport");
        return applicationHeaderRepository.performanceReport(reportsSearchDTO);
    }

    /**
     *
     */
    @Override
    public ReportsSearchDTO getAgentListBasedOnOprTypeAndRole(String loggeduser, String role, String oprtype) {
        log.info("ApplicationHeaderServiceImpl-getAgentListBasedOnOprTypeAndRole");
        return applicationHeaderRepository.getAgentListBasedOnOprTypeAndRole(loggeduser, role, oprtype);
    }

    /**
     *
     */
    @Override
    public Page<AgentTrackerDTO> processReport(ReportsSearchDTO reportsSearchDTO) {
        log.info("ApplicationHeaderServiceImpl-processReport");
        return applicationHeaderRepository.processReport(reportsSearchDTO);
    }

    /**
     *
     */
    @Override
    public AgentTrackerDTO immigrationOfficerDashboard(String loggeduser, String period) {
        log.info("ApplicationHeaderServiceImpl-immigrationOfficerDashboard");
        return applicationHeaderRepository.immigrationOfficerDashboard(loggeduser, period);
    }

    @Override
    @Transactional
    public void deallocateSelectedApplications(List<String> applicationlist) {
        log.info("ApplicationHeaderServiceImpl-immigrationOfficerDashboard");
        applicationHeaderRepository.deallocateSelectedApplications(applicationlist);

    }

    /**
     *
     */
    @Override
    public List<String> fetchApplicationsByLoggedUser(String agentusername) {
        log.info("ApplicationHeaderServiceImpl-fetchApplicationsByLoggedUser");
        return applicationHeaderRepository.deallocateSelectedApplications(agentusername);
    }

    /**
    *
    */
    @Override
    public void updateArrivalDepartureIndicator(String applicationNumber, String oprType, Long arrDepId) {
        log.info("ApplicationHeaderServiceImpl-updateArrivalDepartureIndicator");
        applicationHeaderRepository.updateArrivalDepartureIndicator(applicationNumber, oprType, arrDepId);
    }

    /**
     *
     */
    @Override
    @Transactional
    public void updatePaymentDetails(String referenceNumber) {
        log.info("ApplicationHeaderServiceImpl-updatePaymentDetails");
        applicationHeaderRepository.updatePaymentDetails(referenceNumber);
    }

    /**
     *
     */
    @Override
    public String getEntryTypeByApplicationNumber(String applicationNumber) {
        log.info("ApplicationHeaderServiceImpl-getEntryTypeByApplicationNumber");
        return applicationHeaderRepository.getEntryTypeByApplicationNumber(applicationNumber);
    }

    /**
     *
     */
    @Override
    public Query getListOfEmployeeReporting(String loggeduser, String decisionMaker) {
        log.info("ApplicationHeaderServiceImpl-getListOfEmployeeReporting");
        return applicationHeaderRepository.getListOfEmployeeReporting(loggeduser, decisionMaker);
    }

    /**
     *
     */
    @Override
    public AgentTrackerDTO borderControlDashboardStatistics(GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderServiceImpl-borderControlDashboardStatistics");
        return applicationHeaderRepository.borderControlDashboardStatistics(genericSearchDTO);
    }

    public long overstayStatistics() {
        log.info("ApplicationHeaderServiceImpl-OverstayStatistics");
        return applicationHeaderRepository.overstayStatistics();
    }

    /**
     *
     */
    @Override
    public List<String> agentTrackerForSBCO(GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderServiceImpl-agentTrackerForSBCO");

        return applicationHeaderRepository.agentTrackerForSBCO(genericSearchDTO);
    }

    /**
     *
     */
    @Override
    public void updateExtensionAppliedFlag(String extensionNumber, String flagStatus) {
        log.info("ApplicationHeaderServiceImpl-updateExtensionAppliedFlag");
        applicationHeaderRepository.updateExtensionAppliedFlag(extensionNumber, flagStatus);
    }

    /**
     *
     */
    @Override
    public void updateVisaExpiryAlongWithArrivalDtls(String applicationNumber, String oprType, Long arrDepId,
            Date visaExpiryDate) {
        log.info("ApplicationHeaderServiceImpl-updateVisaExpiryAlongWithArrivalDtls");
        applicationHeaderRepository.updateVisaExpiryAlongWithArrivalDtls(applicationNumber, oprType, arrDepId,
                visaExpiryDate);
    }

    /**
     *
     */
    @Override
    public void updateVisaExpiryAfterExtension(Date expiryAfterExtension, String applicationNumber) {
        log.info("ApplicationHeaderServiceImpl-updateVisaExpiryAfterExtension");
        applicationHeaderRepository.updateVisaExpiryAfterExtension(expiryAfterExtension, applicationNumber);
    }

    @Override
    public long decisionMakerPendingCount(String docstatus) {
        log.info("ApplicationHeaderServiceImpl-decisionMakerPendingCount");
        return applicationHeaderRepository.decisionMakerPendingCount(docstatus);
    }

    /**
     * Met à jour le statut de l'application après confirmation de paiement
     */
//    @Override
//    @Transactional
//    public void updatePaymentConfirmed(String fileNumber) {
//        log.info("ApplicationHeaderServiceImpl-updatePaymentConfirmed pour fileNumber: {}", fileNumber);
//        applicationHeaderRepository.updatePaymentDetails(fileNumber);
//        log.info("Statut application mis à jour - documentStatus: SUB, visaStatus: UP pour fileNumber: {}", fileNumber);
//    }

}
