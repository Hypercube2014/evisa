package com.hypercube.evisa.approver.api.resource;

import java.util.Optional;

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

import com.hypercube.evisa.approver.api.model.MasterCodeTypeDetailsDTO;
import com.hypercube.evisa.common.api.domain.MasterCodeTypeDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeList;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;
import com.hypercube.evisa.common.api.service.MasterCodeTypeDetailsService;
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
public class MasterCodeTypeResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private MasterCodeTypeDetailsService masterCodeTypeDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ModelMapper modelMapper;

	/**
	 * @param authorization
	 * @param locale
	 * @param masterCodeTypeDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/mastercodetype")
	public ResponseEntity<ApiResultDTO> createMasterCodeType(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody MasterCodeTypeDetailsDTO masterCodeTypeDetailsDTO) {
		log.info("MasterCodeTypeResource-createMasterCodeType");
		return new ResponseEntity<ApiResultDTO>(masterCodeTypeDetailsService.createMasterCodeType(locale,
				modelMapper.map(masterCodeTypeDetailsDTO, MasterCodeTypeDetails.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param masterCodeTypeDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/mastercodetype")
	public ResponseEntity<ApiResultDTO> modifyMasterCodeType(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody MasterCodeTypeDetailsDTO masterCodeTypeDetailsDTO) {
		log.info("MasterCodeTypeResource-modifyMasterCodeType");
		return new ResponseEntity<ApiResultDTO>(masterCodeTypeDetailsService.modifyMasterCodeType(locale,
				modelMapper.map(masterCodeTypeDetailsDTO, MasterCodeTypeDetails.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param id
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/mastercodetype/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Optional<MasterCodeTypeDetails> fetchMasterCodeTypeById(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("id") Long id) {
		log.info("MasterCodeTypeResource-fetchMasterCodeTypeById");
		return masterCodeTypeDetailsService.findMasterCodeTypeDetailsById(id);
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param masterCodeSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchmastercodetype")
	@ResponseStatus(code = HttpStatus.OK)
	public Page<MasterCodeTypeDetails> searchMasterCodeTypes(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody MasterCodeSearchDTO masterCodeSearchDTO) {
		log.info("MasterCodeTypeResource-searchMasterCodeTypes");
		return masterCodeTypeDetailsService.searchMasterCodeType(locale, masterCodeSearchDTO);
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param codeType
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/mastercodetype/active")
	@ResponseStatus(code = HttpStatus.OK)
	public MasterCodeList findActiveMasterCodeTypes(@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale) {
		log.info("CommonResource-findActiveMasterCodeTypes");
		return new MasterCodeList(masterCodeTypeDetailsService.findMasterCodeTypes(CommonsConstants.YES));
	}

}
