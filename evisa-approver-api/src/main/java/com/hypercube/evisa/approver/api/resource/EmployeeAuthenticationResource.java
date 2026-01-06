package com.hypercube.evisa.approver.api.resource;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.domain.EmployeeUserLogin;
import com.hypercube.evisa.approver.api.model.ChangeStatusDTO;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTO;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTOList;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;
import com.hypercube.evisa.approver.api.model.EmployeeUserAuthenticationDTO;
import com.hypercube.evisa.approver.api.model.PasswordCheckDTO;
import com.hypercube.evisa.approver.api.repository.EmployeeDetailsRepository;
import com.hypercube.evisa.approver.api.repository.EmployeeUserLoginRepository;
import com.hypercube.evisa.approver.api.service.EmployeeAuthenticationServiceFacade;
import com.hypercube.evisa.approver.api.util.EmployeeUserDetailsService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;
import com.hypercube.evisa.common.api.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
public class EmployeeAuthenticationResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	EmployeeAuthenticationServiceFacade employeeAuthenticationServiceFacade;

	@Autowired()
	EmployeeUserLoginRepository employeeUserLoginRepository;

	/**
	 * @param locale
	 * @param authorization
	 * @param employeeDetailsDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/register/employee")
	public ResponseEntity<ApiResultDTO> registerEmployee(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody EmployeeDetailsDTO employeeDetailsDTO) {
		log.info("::ApplicantAuthenticationResource::registerEmployee::");
		return employeeAuthenticationServiceFacade.registerEmployee(locale, authorization, employeeDetailsDTO);
	}

	// password check

	@PostMapping("/v1/api/check")
	public PasswordCheckDTO validatePassword(@RequestParam String username, @RequestParam String password) {
		
		String hashedPassword = employeeUserLoginRepository.getPassword(username) != null ? employeeUserLoginRepository.getPassword(username) : null;

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		log.info("Received request with username: " + username);

		try {
			boolean isMatch = hashedPassword != null && encoder.matches(password, hashedPassword);
			log.info("Password match result: " + isMatch);

			PasswordCheckDTO response = new PasswordCheckDTO();
			response.setMatch(isMatch);
			response.setUsername(username);
			return response;

		} catch (Exception e) {
			log.error("Error validating password", e);
			throw new RuntimeException("Error validating password", e);
		}
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param userRegisterDetails
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/register/updateemployeedetails")
	public ResponseEntity<ApiResultDTO> modifyRegisteruser(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody EmployeeDetailsDTO employeeDetailsDTO) {
		log.info("::ApplicantAuthenticationResource::modifyRegisteruser::");
		return employeeAuthenticationServiceFacade.modifyRegisterEmployee(locale, authorization, employeeDetailsDTO);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param userDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/validateemployee")
	public ResponseEntity<EmployeeUserAuthenticationDTO> authenticateEmployee(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization, @RequestBody UserDTO userDTO) {
		return employeeAuthenticationServiceFacade.authenticateEmployee(authorization, userDTO);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param userDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/updateemployeecredentials")
	public ResponseEntity<EmployeeUserAuthenticationDTO> updateEmployeecredentials(
			@RequestHeader("Accept-Language") String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization, @RequestBody UserDTO userDTO) {
		log.info("::ApplicantAuthenticationResource::updateEmployeecredentials::");
		return employeeAuthenticationServiceFacade.updateEmployeecredentials(locale, authorization, userDTO);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param username
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/employee/{username}")
	public ResponseEntity<EmployeeDetails> employeeProfile(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("username") String username) {
		log.info("::ApplicantAuthenticationResource::employeeProfile::{}", locale);
		return employeeAuthenticationServiceFacade.employeeProfile(locale, username);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param username
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/employeedesc/{username}")
	public ResponseEntity<EmployeeDetails> employeeProfileDesc(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("username") String username) {
		log.info("::ApplicantAuthenticationResource::employeeProfileDesc::{}", locale);
		return employeeAuthenticationServiceFacade.employeeProfileDesc(locale, username);
	}

	/**
	 * @param authexc
	 * @return
	 */
	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<EmployeeUserAuthenticationDTO> handleAuthenticationException(AuthenticationException authexc,
			Locale lc) {
		log.error("::ApplicantAuthenticationResource::handleAuthenticationException::{} {}", authexc.getMessage(), lc);
		EmployeeUserAuthenticationDTO userAuthDTO = new EmployeeUserAuthenticationDTO();
		userAuthDTO.setStatus(CommonsConstants.ERROR);
		userAuthDTO.setStatusDescription(authexc.getMessage());
		return new ResponseEntity<>(userAuthDTO, HttpStatus.FORBIDDEN);
	}

	/**
	 * @param authorization
	 * @param username
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/employeeexists/{username}")
	public boolean employeeUserExists(@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("username") String username) {
		log.info("::ApplicantAuthenticationResource::employeeUserExists::");
		return employeeAuthenticationServiceFacade.employeeUserExists(username);
	}

	/**
	 * @param authorization
	 * @param employeeDetailsSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchemployeedetails")
	public ResponseEntity<Page<EmployeeDetails>> searchEmployeeDetails(
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody EmployeeDetailsSearchDTO employeeDetailsSearchDTO) {
		log.info("::ApplicantAuthenticationResource::employeeUserExists::");
		return employeeAuthenticationServiceFacade.searchEmployeeDetails(employeeDetailsSearchDTO);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param userName
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/resetsecretkey/{username}")
	public ResponseEntity<EmployeeUserAuthenticationDTO> resetsecretkey(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("username") String userName) {
		log.info("::ApplicantAuthenticationResource::resetsecretkey::");
		return employeeAuthenticationServiceFacade.resetsecretkey(locale, authorization, userName);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param changeStatusDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/changestatus")
	public ResponseEntity<ApiResultDTO> changeEmployeeStatus(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ChangeStatusDTO changeStatusDTO) {
		log.info("::ApplicantAuthenticationResource-changeEmployeeStatus");
		return new ResponseEntity<>(employeeAuthenticationServiceFacade.changeEmployeeStatus(locale, changeStatusDTO),
				HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param roleCode
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/employeesbyrolecode/{rolecode}")
	public ResponseEntity<EmployeeDetailsDTOList> getEmployeesByRole(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("rolecode") String roleCode) {
		log.info("::ApplicantAuthenticationResource::getEmployeesByRole::");
		return new ResponseEntity<>(employeeAuthenticationServiceFacade.getEmployeesByRole(roleCode), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param roleCode
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/transferagents")
	public ResponseEntity<ApiResultDTO> transferAgents(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ReportsSearchDTO reportsSearchDTO) {
		log.info("::ApplicantAuthenticationResource::transferAgents::");
		return new ResponseEntity<>(employeeAuthenticationServiceFacade.transferAgents(reportsSearchDTO),
				HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param emailid
	 * @param address
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/employee/forgetpassword/{emailid}/{address}")
	public ResponseEntity<ApiResultDTO> forgetPassword(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader("Authorization") String authorization, @PathVariable("emailid") String emailid,
			@PathVariable("address") String address) {
		log.info("::ApplicantAuthenticationResource::forgetPassword::");
		return employeeAuthenticationServiceFacade.forgetPassword(locale, authorization, emailid, address);
	}

}
