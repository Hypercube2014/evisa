package com.hypercube.evisa.approver.api.resource;

import java.util.*;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.hypercube.evisa.approver.api.domain.OverstayDeparted;
import com.hypercube.evisa.approver.api.model.ApplicationHeaderDetailsRequestDTO;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureHistoryRequestDTO;
import com.hypercube.evisa.approver.api.service.ApplicationHeaderDetailsService;
import com.hypercube.evisa.approver.api.service.ApproverCommonServiceFacade;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureHistoryService;
import com.hypercube.evisa.approver.api.service.OverstayDepartedService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicationSearchTrackerDTO;
import com.hypercube.evisa.common.api.model.DashboardDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardRequestDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardResponseDTO;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ApproverDashboardResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApproverCommonServiceFacade approverCommonServiceFacade;

	@Autowired(required = true)
	private ApplicationHeaderService applicationHeaderService;

	@Autowired(required = true)
	private ArrivalDepartureHistoryService arrivalDepartureHistoryService;

	@Autowired(required = true)
	private ApplicationHeaderDetailsService applicationHeaderDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationTrackerService applicationTrackerService;

	@Autowired(required = true)
	private OverstayDepartedService overstayDepartedserviceimpl;

	/**
	 * @param locale
	 * @param authorization
	 * @param loggeduser
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/nextsetoffiles/{loggeduser}")
	public ResponseEntity<ApiResultDTO> processNextSetOfFiles(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("loggeduser") String loggeduser) {
		log.info("ApproverDashboardResource-nextSetOfFiles");
		return new ResponseEntity<>(approverCommonServiceFacade.processNextSetOfFiles(locale, loggeduser),
				HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/v1/api/overstay")
	public long overstaystatistics() {
		log.info("ApproverDashboardResource-overstaystatistics");

		return approverCommonServiceFacade.overstayStatistics();
	}

	@CrossOrigin
	@GetMapping(value = "/v1/api/overstayss")
	public List<ApplicationOverStayDetails> overstaystatisticss() {
		log.info("ApproverDashboardResource-overstaystatistics");

		return approverCommonServiceFacade.Checkoverstay();
	}

	/**
	 * @param authorization
	 * @param applicationSearchTrackerDTO
	 * @return
	 */
	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/searchapplicationtracker")
	public Page<ApplicationTracker> searchApplicationTracker(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
		log.info("ApproverDashboardResource-searchApplicationTracker");
		return applicationTrackerService.searchApplicationInTracker(applicationSearchTrackerDTO);
	}

	/**
	 * @param authorization
	 * @param applicationSearchTrackerDTO
	 * @return
	 */
	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/searchfilenumbers")
	public Page<ApplicationTracker> pendingFileNumberForProcessing(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
		log.info("ApproverDashboardResource-pendingFileNumberForProcessing");
		return applicationTrackerService.pendingFileNumberForProcessing(applicationSearchTrackerDTO);
	}

	/**
	 * @param authorization
	 * @param applicationSearchTrackerDTO
	 * @return
	 */
	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/searchapplicationforarrivaldeparture")
	public Page<ApplicationTracker> searchApplicationsForArrivalDeparture(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
		log.info("ApproverDashboardResource-searchApplicationsForArrivalDeparture");

		List<OverstayDeparted> listdepartedOverstay = overstayDepartedserviceimpl.findAllOverstayDeparted();

		return applicationTrackerService.searchApplicationsForArrivalDeparture(applicationSearchTrackerDTO);
	}

	/**
	 * @param authorization
	 * @param loggeduser
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/dmdashboard/{loggeduser}/{period}")
	public ResponseEntity<DashboardDTO> decisionMakerDashboard(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("loggeduser") String loggeduser, @PathVariable("period") String period) {
		log.info("ApproverDashboardResource-decisionMakerDashboard");
		return new ResponseEntity<>(approverCommonServiceFacade.decisionMakerDashboard(loggeduser, period),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param loggeduser
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/arrdephistory")
	public ResponseEntity<Object> SBCODashboard(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ArrivalDepartureHistoryRequestDTO arrivalDepartureHistory) {
		String approver = arrivalDepartureHistory.getLoggedUser();
		String period = arrivalDepartureHistory.getPeriod();
		log.info("ApproverDashboardResource-SBCODashboard");

		return new ResponseEntity<Object>(arrivalDepartureHistoryService.getSBCOApprovedReject(approver, period),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param managerDashboardDTO
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/applicationDetails")
	public ResponseEntity<Object> applicationDetails(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicationHeaderDetailsRequestDTO applicationHeaderDetailsRequestDTO) {
		log.info("ApproverDashboardResource-applicationHeaderDetails");

		String period = applicationHeaderDetailsRequestDTO.getPeriod();
		return new ResponseEntity<Object>(applicationHeaderDetailsService.getApplicationList(period),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param managerDashboardDTO
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/dmmdashboard")
	public ResponseEntity<ManagerDashboardResponseDTO> immigrationOfficerDashboard(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ManagerDashboardRequestDTO managerDashboardDTO) {
		log.info("ApproverDashboardResource-immigrationOfficerDashboard");

		return new ResponseEntity<>(approverCommonServiceFacade.immigrationOfficerDashboard(managerDashboardDTO),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param genericSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/bordercontroldashboard")
	public ResponseEntity<AgentTrackerDTO> borderControlDashboardStatistics(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody GenericSearchDTO genericSearchDTO) {
		log.info("ApproverDashboardResource-borderControlDashboardStatistics");
		List<OverstayDeparted> listdepartedOverstay = overstayDepartedserviceimpl.findAllOverstayDeparted();

		for (OverstayDeparted overstayDeparted : listdepartedOverstay) {
			System.out.println(overstayDeparted.getAmount());
			System.out.println(overstayDeparted.getApplicationNumber());
			System.out.println(overstayDeparted.getFileNumber());
			System.out.println(overstayDeparted.getUsername());
			System.out.println(overstayDeparted.getAction_date());
			System.out.println(overstayDeparted.getGivenName());
		}
		return new ResponseEntity<>(approverCommonServiceFacade.borderControlDashboardStatistics(genericSearchDTO),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param genericSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/agenttrackerforsbco")
	public ResponseEntity<Page<ApplicationTracker>> agentTrackerForSBCO(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody GenericSearchDTO genericSearchDTO) {
		log.info("ApproverDashboardResource-agentTrackerForSBCO");

		return new ResponseEntity<>(approverCommonServiceFacade.agentTrackerForSBCO(genericSearchDTO), HttpStatus.OK);
	}

	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/applicationPendingdmm")
	public ResponseEntity<Long> decisionMakerPending(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody GenericSearchDTO genericSearchDTO) {
		log.info("ApproverDashboardResource-decisionMakerPending");
		// System.out.println("docstatus"+docstatus);
		return new ResponseEntity<>(applicationHeaderService.decisionMakerPendingCount(genericSearchDTO.getDocstatus()),
				HttpStatus.OK);
	}

	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/overstayDeparted")
	public ResponseEntity<List<OverstayDeparted>> overstayDeparted(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization) {

		// List<OverstayDeparted>
		// listdepartedOverstay=overstayDepartedserviceimpl.findAllOverstayDeparted();
		// for (OverstayDeparted overstayDeparted : listdepartedOverstay) {
		// System.out.println(overstayDeparted.getAmount());
		// System.out.println(overstayDeparted.getApplicationNumber());
		// System.out.println(overstayDeparted.getFileNumber());
		// System.out.println(overstayDeparted.getUsername());
		// System.out.println(overstayDeparted.getAction_date());
		// System.out.println(overstayDeparted.getGivenName());
		// }

		return new ResponseEntity<>(overstayDepartedserviceimpl.findAllOverstayDeparted(), HttpStatus.OK);
	}

	@CrossOrigin
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping(value = "/v1/api/countoverstayDeparted")
	public ResponseEntity<Long> CountoverstayDeparted(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization) {
		long counter = 0;
		List<OverstayDeparted> listdepartedOverstay = overstayDepartedserviceimpl.findAllOverstayDeparted();
		for (OverstayDeparted overstayDeparted : listdepartedOverstay) {
			counter++;
		}
		System.out.println(counter);
		return new ResponseEntity<>(counter, HttpStatus.OK);
	}

}
