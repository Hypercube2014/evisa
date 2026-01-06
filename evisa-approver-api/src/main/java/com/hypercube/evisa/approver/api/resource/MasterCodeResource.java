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

import com.hypercube.evisa.common.api.domain.MasterCodeDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeDetailsDTO;
import com.hypercube.evisa.common.api.model.MasterCodeList;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
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
public class MasterCodeResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private MasterCodeDetailsService masterCodeDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ModelMapper modelMapper;

	/**
	 * @param authorization
	 * @param locale
	 * @param masterCodeDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/mastercode")
	public ResponseEntity<ApiResultDTO> createMasterCode(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody MasterCodeDetailsDTO masterCodeDetailsDTO) {
		log.info("MasterCodeResource-createMasterCode");
		return new ResponseEntity<ApiResultDTO>(masterCodeDetailsService.createMasterCode(locale,
				modelMapper.map(masterCodeDetailsDTO, MasterCodeDetails.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param masterCodeDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/mastercode")
	public ResponseEntity<ApiResultDTO> modifyMasterCode(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody MasterCodeDetailsDTO masterCodeDetailsDTO) {
		log.info("MasterCodeResource-modifyMasterCode");
		return new ResponseEntity<ApiResultDTO>(masterCodeDetailsService.modifyMasterCode(locale,
				modelMapper.map(masterCodeDetailsDTO, MasterCodeDetails.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param id
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/mastercode/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Optional<MasterCodeDetails> fetchMasterCodeById(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("id") Long id) {
		log.info("MasterCodeResource-fetchMasterCodeById");
		return masterCodeDetailsService.findMasterCodeDetailsById(id);
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param masterCodeSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchmastercode")
	@ResponseStatus(code = HttpStatus.OK)
	public Page<MasterCodeDetails> searchMasterCodes(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody MasterCodeSearchDTO masterCodeSearchDTO) {
		log.info("MasterCodeResource-searchMasterCodes");
		return masterCodeDetailsService.searchMasterCodes(locale, masterCodeSearchDTO);
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param codeType
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/mastercode/active/{codeType}")
	@ResponseStatus(code = HttpStatus.OK)
	public MasterCodeList findActiveMasterCodesByCodeType(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("codeType") String codeType) {
		log.info("CommonResource-findActiveMasterCodesByCodeType");
		return new MasterCodeList(
				masterCodeDetailsService.findActiveMasterCodesByCodeType(codeType, CommonsConstants.YES));
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param codeType
	 * @param code
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/mastercode/{codeType}/{code}")
	public ResponseEntity<MasterCodeResultDTO> findMasterCodesByCodeTypeAndCode(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("codeType") String codeType,
			@PathVariable("code") String code) {
		log.info("CommonResource-findMasterCodesByCodeTypeAndCode");
		return new ResponseEntity<>(masterCodeDetailsService.findMasterCodesByCodeTypeAndCode(codeType, code),
				HttpStatus.OK);
	}

}
