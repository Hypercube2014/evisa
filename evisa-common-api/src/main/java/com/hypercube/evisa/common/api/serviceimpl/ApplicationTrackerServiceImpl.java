package com.hypercube.evisa.common.api.serviceimpl;

import java.time.LocalDate;
import java.util.Date;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.AgeStatisticsDTO;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.ApplicationSearchTrackerDTO;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardRequestDTO;
import com.hypercube.evisa.common.api.repository.ApplicationTrackerRepository;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
@Service
public class ApplicationTrackerServiceImpl implements ApplicationTrackerService {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicationTrackerRepository applicationTrackerRepository;

    /**
     * 
     */
    @Override
    public Page<ApplicationTracker> searchApplicationInTracker(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
        log.info("ApplicationTrackerServiceImpl-searchApplicationInTracker");

        if (applicationSearchTrackerDTO.getPeriod() != null) {
            GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(applicationSearchTrackerDTO.getPeriod());
            applicationSearchTrackerDTO.setStartDate(genericSearchDTO.getStartDate());
            applicationSearchTrackerDTO.setEndDate(genericSearchDTO.getEndDate());
        }

        return applicationTrackerRepository.searchApplicationInTracker(applicationSearchTrackerDTO);
    }

    /**
     * 
     */
    @Override
    public List<String> pendingVisaProcessList(boolean isExpressVisa, Pageable pageable) {
        log.info("ApplicationTrackerServiceImpl-pendingExpressVisaList");
        return applicationTrackerRepository.pendingVisaProcessList(isExpressVisa, pageable);
    }

    /**
     *
     */
    @Override
    public int getVisaDuration(String applicationNumber) {
        log.info("ApplicationTrackerServiceImpl-pendingExpressVisaList");
        return applicationTrackerRepository.getVisaDuration(applicationNumber);
    }

    /**
     *
     */
    @Override
    public Page<ApplicationTracker> searchApplicationsForArrivalDeparture(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
        log.info("ApplicationTrackerServiceImpl-searchApplicationsForArrival");
        return applicationTrackerRepository.searchApplicationsForArrivalDeparture(applicationSearchTrackerDTO);
    }

    /**
     *
     */
    @Override
    public ApplicationTracker getApplicationDetails(String applicationNumber) {
        log.info("ApplicationTrackerServiceImpl-getApplicationDetails");
        return applicationTrackerRepository.findByApplicationNumber(applicationNumber);
    }

    /**
     *
     */
    @Override
    public List<CountDTO> applicationProcessStatistics(Date subsDate, List<String> countryStat) {
        log.info("ApplicationTrackerServiceImpl-applicationProcessStatistics");
        return applicationTrackerRepository.visaApplicationDashboard(subsDate, countryStat);
    }

    /**
     *
     */
    @Override
    public List<CountDTO> travelPurposeStatistics(List<String> countryStats, Date startDate, Date endDate) {
        log.info("ApplicationTrackerServiceImpl-travelPurposeStatistics");
        return applicationTrackerRepository.travelPurposeStatistics(countryStats, startDate, endDate);
    }

    /**
     *
     */
    @Override
    public List<CountDTO> countryStatistics(List<String> loggedUser, Date startDate, Date endDate) {
        log.info("ApplicationTrackerServiceImpl-countryStatistics");
        return applicationTrackerRepository.countryStatistics(loggedUser, startDate, endDate);
    }

    /**
    *
    */
    @Override
	public AgeStatisticsDTO ageStatistics(List<String> countryStats, Date startDate, Date endDate) {
        log.info("ApplicationTrackerServiceImpl-ageStatistics");
        return applicationTrackerRepository.ageStatistics(countryStats, startDate, endDate);
    }
    
    /**
    *
    */
    @Override
    public AgeStatisticsDTO ageStatisticsAll(Date startDate, Date endDate) {
        log.info("ApplicationTrackerServiceImpl-ageStatisticsPeriod");
        return applicationTrackerRepository.ageStatisticsAll(startDate, endDate);
    }


    /**
     *
     */
    @Override
    public Page<ApplicationTracker> searchApplicationsForBCO(List<String> applicationNumberList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationTrackerServiceImpl-ageStatistics");

        return applicationTrackerRepository.searchApplicationsForBCO(applicationNumberList, genericSearchDTO);
    }

    /**
     *
     */
    @Override
    @Transactional
    public AgentTrackerDTO immigrationOfficerCount(ManagerDashboardRequestDTO managerDashboardDTO) {
        log.info("ApplicationTrackerServiceImpl-immigrationOfficerCount");
        
        return applicationTrackerRepository.immigrationOfficerCount(managerDashboardDTO);
    }

    /**
     *
     */
    @Override
    public Page<ApplicationTracker> pendingFileNumberForProcessing(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
        log.info("ApplicationTrackerServiceImpl-pendingFileNumberForProcessingcc " + applicationTrackerRepository.pendingFileNumberForProcessing(applicationSearchTrackerDTO));
        
        return applicationTrackerRepository.pendingFileNumberForProcessing(applicationSearchTrackerDTO);
    }

    /**
     *
     */
    @Override
    public String getApproverRemarks(String applicationNumber) {
        log.info("ApplicationTrackerServiceImpl-getApproverRemarks");
        return applicationTrackerRepository.getApproverRemarks(applicationNumber);
    }

	
    /**
    *
    */
	@Override
	public List<CountDTO> top5countryStatistics(Date startDate, Date endDate) {
		  log.info("ApplicationTrackerServiceImpl-countryStatisticsPeriod");
		return applicationTrackerRepository.top5countryStatistics(startDate, endDate);
	}

    @Override
    public LocalDate getArrivalDate(String applicationNumber) {
        log.info("ApplicationTrackerServiceImpl-arrivaldate");
        return applicationTrackerRepository.getArrivalDate(applicationNumber);

    }
	

	

}
