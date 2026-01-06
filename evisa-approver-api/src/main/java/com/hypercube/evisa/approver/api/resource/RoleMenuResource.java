package com.hypercube.evisa.approver.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.RoleMenuDTO;
import com.hypercube.evisa.common.api.model.RoleMenuResultDTO;
import com.hypercube.evisa.common.api.service.RoleMenuService;
import com.hypercube.evisa.common.api.service.RoleMenuServiceFacade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class RoleMenuResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	RoleMenuService roleMenuService;

	/**
	 * 
	 */
	@Autowired(required = true)
	RoleMenuServiceFacade roleMenuServiceFacade;

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param roleDetails
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/updaterolmenuedetails", method = RequestMethod.PUT)
	public ResponseEntity<ApiResultDTO> modifyRolMenuedetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody RoleMenuDTO roleDetails) {
		log.info(":RoleMenuResourcee:modifyRoleMenudetailse::");
		return roleMenuService.updateRoleMenuDetails(roleDetails, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param roleDetails
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/getrolmenuedetails", method = RequestMethod.POST)
	public ResponseEntity<List<RoleMenuResultDTO>> getRolMenuedetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody RoleDetails roleDetails) {
		log.info(":RoleMenuResourcee:getRolMenuedetails::");
		return roleMenuServiceFacade.getRoleMenuDetails(roleDetails, loggeduser, locale);
	}

}
