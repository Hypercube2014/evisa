package com.hypercube.evisa.common.api.customsrepo;

import java.util.Date;

import java.util.List;

import org.springframework.data.domain.Page;
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
public interface ApplicationTrackerCustomsRepo {

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
     * @param applicationSearchTrackerDTO
     * @return
     */
    Page<ApplicationTracker> searchApplicationsForArrivalDeparture(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO);

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
     * @param subsDate
     * @param countryStats
     * @return
     */
    List<CountDTO> visaApplicationDashboard(Date subsDate, List<String> countryStats);

	/**
	*
	*/
	AgeStatisticsDTO ageStatisticsAll(Date startDate, Date endDate);

}
