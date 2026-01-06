package com.hypercube.evisa.approver.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.hypercube.evisa.common.api.constants.*;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureDetailsDTO;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureDetailsList;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureHistoryDetailsList;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureServiceFacade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class ArrivalDepartureResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ArrivalDepartureServiceFacade arrivalDepartureServiceFacade;

	/**
	 * @param locale
	 * @param authorization
	 * @param arrDepDtlsDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/processarrival")
	public ResponseEntity<ApiResultDTO> processArrival(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ArrivalDepartureDetailsDTO arrDepDtlsDTO) {
		log.info("ArrivalDepartureResource-processArrival");
		return new ResponseEntity<>(arrivalDepartureServiceFacade.processArrival(arrDepDtlsDTO), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param arrDepDtlsDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/processdeparture")
	public ResponseEntity<ApiResultDTO> processDeparture(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ArrivalDepartureDetailsDTO arrDepDtlsDTO) {
		log.info("ArrivalDepartureResource-processDeparture");
		return new ResponseEntity<>(arrivalDepartureServiceFacade.processDeparture(arrDepDtlsDTO), HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param applicationNumber
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/arrivaldeparture/history/{applicationnumber}")
	public ResponseEntity<ArrivalDepartureDetailsList> getArrivalDepartureHistory(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("applicationnumber") String applicationNumber) {
		log.info("ArrivalDepartureResource-getArrivalDepatureHistory");
		return new ResponseEntity<>(arrivalDepartureServiceFacade.fetchArrivalDepartureDetails(applicationNumber),
				HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param arrdepid
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/arrivaldeparture/{arrdepid}")
	public ResponseEntity<ArrivalDepartureDetails> getArrivalDepartureById(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("arrdepid") Long arrdepid) {
		log.info("ArrivalDepartureResource-getArrivalDepartureById");
		return new ResponseEntity<>(arrivalDepartureServiceFacade.getArrivalDepartureById(arrdepid), HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/v1/api/arrivaldeparturehistory/{applicationnumber}")
	public ResponseEntity<ArrivalDepartureHistoryDetailsList> getArrivalDepartureHistoryDetails(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("applicationnumber") String applicationNumber) {
		log.info("ArrivalDepartureResource-getArrivalDepatureHistoryDetails");
		return new ResponseEntity<>(arrivalDepartureServiceFacade.arrivalDepartureHistoryDetails(applicationNumber),
				HttpStatus.OK);
	}

}
