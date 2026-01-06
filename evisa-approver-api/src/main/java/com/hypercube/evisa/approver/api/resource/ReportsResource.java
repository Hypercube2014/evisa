package com.hypercube.evisa.approver.api.resource;

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
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.approver.api.service.ReportsServiceFacade;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ReportsResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ReportsServiceFacade reportsServiceFacade;

	/**
	 * @param authorization
	 * @param genericSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/agenttracker")
	public ResponseEntity<Page<AgentTrackerDTO>> agentTracker(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody GenericSearchDTO genericSearchDTO) {
		log.info("ReportsResource-agentTracker");
		return new ResponseEntity<>(reportsServiceFacade.agentTracker(genericSearchDTO), HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param reportsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/performancereport")
	public ResponseEntity<Page<AgentTrackerDTO>> performanceReport(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ReportsSearchDTO reportsSearchDTO) {
		log.info("ReportsResource-agentTracker");
		return new ResponseEntity<>(reportsServiceFacade.performanceReport(reportsSearchDTO), HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param reportsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/processreport")
	public ResponseEntity<Page<AgentTrackerDTO>> processReport(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ReportsSearchDTO reportsSearchDTO) {
		log.info("ReportsResource-agentTracker");
		return new ResponseEntity<>(reportsServiceFacade.processReport(reportsSearchDTO), HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param loggeduser
	 * @param role
	 * @param oprtype
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/fetchagents/{loggeduser}/{role}/{oprtype}")
	public ResponseEntity<ReportsSearchDTO> getAgentListBasedOnOprTypeAndRole(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("loggeduser") String loggeduser, @PathVariable("role") String role,
			@PathVariable("oprtype") String oprtype) {
		log.info("ReportsResource-getAgentListBasedOnOprTypeAndRole");
		return new ResponseEntity<>(reportsServiceFacade.getAgentListBasedOnOprTypeAndRole(loggeduser, role, oprtype),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param loggeduser
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/fetchapplications/{agentusername}")
	public ResponseEntity<ReportsSearchDTO> fetchApplicationsByLoggedUser(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("agentusername") String agentusername) {
		log.info("ReportsResource-fetchApplicationsByLoggedUser");
		return new ResponseEntity<>(reportsServiceFacade.fetchApplicationsByLoggedUser(agentusername), HttpStatus.OK);
	}

}
