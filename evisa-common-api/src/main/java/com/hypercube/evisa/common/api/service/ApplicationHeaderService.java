package com.hypercube.evisa.common.api.service;

import java.util.Date;


import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicationHeader;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;



/**
 * @author SivaSreenivas
 *
 */
public interface ApplicationHeaderService {

    /**
     * @param applicationHeaderList
     */
    void saveAllApplicationHeader(List<ApplicationHeader> applicationHeaderList);

    /**
     * @param loggeduser
     * @return
     */
    int checkPendingAllocatedList(String loggeduser);

    /**
     * @param loggeduser
     * @param applicationList
     */
    void allocateSubmittedApplications(String loggeduser, List<String> applicationList);

    /**
     * @param applicationHeader
     */
    void updateApprovalDetails(ApplicationHeader applicationHeader);

    /**
     * @param applicationNumber
     * @return
     */
    ApplicationHeader getHeaderApplication(String applicationNumber);

    /**
     * @param username
     */
    void deallocateApplications(String username);

    /**
     * @param loggeduser
     * @return
     */
    Map<String, Long> decisionMakerDashboard(String loggeduser, String period);

    /**
     * @param genericSearchDTO
     * @return
     */
    Page<AgentTrackerDTO> agentTrackerDetails(GenericSearchDTO genericSearchDTO);

    /**
     * @param reportsSearchDTO
     * @return
     */
    Page<AgentTrackerDTO> performanceReport(ReportsSearchDTO reportsSearchDTO);

    /**
     * @param loggeduser
     * @param role
     * @param oprtype
     * @return
     */
    ReportsSearchDTO getAgentListBasedOnOprTypeAndRole(String loggeduser, String role, String oprtype);

    /**
     * @param reportsSearchDTO
     * @return
     */
    Page<AgentTrackerDTO> processReport(ReportsSearchDTO reportsSearchDTO);

    /**
     * @param loggeduser
     * @param period
     * @return
     */
    AgentTrackerDTO immigrationOfficerDashboard(String loggeduser, String period);

    /**
     * @param applicationlist
     */
    void deallocateSelectedApplications(List<String> applicationlist);

    /**
     * @param agentusername
     * @return
     */
    List<String> fetchApplicationsByLoggedUser(String agentusername);

    /**
     * @param applicationNumber
     * @param oprType
     * @param arrDepId
     */
    void updateArrivalDepartureIndicator(String applicationNumber, String oprType, Long arrDepId);

    /**
     * @param applicationNumber
     * @param oprType
     * @param arrDepId
     * @param visaExpiryDate
     */
    void updateVisaExpiryAlongWithArrivalDtls(String applicationNumber, String oprType, Long arrDepId,
                                              Date visaExpiryDate);

    /**
     * @param referenceNumber
     */
    void updatePaymentDetails(String referenceNumber);

    /**
     * @param applicationNumber
     * @return
     */
    String getEntryTypeByApplicationNumber(String applicationNumber);

    /**
     * @param loggeduser
     * @param decisionMaker
     * @return
     */
    Query getListOfEmployeeReporting(String loggeduser, String decisionMaker);

    /**
     * @param genericSearchDTO
     * @return
     */
    AgentTrackerDTO borderControlDashboardStatistics(GenericSearchDTO genericSearchDTO);

    /**
     * @param genericSearchDTO
     * @return
     */
    List<String> agentTrackerForSBCO(GenericSearchDTO genericSearchDTO);

    /**
     * @param extensionNumber
     * @param flagStatus
     */
    void updateExtensionAppliedFlag(String extensionNumber, String flagStatus);

    /**
     * @param expiryAfterExtension
     * @param applicationNumber
     */
    void updateVisaExpiryAfterExtension(Date expiryAfterExtension, String applicationNumber);

    long overstayStatistics();

    long decisionMakerPendingCount(String docstatus);

    /**
     * Met à jour le statut de l'application après confirmation de paiement
     *
     * @param fileNumber Numéro de dossier
     */
//    void updatePaymentConfirmed(String fileNumber);
}