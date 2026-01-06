package com.hypercube.evisa.approver.api.resource;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;
import com.hypercube.evisa.common.api.model.ApplicantTravelHistorySearchDTO;
import com.hypercube.evisa.common.api.service.ApplicantTravelHistoryServiceFacade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ApplicantTravelHistoryResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicantTravelHistoryServiceFacade applTravelHistServiceFacade;

	/**
	 * @param authorization
	 * @param applTravelHistSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/travelhistory/search")
	public ResponseEntity<Page<ApplicantTravelHistory>> searchTravelHistoryService(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ApplicantTravelHistorySearchDTO applTravelHistSearchDTO) {
		log.info("ApplicantTravelHistoryResource-searchTravelHistoryService {}", applTravelHistSearchDTO);

		return new ResponseEntity<>(applTravelHistServiceFacade.searchApplicantTravelHistory(applTravelHistSearchDTO),
				HttpStatus.OK);
	}

}
