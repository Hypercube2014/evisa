package com.hypercube.evisa.approver.api.resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.approver.api.model.ManagementVisaDetailsDTO;
import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.VisaDetailsSearchDTO;
import com.hypercube.evisa.common.api.service.ManagementVisaDetailsService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class ManagementVisaDetailsResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ManagementVisaDetailsService managementVisaDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ModelMapper modelMapper;

	/**
	 * @param authorization
	 * @param locale
	 * @param managementVisaDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/visadetails")
	public ResponseEntity<ApiResultDTO> createVisaDetails(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody ManagementVisaDetailsDTO managementVisaDetailsDTO) {
		log.info("ManagementVisaDetailsResource-createVisaDetails");
		return new ResponseEntity<ApiResultDTO>(managementVisaDetailsService.createVisaDetails(locale,
				modelMapper.map(managementVisaDetailsDTO, ManagementVisaDetails.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param managementVisaDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/visadetails")
	public ResponseEntity<ApiResultDTO> modifyVisaDetails(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody ManagementVisaDetailsDTO managementVisaDetailsDTO) {
		log.info("ManagementVisaDetailsResource-modifyVisaDetails");
		return new ResponseEntity<ApiResultDTO>(managementVisaDetailsService.modifyVisaDetails(locale,
				modelMapper.map(managementVisaDetailsDTO, ManagementVisaDetails.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param managementVisaDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/visadetails/{visaId}")
	public ResponseEntity<ManagementVisaDetails> findVisaDetailsById(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("visaId") Long visaId) {
		log.info("ManagementVisaDetailsResource-modifyVisaDetails");
		return new ResponseEntity<>(managementVisaDetailsService.findVisaDetailsById(visaId), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param visaDetailsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchvisadetails")
	@ResponseStatus(code = HttpStatus.OK)
	public Page<ManagementVisaDetails> searchVisaDetails(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody VisaDetailsSearchDTO visaDetailsSearchDTO) {
		log.info("ManagementVisaDetailsResource-searchVisaDetails");
		return managementVisaDetailsService.searchVisaDetails(locale, visaDetailsSearchDTO);
	}

}
