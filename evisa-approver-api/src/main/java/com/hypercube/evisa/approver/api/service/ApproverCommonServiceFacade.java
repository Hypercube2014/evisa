package com.hypercube.evisa.approver.api.service;

import java.util.List;




import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.approver.api.model.ApproverApplicationPreviewDTO;
import com.hypercube.evisa.approver.api.model.ApproverHistoryDetailsDTO;
import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.DashboardDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardRequestDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardResponseDTO;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;


/**
 * @author SivaSreenivas
 *
 */
public interface ApproverCommonServiceFacade {

	/**
	 * @param loggeduser
	 * @return
	 */
	ApiResultDTO processNextSetOfFiles(String locale, String loggeduser);

	/**
	 * @param locale
	 * @param applicationNumber
	 * @return
	 */
	ApproverApplicationPreviewDTO applicationPreview(HttpServletRequest request, String locale,
			String applicationNumber);

	/**
	 * @param id
	 * @return
	 */
	ApplicantAttachmentDetails getAttachment(Long id);

	/**
	 * @param approverHistoryDDetaisDTO
	 * @return
	 */
	ApiResultDTO processApproval(String locale, ApproverHistoryDetailsDTO approverHistoryDDetaisDTO);

	/**
	 * @param loggeduser
	 * @param period
	 * @return
	 */
	DashboardDTO decisionMakerDashboard(String loggeduser, String period);

	/**
	 * @param applicationlist
	 * @return
	 */
	ApiResultDTO deallocateSelectedApplications(List<String> applicationlist);

	/**
	 * @param request
	 * @param locale
	 * @param applicationNumber
	 * @return
	 */
	ApproverApplicationPreviewDTO applicationDeparturePreview(HttpServletRequest request, String locale,
			String applicationNumber);

	/**
	 * @param genericSearchDTO
	 * @return
	 */
	AgentTrackerDTO borderControlDashboardStatistics(GenericSearchDTO genericSearchDTO);

	/**
	 * @param genericSearchDTO
	 * @return
	 */
	Page<ApplicationTracker> agentTrackerForSBCO(GenericSearchDTO genericSearchDTO);

	/**
	 * @param managerDashboardDTO
	 * @return
	 */
	ManagerDashboardResponseDTO immigrationOfficerDashboard(ManagerDashboardRequestDTO managerDashboardDTO);
	
	long overstayStatistics();

	/**
	*
	*/
	//ImmigrationOfficerStatisticsDTO statistiqueTop5(ManagerDashboardRequestDTO managerDashboardDTO);

	/**
	*
	*/
	//ManagerDashboardResponseDTO statistiqueTop5country();

	/**
	*
	*/

	
	List<ApplicationOverStayDetails> Checkoverstay();

	/**
	 * Télécharge le PDF du visa approuvé pour un numéro d'application donné
	 * @param applicationNumber le numéro d'application
	 * @param request la requête HTTP
	 * @return ResponseEntity contenant le PDF en tant que Resource
	 */
	ResponseEntity<Resource> downloadApprovedVisaPdf(String applicationNumber, HttpServletRequest request);
}
