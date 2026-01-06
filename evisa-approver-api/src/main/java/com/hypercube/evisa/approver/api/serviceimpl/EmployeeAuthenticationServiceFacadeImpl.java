package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.domain.EmployeeProfileAttachment;
import com.hypercube.evisa.approver.api.domain.EmployeeSuspensionHistory;
import com.hypercube.evisa.approver.api.domain.EmployeeUserLogin;
import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginHistory;
import com.hypercube.evisa.approver.api.model.ChangeStatusDTO;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTO;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsDTOList;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;
import com.hypercube.evisa.approver.api.model.EmployeeUserAuthenticationDTO;
import com.hypercube.evisa.approver.api.service.EmployeeAuthenticationServiceFacade;
import com.hypercube.evisa.approver.api.service.EmployeeDetailsService;
import com.hypercube.evisa.approver.api.service.EmployeeProfileAttachmentService;
import com.hypercube.evisa.approver.api.service.EmployeeSuspensionHistoryService;
import com.hypercube.evisa.approver.api.service.EmployeeUserLoginAttemptService;
import com.hypercube.evisa.approver.api.service.EmployeeUserLoginHistoryService;
import com.hypercube.evisa.approver.api.util.EmployeeJwtUtil;
import com.hypercube.evisa.approver.api.util.EmployeeUserDetailsService;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.IndexDTO;
import com.hypercube.evisa.common.api.model.MailDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;
import com.hypercube.evisa.common.api.model.UserDTO;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class EmployeeAuthenticationServiceFacadeImpl implements EmployeeAuthenticationServiceFacade {

	private static final int RANDOM_NUM_LENGTH = 7;
	// private static final int CREDENTIALS_EXPIRATION = 60 * 24 * 60;
	private static final int CREDENTIALS_NON_EXPIRATION = 60 * 24 * 5475;
	private static final int RESET_EXPIRATION = 15;
	// private static final int ACCOUNT_EXPIRATION = 60 * 24 * 365;
	private static final int ACCOUNT_NON_EXPIRATION = 60 * 24 * 5475;

	/**
	 * 
	 */
	@Value("${mail.sent.from}")
	private String mailFrom;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ModelMapper modelMapper;

	/**
	 * 
	 */
	@Autowired(required = true)
	private PasswordEncoder passwordEncoder;

	/**
	 * 
	 */
	@Autowired(required = true)
	private AuthenticationManager authenticationManager;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeDetailsService employeeDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserDetailsService employeeUserDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private MasterCodeDetailsService masterCodeDetailsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeJwtUtil jwtUtil;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserLoginHistoryService userLoginHistoryService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeProfileAttachmentService employeeProfileAttachmentService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeUserLoginAttemptService empUserLoginAttemptService;

	/**
	* 
	*/
	@Autowired(required = true)
	private ApplicationHeaderService applicationHeaderService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private EmployeeSuspensionHistoryService empSuspensionHistoryService;

	/**
	 * @param locale
	 * @param authorization
	 * @param userRegisterDetailsDTO
	 * @return
	 */
	@Override
	public ResponseEntity<ApiResultDTO> registerEmployee(String locale, String authorization,
			EmployeeDetailsDTO employeeDetailsDTO) {
		log.info("EmployeeAuthenticationServiceFacadeImpl-registerEmployee");

		/* Verifying username already exists in the system */
		if (employeeDetailsService.verifyUserNameExists(employeeDetailsDTO.getUserName())) {
			return new ResponseEntity<ApiResultDTO>(new ApiResultDTO(CommonsConstants.ERROR, LocaleConfig
					.getResourceValue("user.name.notavailable", null, locale, CommonsConstants.AUTH_RESOURCE)),
					HttpStatus.OK);
		}

		/* Verifying email id already exists in the system */
		if (employeeDetailsService.verifyEmailExists(employeeDetailsDTO.getEmail())) {
			List<Object> objArray = new ArrayList<>();
			objArray.add(employeeDetailsDTO.getEmail());

			return new ResponseEntity<ApiResultDTO>(
					new ApiResultDTO(CommonsConstants.ERROR, LocaleConfig.getResourceValue(
							"account.exists.with.emailid", objArray.toArray(), locale, CommonsConstants.AUTH_RESOURCE)),
					HttpStatus.OK);
		}

		/* insert the user register details */
		EmployeeDetails userRegisterDetailsResult = employeeDetailsService
				.saveEmployeeDetails(modelMapper.map(employeeDetailsDTO, EmployeeDetails.class));

		/* generate the user login for registered user */
		EmployeeUserLogin userLogin = new EmployeeUserLogin();
		userLogin.setUsername(userRegisterDetailsResult.getUsername());
		String pswdresult = CommonsUtil.randomAlphaNumeric(RANDOM_NUM_LENGTH);
		log.info("pswdresult-=-=-=-{}", pswdresult);
		userLogin.setPassword(passwordEncoder.encode(pswdresult));
		userLogin.setAccountEnabled(true);
		Calendar cal = Calendar.getInstance();
		userLogin.setCredentialsExpiryDate(CommonsUtil.calculateExpiryDate(RESET_EXPIRATION, cal));

		userLogin.setAccountExpiryDate(CommonsUtil.calculateExpiryDate(ACCOUNT_NON_EXPIRATION, cal));
		userLogin.setCredentialsExpiryDate(
				CommonsUtil.calculateExpiryDate(CREDENTIALS_NON_EXPIRATION, Calendar.getInstance()));

		userLogin.setChangePasswordRequired(true);
		userLogin.setAccountNonLocked(true);
		userLogin.setRoles(userRegisterDetailsResult.getRole());

		EmployeeUserLogin userResult = employeeUserDetailsService.generateUserCredentials(userLogin);

		/* send the user credentials through EMail Notification */
		ApiResultDTO userAuthDTO;
		if (userResult == null) {
			userAuthDTO = new ApiResultDTO(CommonsConstants.ERROR, LocaleConfig
					.getResourceValue("user.registration.failure", null, locale, CommonsConstants.AUTH_RESOURCE));
		} else {

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("username", userRegisterDetailsResult.getUsername());
			model.put("pswrd", pswdresult);
			jmsTemplate.convertAndSend("mailbox", new MailDTO(mailFrom, userRegisterDetailsResult.getEmail(),
					"Password Reset", "emailsent.ftl", model));

			userAuthDTO = new ApiResultDTO(CommonsConstants.SUCCESS, LocaleConfig
					.getResourceValue("user.registration.success", null, locale, CommonsConstants.AUTH_RESOURCE));
		}

		return new ResponseEntity<>(userAuthDTO, HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param userDTO
	 * @return
	 */
	@Override
	public ResponseEntity<EmployeeUserAuthenticationDTO> authenticateEmployee(String authorization,
			UserDTO userDTO) {
		log.info("::AuthenticationServiceFacadeImpl::authenticateEmployee::");
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getSecretKey()));

		boolean result = authentication.isAuthenticated();

		EmployeeUserAuthenticationDTO userAuthDTO = new EmployeeUserAuthenticationDTO();

		if (result) {
			/* get user login details */
			EmployeeDetails userDetails = employeeDetailsService.getEmployeeDetailsByUsername(userDTO.getUsername());
			EmployeeUserLogin userLoginResult = employeeUserDetailsService
					.getUserDetailsByUserName(userDTO.getUsername());

			/* return the user success message */
			userAuthDTO.setUsername(userDTO.getUsername());
			userAuthDTO.setAccessToken(jwtUtil.generateToken(userDTO.getUsername()));
			userAuthDTO.setChangePasswordRequired(getPasswordChangeReq(userLoginResult));
			userAuthDTO.setStatus(CommonsConstants.SUCCESS);
			userAuthDTO.setStatusDescription(LocaleConfig.getResourceValue("login.verify.success", null,"fr",
					CommonsConstants.AUTH_RESOURCE));
			userAuthDTO.setCompleteName(userDetails.getFullName());
			userAuthDTO.setEmailId(userDetails.getEmail());
			userAuthDTO.setRoles(userDetails.getRole());
			EmployeeProfileAttachment userProfileAttachment = employeeProfileAttachmentService
					.getEmployeeProfileAttachment(userDTO.getUsername());
			if (userProfileAttachment != null) {
				userAuthDTO.setProfilePic(Base64.getEncoder().encodeToString(userProfileAttachment.getFileData()));
			} else {
				userAuthDTO.setProfilePic("");
			}

			/* insert login user history */
			userLoginHistoryService.saveUserLoginHistory(
					new EmployeeUserLoginHistory(userDTO.getUserip(), userDTO.getUseragent(), userDTO.getUsername()));

		}

		return new ResponseEntity<>(userAuthDTO, HttpStatus.OK);
	}

	/**
	 * @param userLoginResult
	 * @return
	 */
	private boolean getPasswordChangeReq(EmployeeUserLogin userLoginResult) {
		log.info("::AuthenticationServiceFacadeImpl::getPasswordChangeReq::");
		return userLoginResult.isChangePasswordRequired();
	}

	/**
	 * 
	 */
	@Override
	public ResponseEntity<ApiResultDTO> modifyRegisterEmployee(String locale, String authorization,
			EmployeeDetailsDTO employeeDetailsDTO) {
		log.info("::AuthenticationServiceFacadeImpl::modifyRegisterEmployee::");

		ApiResultDTO apiResultDTO;
		EmployeeDetails employeeDetails = employeeDetailsService
				.getEmployeeDetailsByUsername(employeeDetailsDTO.getUserName());

		if (employeeDetails != null) {
			employeeDetailsService.saveEmployeeDetails(modelMapper.map(employeeDetailsDTO, EmployeeDetails.class));

			if (!employeeDetails.getRole().equals(employeeDetailsDTO.getRole())) {
				employeeUserDetailsService.updateUserRole(employeeDetails.getUsername(), employeeDetails.getRole());
			}
			apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
					LocaleConfig.getResourceValue("update.success", null, locale, null));
		} else {
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
					LocaleConfig.getResourceValue("error.invalid.request.notexist", null, locale, null));
		}

		return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
	}

	/**
	 * 
	 */
	@Override
	public ResponseEntity<EmployeeDetails> employeeProfile(String locale, String username) {
		log.info("::AuthenticationServiceFacadeImpl::employeeProfile::");

		EmployeeDetails employeeDetails = employeeDetailsService.getEmployeeDetailsByUsername(username);

		EmployeeProfileAttachment userProfileAttachment = employeeProfileAttachmentService
				.getEmployeeProfileAttachment(username);
		if (userProfileAttachment != null) {
			employeeDetails.setProfilePic(Base64.getEncoder().encodeToString(userProfileAttachment.getFileData()));
		} else {
			employeeDetails.setProfilePic("");
		}

		return new ResponseEntity<>(employeeDetails, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<EmployeeUserAuthenticationDTO> updateEmployeecredentials(String locale, String authorization,
			UserDTO userDTO) {
		log.info("::AuthenticationServiceFacadeImpl::updateEmployeecredentials::");
		HttpStatus httpstatus;

		EmployeeUserLogin userLoginResult = employeeUserDetailsService.getUserDetailsByUserName(userDTO.getUsername());
		boolean result = verifyPwdMatcher(userDTO, userLoginResult);

		EmployeeUserAuthenticationDTO userAuthDTO;
		if (result) {
			employeeUserDetailsService.updateUserCredentials(getEmployeeUserLogin(userDTO, userLoginResult));
			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.SUCCESS, LocaleConfig
					.getResourceValue("password.update.success", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.OK;
		} else {
			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.ERROR, LocaleConfig
					.getResourceValue("password.update.old.incorrect", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.FORBIDDEN;
		}

		return new ResponseEntity<>(userAuthDTO, httpstatus);
	}

	@Override
	public ResponseEntity<EmployeeUserAuthenticationDTO> resetEmployeeSecretKey(String locale, String authorization,
			String username) {
		log.info("::AuthenticationServiceFacadeImpl::resetEmployeeSecretKey::");
		EmployeeUserAuthenticationDTO userAuthDTO;
		EmployeeUserLogin userLoginResult = employeeUserDetailsService.getUserDetailsByUserName(username);
		HttpStatus httpstatus;

		if (userLoginResult == null) {

			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.ERROR,
					LocaleConfig.getResourceValue("user.not.exist", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.FORBIDDEN;

		} else if (!getAccountEnabledForUserLogin(userLoginResult)
				|| CommonsUtil.getTime(getAccountExpirytDateFromUserLogin(userLoginResult))
						- CommonsUtil.getTime(Calendar.getInstance().getTime()) <= 0) {

			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.ERROR, LocaleConfig
					.getResourceValue("account.not.enabled.or.expired", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.FORBIDDEN;

		} else {
			String pswdresult = CommonsUtil.randomAlphaNumeric(RANDOM_NUM_LENGTH);
			log.info("password reset -=-=-=-={}", pswdresult);
			employeeUserDetailsService.updateUserCredentials(getEmployeeLoginForResetKey(userLoginResult, pswdresult));
			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.SUCCESS, LocaleConfig
					.getResourceValue("password.reset.success", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.OK;

			EmployeeDetails userRegisterDetails = employeeDetailsService.getEmployeeDetailsByUsername(username);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("username", username);
			model.put("pswrd", pswdresult);
			jmsTemplate.convertAndSend("mailbox",
					new MailDTO(mailFrom, userRegisterDetails.getEmail(), "Password Reset", "emailsent.ftl", model));

		}

		return new ResponseEntity<>(userAuthDTO, httpstatus);
	}

	/**
	 * @param userDTO
	 * @param userLoginResult
	 * @return
	 */
	private boolean verifyPwdMatcher(UserDTO userDTO, EmployeeUserLogin userLoginResult) {
		return passwordEncoder.matches(userDTO.getSecretKey(), userLoginResult.getPassword());
	}

	/**
	 * @param userDTO
	 * @param userLoginResult
	 * @return
	 */
	private EmployeeUserLogin getEmployeeUserLogin(UserDTO userDTO, EmployeeUserLogin userLoginResult) {
		EmployeeUserLogin userLogin = new EmployeeUserLogin();
		userLogin.setUsername(userDTO.getUsername());
		userLogin.setPassword(passwordEncoder.encode(userDTO.getNewsecretkey()));
		userLogin.setAccountEnabled(true);
		userLogin.setCredentialsExpiryDate(
				CommonsUtil.calculateExpiryDate(CREDENTIALS_NON_EXPIRATION, Calendar.getInstance()));

		userLogin.setAccountExpiryDate(userLoginResult.getAccountExpiryDate());
		userLogin.setChangePasswordRequired(false);
		userLogin.setAccountNonLocked(true);
		userLogin.setRoles(userLoginResult.getRoles());
		return userLogin;
	}

	/**
	 * @param userLoginResult
	 * @return
	 */
	private boolean getAccountEnabledForUserLogin(EmployeeUserLogin userLoginResult) {
		return userLoginResult.isAccountEnabled();
	}

	/**
	 * @param userLoginResult
	 * @return
	 */
	private Date getAccountExpirytDateFromUserLogin(EmployeeUserLogin userLoginResult) {
		return userLoginResult.getAccountExpiryDate();
	}

	/**
	 * @param userLoginResult
	 * @return
	 */
	private EmployeeUserLogin getEmployeeLoginForResetKey(EmployeeUserLogin userLoginResult, String pswdresult) {
		userLoginResult.setPassword(passwordEncoder.encode(pswdresult));
		userLoginResult.setCredentialsExpiryDate(
				CommonsUtil.calculateExpiryDate(CREDENTIALS_NON_EXPIRATION, Calendar.getInstance()));
		userLoginResult.setChangePasswordRequired(true);
		userLoginResult.setAccountNonLocked(true);
		return userLoginResult;
	}

	/**
	 * 
	 */
	@Override
	public boolean employeeUserExists(String username) {
		log.info("::AuthenticationServiceFacadeImpl::resetEmployeeSecretKey::");
		return employeeDetailsService.verifyUserNameExists(username);
	}

	/**
	 * 
	 */
	@Override
	public ResponseEntity<Page<EmployeeDetails>> searchEmployeeDetails(
			EmployeeDetailsSearchDTO employeeDetailsSearchDTO) {
		log.info("::AuthenticationServiceFacadeImpl::searchEmployeeDetails::");
		return new ResponseEntity<>(employeeDetailsService.searchEmployeeDetails(employeeDetailsSearchDTO),
				HttpStatus.OK);
	}

	/**
	 * 
	 */
	@Override
	public ResponseEntity<EmployeeUserAuthenticationDTO> resetsecretkey(String locale, String authorization,
			String userName) {
		log.info("::AuthenticationServiceFacadeImpl::resetsecretkey::");

		EmployeeUserAuthenticationDTO userAuthDTO;
		EmployeeUserLogin userLoginResult = employeeUserDetailsService.getUserDetailsByUserName(userName);
		HttpStatus httpstatus;

		if (userLoginResult == null) {

			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.ERROR,
					LocaleConfig.getResourceValue("user.not.exist", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.FORBIDDEN;

		} else if (!getAccountEnabledForUserLogin(userLoginResult)
				|| CommonsUtil.getTime(getAccountExpirytDateFromUserLogin(userLoginResult))
						- CommonsUtil.getTime(Calendar.getInstance().getTime()) <= 0) {

			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.ERROR, LocaleConfig
					.getResourceValue("account.not.enabled.or.expired", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.FORBIDDEN;

		} else {
			String pswdresult = CommonsUtil.randomAlphaNumeric(RANDOM_NUM_LENGTH);
			log.info("password reset -=-=-=-={}", pswdresult);
			employeeUserDetailsService.updateUserCredentials(getEmployeeLoginForResetKey(userLoginResult, pswdresult));
			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.SUCCESS, LocaleConfig
					.getResourceValue("password.reset.success", null, locale, CommonsConstants.AUTH_RESOURCE));
			httpstatus = HttpStatus.OK;

			EmployeeDetails userRegisterDetails = employeeDetailsService.getEmployeeDetailsByUsername(userName);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("username", userName);
			model.put("pswrd", pswdresult);
			jmsTemplate.convertAndSend("mailbox",
					new MailDTO(mailFrom, userRegisterDetails.getEmail(), "Password Reset", "emailsent.ftl", model));

		}

		return new ResponseEntity<>(userAuthDTO, httpstatus);
	}

	/**
	*
	*/
	@Override
	@Transactional
	public ResponseEntity<EmployeeUserAuthenticationDTO> updateUserAccountLock(boolean result, String username) {
		log.info("::AuthenticationServiceFacadeImpl::updateUserAccountLock::");

		EmployeeUserAuthenticationDTO userAuthDTO;
		HttpStatus httpStatus;
		// verify the username exists
		if (employeeDetailsService.verifyUserNameExists(username)) {
			empUserLoginAttemptService.updateUserAccountEnabled(result, username);

			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.SUCCESS,
					"Updated Lock/Unlock details successfully");
			httpStatus = HttpStatus.OK;
		} else {
			userAuthDTO = new EmployeeUserAuthenticationDTO(CommonsConstants.ERROR, "Username doesn't exist in system");
			httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
		}

		return new ResponseEntity<>(userAuthDTO, httpStatus);
	}

	/**
	*
	*/
	@Override
	public EmployeeDetailsDTOList getEmployeesByRole(String roleCode) {
		log.info("::AuthenticationServiceFacadeImpl::getEmployeesByRole::");
		return employeeDetailsService.getEmployeesByRole(roleCode);
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public ApiResultDTO changeEmployeeStatus(String locale, ChangeStatusDTO changeStatusDTO) {
		log.info("::AuthenticationServiceFacadeImpl-changeEmployeeStatus");

		ApiResultDTO apiResultDTO;

		/* Need to get the Employee Details by UserName */
		EmployeeDetails employeeDetails = employeeDetailsService
				.getEmployeeDetailsByUsername(changeStatusDTO.getUsername());

		if (employeeDetails != null) {

			/* check and deallocate applications for DM role */
			if ("DM".equals(employeeDetails.getRole())) {
				applicationHeaderService.deallocateApplications(changeStatusDTO.getUsername());
			}

			/* insert the reasons for change status */
			empSuspensionHistoryService.saveEmployeeSuspensionHistory(new EmployeeSuspensionHistory(
					changeStatusDTO.getUsername(), changeStatusDTO.isStatus(), changeStatusDTO.getReasons(),
					changeStatusDTO.getRemarks(), changeStatusDTO.getLoggeduser()));

			/* update employee status */
			employeeDetailsService.updateEmployeeStatus(changeStatusDTO.getUsername(),
					changeStatusDTO.isStatus() ? "Y" : "N");

			/* update account status */
			empUserLoginAttemptService.updateUserAccountEnabled(changeStatusDTO.isStatus(),
					changeStatusDTO.getUsername());

			apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Activate/De-Activate Successful");
		} else {
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "Username doesn't exist in system");
		}

		return apiResultDTO;
	}

	/**
	 *
	 */
	@Override
	public ApiResultDTO transferAgents(ReportsSearchDTO reportsSearchDTO) {
		log.info("::AuthenticationServiceFacadeImpl-transferAgents");
		return employeeDetailsService.transferAgents(reportsSearchDTO.getReportType(), reportsSearchDTO.getAgentList());
	}

	/**
	 *
	 */
	@Override
	public ResponseEntity<EmployeeDetails> employeeProfileDesc(String locale, String username) {
		log.info("::AuthenticationServiceFacadeImpl::employeeProfileDesc::");

		EmployeeDetails employeeDetails = employeeDetailsService.getEmployeeDetailsByUsername(username);

		/* consolidate all the master codes */
		List<String> codeList = new ArrayList<>();
		codeList.add(employeeDetails.getCountryOfBirth());
		codeList.add(employeeDetails.getNationality());
		codeList.add(employeeDetails.getRole());

		/* get master code details with consolidate master code list */
		List<MasterCodeResultDTO> masterCodeResultDTOList = masterCodeDetailsService.getMasterCodeLists(codeList);

		/* construct map object to identify the desc with code and code type */
		Map<IndexDTO, String> resultMap = new HashMap<>();
		for (MasterCodeResultDTO masterCodeDTO : masterCodeResultDTOList) {
			String value;
			if ("en".equalsIgnoreCase(locale)) {
				value = masterCodeDTO.getDescription();
			} else {
				value = masterCodeDTO.getDescriptionOther();
			}
			resultMap.put(new IndexDTO(masterCodeDTO.getCode(), masterCodeDTO.getCodeType()), value);
		}

		EmployeeProfileAttachment userProfileAttachment = employeeProfileAttachmentService
				.getEmployeeProfileAttachment(username);
		if (userProfileAttachment != null) {
			employeeDetails.setProfilePic(Base64.getEncoder().encodeToString(userProfileAttachment.getFileData()));
		} else {
			employeeDetails.setProfilePic("");
		}

		/* replace the code with description from result map */
		employeeDetails.setCountryOfBirth(
				resultMap.get(new IndexDTO(employeeDetails.getCountryOfBirth(), CommonsConstants.CODETYPE_CNTRY)));
		employeeDetails.setNationality(
				resultMap.get(new IndexDTO(employeeDetails.getNationality(), CommonsConstants.CODETYPE_NTNLT)));
		employeeDetails.setRole(resultMap.get(new IndexDTO(employeeDetails.getRole(), CommonsConstants.CODETYPE_ROLE)));

		return new ResponseEntity<>(employeeDetails, HttpStatus.OK);
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public ResponseEntity<ApiResultDTO> forgetPassword(String locale, String authorization, String emailid,
			String address) {
		log.info("::AuthenticationServiceFacadeImpl::forgetPassword::");

		/* get account by email id */
		EmployeeDetails employeeDtls = employeeDetailsService.getEmployeeDetailsByEmailId(emailid);

		if (employeeDtls == null) {
			log.info("Email ID doesn't exists in the system");
		} else {
			if (employeeDtls.getEmployementStatus().equals(CommonsConstants.YES)) {
				String pswdresult = CommonsUtil.randomAlphaNumeric(RANDOM_NUM_LENGTH);
				log.info("pswdresult-=-=-=-{}", pswdresult);

				employeeUserDetailsService.forgetPasswordChange(employeeDtls.getUsername(),
						passwordEncoder.encode(pswdresult));

				jmsTemplate.convertAndSend("auditbox",
						new AuditDetails(CommonsConstants.UPDATE, CommonsConstants.FORGET_PSWD, emailid,
								"Forget Password", address == null ? "UNKNOWN" : address));

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("username", employeeDtls.getUsername());
				model.put("pswrd", pswdresult);
				jmsTemplate.convertAndSend("mailbox",
						new MailDTO(mailFrom, emailid, "Forget Password Reset", "emailsent.ftl", model));
			} else {
				log.info("Account {} associated to EMail ID {} is not active", employeeDtls.getUsername(), emailid);
			}
		}

		return new ResponseEntity<>(
				new ApiResultDTO(CommonsConstants.SUCCESS, "Details sent to the registered EMail ID"), HttpStatus.OK);
	}

}
