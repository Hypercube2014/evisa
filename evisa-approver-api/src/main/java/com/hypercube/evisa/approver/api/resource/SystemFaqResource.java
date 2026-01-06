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

import com.hypercube.evisa.common.api.domain.SystemFAQ;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.SystemFAQDTO;
import com.hypercube.evisa.common.api.model.SystemFAQSearchDTO;
import com.hypercube.evisa.common.api.service.SystemFAQService;
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
public class SystemFaqResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private SystemFAQService systemFAQService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ModelMapper modelMapper;

	/**
	 * @param authorization
	 * @param locale
	 * @param systemFaqDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/systemfaq")
	public ResponseEntity<ApiResultDTO> createSystemFaq(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @RequestBody SystemFAQDTO systemFaqDTO) {
		log.info("SystemFaqResource-createSystemFaq");
		return new ResponseEntity<ApiResultDTO>(
				systemFAQService.saveSystemFaq(locale, modelMapper.map(systemFaqDTO, SystemFAQ.class)), HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param systemFaqDTO
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/systemfaq")
	public ResponseEntity<ApiResultDTO> modifySystemFaq(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @RequestBody SystemFAQDTO systemFaqDTO) {
		log.info("SystemFaqResource-createSystemFaq");
		return new ResponseEntity<ApiResultDTO>(
				systemFAQService.modifySystemFaq(locale, modelMapper.map(systemFaqDTO, SystemFAQ.class)),
				HttpStatus.OK);

	}

	/**
	 * @param authorization
	 * @param locale
	 * @param faqid
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/systemfaq/{faqid}")
	public ResponseEntity<SystemFAQ> fetchSystemFaqById(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("faqid") Long faqid) {
		log.info("SystemFaqResource-fetchSystemFaqById");
		return new ResponseEntity<>(systemFAQService.getSystemFaqById(faqid), HttpStatus.OK);
	}

	/**
	 * @param authorization
	 * @param locale
	 * @param systemFAQSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchsystemfaq")
	@ResponseStatus(code = HttpStatus.OK)
	public Page<SystemFAQ> searchSystemFaq(@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestBody SystemFAQSearchDTO systemFAQSearchDTO) {
		log.info("SystemFaqResource-searchSystemFaq");
		return systemFAQService.searchSystemFaq(locale, systemFAQSearchDTO);
	}

}
