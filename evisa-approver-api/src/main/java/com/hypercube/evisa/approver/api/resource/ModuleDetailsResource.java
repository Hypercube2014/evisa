package com.hypercube.evisa.approver.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.common.api.domain.ModuleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailSearchResultsDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailsSearchDTO;
import com.hypercube.evisa.common.api.service.ModuleDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class ModuleDetailsResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	ModuleDetailsService moduleDetailsService;

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param moduleDetails
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/savemoduledetails")
	public ResponseEntity<ApiResultDTO> savModuleCodeDetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody ModuleDetails moduleDetails) {
		log.info("::ModuleDetailsResource::savModuleCodeDetails::");
		return moduleDetailsService.saveModuleDetails(moduleDetails, locale, loggeduser);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param moduleDetailsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchmoduledetails")
	public ResponseEntity<Page<ModuleDetailSearchResultsDTO>> searchModuleCodedetails(
			@RequestHeader("Accept-Language") String locale, @RequestHeader("loggeduser") String loggeduser,
			@RequestHeader("Authorization") String authToken,
			@RequestBody ModuleDetailsSearchDTO moduleDetailsSearchDTO) {
		log.info("::ModuleDetailsResource::searchModuleCodedetails:: {}", moduleDetailsSearchDTO);
		return moduleDetailsService.searchModuleCodedetails(moduleDetailsSearchDTO, loggeduser);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param sysId
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/viewmoduledetails/{moduleId}")
	public ResponseEntity<ModuleDetails> viewModuleCodedetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@PathVariable("moduleId") Long moduleId) {
		log.info("::ModuleDetailsResource::viewModuleCodedetails:: {}", moduleId);
		return moduleDetailsService.viewModuleCodedetails(moduleId, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param sysId
	 * @return
	 */
	@CrossOrigin
	@DeleteMapping(value = "/v1/api/moduledetails/{moduleId}")
	public ResponseEntity<ApiResultDTO> deleteModuleCodedetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@PathVariable("moduleId") Long moduleId) {
		log.info("::ModuleDetailsResource::deleteModuleCodedetails:: {}", moduleId);
		return moduleDetailsService.deleteModuleCodedetails(moduleId, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param moduleDetails
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/updatemoduledetails")
	public ResponseEntity<ApiResultDTO> modifyModuledetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody ModuleDetails moduleDetails) {
		log.info("::ModuleDetailsResource::modifyModuledetails::");
		return moduleDetailsService.updateModuleDetails(moduleDetails, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/activeModuledetails")
	public ResponseEntity<List<ModuleDetails>> fetchActiveModuleDetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken) {
		log.info("::ModuleDetailsResource::fetchActiveModuleDetails::");
		return moduleDetailsService.getActiveModules(loggeduser, locale);
	}

}
