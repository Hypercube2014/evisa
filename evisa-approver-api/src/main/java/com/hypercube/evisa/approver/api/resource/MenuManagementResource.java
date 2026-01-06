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

import com.hypercube.evisa.common.api.domain.MenuDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchResultsDTO;
import com.hypercube.evisa.common.api.service.MenuManagementService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Data
@Slf4j
public class MenuManagementResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	MenuManagementService menuManagementService;

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param menuDetails
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/savemenu", method = RequestMethod.POST)
	public ResponseEntity<ApiResultDTO> saveMenudetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody MenuDetails menuDetails) {
		log.info("::MenuManagementResource::MenuManagementResource::");
		return menuManagementService.insertMenuDetails(menuDetails, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param menuDetails
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/updatemenu", method = RequestMethod.PUT)
	public ResponseEntity<ApiResultDTO> modifyMenudetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody MenuDetails menuDetails) {
		log.info("::MenuManagementResource::MenuManagementResource::");
		return menuManagementService.updateMenuDetails(menuDetails, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param menuId
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/viewmenu/{menuId}", method = RequestMethod.GET)
	public ResponseEntity<MenuDetails> viewMenudetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken,
			@RequestBody @PathVariable Long menuId) {
		log.info("::MenuManagementResource::viewMenudetails:: {}", menuId);
		return menuManagementService.viewMenuDetails(menuId, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @param menuDetailsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/searchmenu", method = RequestMethod.POST)
	public ResponseEntity<Page<MenuDetailsSearchResultsDTO>> searchMenuDetails(
			@RequestHeader("Accept-Language") String locale, @RequestHeader("loggeduser") String loggeduser,
			@RequestHeader("Authorization") String authToken, @RequestBody MenuDetailsSearchDTO menuDetailsSearchDTO) {
		log.info("::MenuManagementResource::searchMenudetails::");
		return menuManagementService.searchMenuDetails(menuDetailsSearchDTO, loggeduser, locale);
	}

	/**
	 * @param locale
	 * @param loggeduser
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/v1/api/activemenudetails", method = RequestMethod.GET)
	public ResponseEntity<List<MenuDetails>> fetchActiveMenuDetails(@RequestHeader("Accept-Language") String locale,
			@RequestHeader("loggeduser") String loggeduser, @RequestHeader("Authorization") String authToken) {
		log.info("::MenuManagementResource::fetchActiveMenuDetails::");
		return menuManagementService.getActiveMenu(loggeduser, locale);
	}

}
