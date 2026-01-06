package com.hypercube.evisa.approver.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchResultsDTO;
import com.hypercube.evisa.common.api.service.RoleManagementService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class RoleManagementResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	RoleManagementService roleManagementService;

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param roleDetailsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/searchroledetails", method = RequestMethod.POST)
	public ResponseEntity<Page<RoleDetailsSearchResultsDTO>> searchRoleDetails(
			@RequestHeader("Accept-Language") String locale, @RequestHeader("loggeduser") String loggeduser,
			@RequestHeader("Authorization") String authToken, @RequestBody RoleDetailsSearchDTO roleDetailsSearchDTO) {
		log.info("::RoleManagementResource::searchRoledetails::");
		return roleManagementService.searchRoleDetails(roleDetailsSearchDTO, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param roleId
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/viewroledetails/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<RoleDetails> viewRoledetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody @PathVariable Long roleId) {
		log.info("::RoleManagementResource::viewMaildetails:: {}", roleId);
		return roleManagementService.viewRoleDetails(roleId, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param roleDetails
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/saveroledetails", method = RequestMethod.POST)
	public ResponseEntity<ApiResultDTO> saveRoledetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody RoleDetails roleDetails) {
		log.info("::RoleManagementResource::RoleManagementResource::");
		return roleManagementService.createRoleDetails(roleDetails, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param roleDetails
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/updateroledetails", method = RequestMethod.PUT)
	public ResponseEntity<ApiResultDTO> modifyRoledetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody RoleDetails roleDetails) {
		log.info("::RoleManagementResource::RoleManagementResource::");
		return roleManagementService.updateRoleDetails(roleDetails, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/activeroledetails", method = RequestMethod.GET)
	public ResponseEntity<List<RoleDetails>> fetchActiveRoleDetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken) {
		log.info("::RoleManagementResource::fetchActiveRoleDetails::");
		return roleManagementService.getActiveRoles(loggeduser, locale);
	}
}
