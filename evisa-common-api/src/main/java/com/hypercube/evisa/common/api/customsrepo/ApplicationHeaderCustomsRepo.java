package com.hypercube.evisa.common.api.customsrepo;

import java.util.List;

import javax.persistence.Query;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicationHeaderCustomsRepo {
    
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
     * @param reportsSearchDTO
     * @return
     */
    Page<AgentTrackerDTO> processReport(ReportsSearchDTO reportsSearchDTO);
    
    /**
     * @param loggeduser
     * @param role
     * @param oprtype
     * @return
     */
    ReportsSearchDTO getAgentListBasedOnOprTypeAndRole(String loggeduser, String role, String oprtype);
    
    /**
     * @param loggeduser
     * @param period
     * @return
     */
    AgentTrackerDTO immigrationOfficerDashboard(String loggeduser, String period);
    
    /**
     * @param loggeduser
     * @param role
     * @return
     */
    Query getListOfEmployeeReporting(String loggeduser, String role);
    
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
	*
	*/
	long overstayStatistics();
	
	

}
