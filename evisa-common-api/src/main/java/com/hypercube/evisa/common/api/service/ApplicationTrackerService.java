package com.hypercube.evisa.common.api.service;

import java.time.LocalDate;
import java.util.Date;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.AgeStatisticsDTO;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.ApplicationSearchTrackerDTO;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardRequestDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicationTrackerService {

    /**
     * @param applicationSearchTrackerDTO
     * @return
     */
    Page<ApplicationTracker> searchApplicationInTracker(ApplicationSearchTrackerDTO applicationSearchTrackerDTO);
    
    /**
     * @param applicationSearchTrackerDTO
     * @return
     */
    Page<ApplicationTracker> pendingFileNumberForProcessing(ApplicationSearchTrackerDTO applicationSearchTrackerDTO);

    /**
     * @param isExpressVisa
     * @param limit
     * @return
     */
    List<String> pendingVisaProcessList(boolean isExpressVisa, Pageable pageable);

    /**
     * @param applicationNumber
     * @return
     */
    int getVisaDuration(String applicationNumber);

    LocalDate getArrivalDate(String applicationNumber);

    /**
     * @param applicationSearchTrackerDTO
     * @return
     */
    Page<ApplicationTracker> searchApplicationsForArrivalDeparture(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO);

    /**
     * @param applicationNumber
     * @return
     */
    ApplicationTracker getApplicationDetails(String applicationNumber);

    /**
     * @param subsDate
     * @param countryStats
     * @return
     */
    List<CountDTO> applicationProcessStatistics(Date subsDate, List<String> countryStats);

    /**
     * @param countries
     * @param startDate
     * @param endDate
     * @return
     */
    List<CountDTO> countryStatistics(List<String> countries, Date startDate, Date endDate);
    
    

    /**
     * @param countryStats
     * @param startDate
     * @param endDate
     * @return
     */
    List<CountDTO> travelPurposeStatistics(List<String> countryStats, Date startDate, Date endDate);

    /**
     * @param countryStats
     * @param startDate
     * @param endDate
     * @return
     */
    AgeStatisticsDTO ageStatistics(List<String> countryStats, Date startDate, Date endDate);

    /**
     * @param applicationNumberList
     * @param genericSearchDTO
     * @return
     */
    Page<ApplicationTracker> searchApplicationsForBCO(List<String> applicationNumberList,
            GenericSearchDTO genericSearchDTO);

    /**
     * @param managerDashboardDTO
     * @return
     */
    AgentTrackerDTO immigrationOfficerCount(ManagerDashboardRequestDTO managerDashboardDTO);

    /**
     * @param applicationNumber
     * @return
     */
    String getApproverRemarks(String applicationNumber);


    /**
     * @param startDates
     * @param endDates
     * @return
     */
	AgeStatisticsDTO ageStatisticsAll(Date startDates, Date endDates);


    /**
     * @param startDatess
     * @param endDatess
     * @return
     */
	List<CountDTO> top5countryStatistics(Date startDatess, Date endDatess);

}
