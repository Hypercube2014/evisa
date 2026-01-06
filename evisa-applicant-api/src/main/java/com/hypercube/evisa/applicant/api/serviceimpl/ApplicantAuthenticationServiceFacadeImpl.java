/**
* 
*/
package com.hypercube.evisa.applicant.api.serviceimpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.applicant.api.constants.UtilParamPropertyConstants;
import com.hypercube.evisa.applicant.api.listener.OnRegistrationCompleteEvent;
import com.hypercube.evisa.applicant.api.model.ApplicantUserAuthenticationDTO;
import com.hypercube.evisa.common.api.model.RefreshJwtRequestDTO;
import com.hypercube.evisa.applicant.api.model.UserVerificationTokenDTO;
import com.hypercube.evisa.applicant.api.service.ApplicantAuthenticationServiceFacade;
import com.hypercube.evisa.applicant.api.util.ApplicantJwtUtil;
import com.hypercube.evisa.applicant.api.util.ApplicantUserDetailsService;
import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicantUserLogin;
import com.hypercube.evisa.common.api.domain.ApplicantUserLoginHistory;
import com.hypercube.evisa.common.api.domain.ApplicantUserRegisterDetails;
import com.hypercube.evisa.common.api.domain.ApplicantVerificationToken;
import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.domain.UserProfileAttachment;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantUserRegisterDetailsDTO;
import com.hypercube.evisa.common.api.model.MailDTO;
import com.hypercube.evisa.common.api.model.UserDTO;
import com.hypercube.evisa.common.api.model.RefreshJwtRequestDTO;
import com.hypercube.evisa.common.api.service.ApplicantUserLoginHistoryService;
import com.hypercube.evisa.common.api.service.ApplicantUserRegisterDetailsService;
import com.hypercube.evisa.common.api.service.ApplicantVerificationTokenService;
import com.hypercube.evisa.common.api.service.UserProfileAttachmentService;


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
public class ApplicantAuthenticationServiceFacadeImpl implements ApplicantAuthenticationServiceFacade {

        private static final int RANDOM_NUM_LENGTH = 7;
        private static final int CREDENTIALS_EXPIRATION = 60 * 24 * 60;
        private static final int CREDENTIALS_NON_EXPIRATION = 60 * 24 * 5475;
        private static final int RESET_EXPIRATION = 15;
        private static final int ACCOUNT_EXPIRATION = 60 * 24 * 365;
        private static final int ACCOUNT_NON_EXPIRATION = 60 * 24 * 5475;
        
        /**
         * 
         */
        @Value("${mail.sent.from}")
        private String mailFrom;

        /**
         * 
         */
        @Value("${app.url}")
        private String appUrl;

        /**
         * 
         */
        @Autowired(required = true)
        private JmsTemplate jmsTemplate;

        /**
         * 
         */
        @Autowired
        ApplicationEventPublisher eventPublisher;

        /**
         * 
         */
        @Autowired(required = true)
        ModelMapper modelMapper;

        /**
         * 
         */
        @Autowired(required = true)
        PasswordEncoder passwordEncoder;

        /**
         * 
         */
        @Autowired(required = true)
        AuthenticationManager authenticationManager;

        /**
         * 
         */
        @Autowired(required = true)
        ApplicantUserRegisterDetailsService userRegisterDetailsService;

        /**
         * 
         */
        @Autowired(required = true)
        ApplicantUserDetailsService applicantUserDetailsService;

        /**
         * 
         */
        @Autowired(required = true)
        ApplicantJwtUtil jwtUtil;

        /**
         * 
         */
        @Autowired(required = true)
        ApplicantUserLoginHistoryService userLoginHistoryService;

        /**
         * 
         */
        @Autowired(required = true)
        ApplicantVerificationTokenService applicantVerificationTokenService;

        /**
         * 
         */
        @Autowired(required = true)
        UserProfileAttachmentService userProfileAttachmentService;

        /**
         * @param locale
         * @param authorization
         * @param userRegisterDetailsDTO
         * @return
         */
        @Override
        public ResponseEntity<ApiResultDTO> registeruser(HttpServletRequest request, String locale,
                        String authorization,
                        ApplicantUserRegisterDetailsDTO userRegisterDetailsDTO) {
                log.info("ApplicantAuthenticationServiceFacadeImpl-registeruser");

                /* Verifying username already exists in the system */
                if (userRegisterDetailsService.verifyUserNameExists(userRegisterDetailsDTO.getUserName())) {
                        return new ResponseEntity<ApiResultDTO>(new ApiResultDTO(CommonsConstants.ERROR, LocaleConfig
                                        .getResourceValue("user.name.notavailable", null, locale,
                                                        CommonsConstants.AUTH_RESOURCE)),
                                        HttpStatus.OK);
                }

                /* Verifying email id already exists in the system */
                if (userRegisterDetailsService.verifyEmailExists(userRegisterDetailsDTO.getEmailId())) {
                        List<Object> objArray = new ArrayList<>();
                        objArray.add(userRegisterDetailsDTO.getEmailId());

                        return new ResponseEntity<ApiResultDTO>(
                                        new ApiResultDTO(CommonsConstants.ERROR, LocaleConfig.getResourceValue(
                                                        "account.exists.with.emailid", objArray.toArray(), locale,
                                                        CommonsConstants.AUTH_RESOURCE)),
                                        HttpStatus.OK);
                }

                /* insert the user register details */
                ApplicantUserRegisterDetails userRegisterDetailsResult = userRegisterDetailsService
                                .saveUserRegisterDetails(modelMapper.map(userRegisterDetailsDTO,
                                                ApplicantUserRegisterDetails.class));

                /* generate the user login for registered user */
                ApplicantUserLogin userLogin = new ApplicantUserLogin();
                userLogin.setUserName(userRegisterDetailsResult.getUserName());
                userLogin.setPassword(passwordEncoder.encode(userRegisterDetailsDTO.getSecretKey()));
                userLogin.setAccountEnabled(false);
                Calendar cal = Calendar.getInstance();
                userLogin.setCredentialsExpiryDate(CommonsUtil.calculateExpiryDate(RESET_EXPIRATION, cal));

                String accExpireFlag = UtilParamPropertyConstants.getUserAccExpiryFlag();
                if (null != accExpireFlag && (CommonsConstants.YES).equalsIgnoreCase(accExpireFlag)) {
                        userLogin.setAccountExpiryDate(CommonsUtil.calculateExpiryDate(ACCOUNT_EXPIRATION, cal));
                } else {
                        userLogin.setAccountExpiryDate(CommonsUtil.calculateExpiryDate(ACCOUNT_NON_EXPIRATION, cal));
                }

                String credExpireFlag = UtilParamPropertyConstants.getUserPwdExpiryFlag();
                if (null != credExpireFlag && (CommonsConstants.YES).equalsIgnoreCase(credExpireFlag)) {
                        userLogin.setCredentialsExpiryDate(
                                        CommonsUtil.calculateExpiryDate(CREDENTIALS_EXPIRATION,
                                                        Calendar.getInstance()));
                } else {
                        userLogin.setCredentialsExpiryDate(
                                        CommonsUtil.calculateExpiryDate(CREDENTIALS_NON_EXPIRATION,
                                                        Calendar.getInstance()));
                }

                userLogin.setChangePasswordRequired(false);
                userLogin.setAccountNonLocked(true);
                userLogin.setRoles(userRegisterDetailsResult.getRole());
                userLogin.setEmailId(userRegisterDetailsResult.getEmailId());
                userLogin.setLocale(locale);

                ApplicantUserLogin userResult = applicantUserDetailsService.generateUserCredentials(userLogin);

                /* send the user credentials through EMail Notification */
                ApiResultDTO userAuthDTO;
                if (userResult == null) {
                        userAuthDTO = new ApiResultDTO(CommonsConstants.ERROR, LocaleConfig
                                        .getResourceValue("user.registration.failure", null, locale,
                                                        CommonsConstants.AUTH_RESOURCE));
                } else {

                        eventPublisher.publishEvent(
                                        new OnRegistrationCompleteEvent(userLogin, request.getLocale(),
                                                        request.getContextPath()));
                        List<Object> objArray = new ArrayList<>();
                        objArray.add(userLogin.getEmailId());
                        userAuthDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                                        LocaleConfig.getResourceValue("user.verify.email.sent", objArray.toArray(),
                                                        locale, null));
                }

                return new ResponseEntity<ApiResultDTO>(userAuthDTO, HttpStatus.OK);
        }

        public boolean isAuthorized(String requestedUsername) {
                String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
                return requestedUsername.equals(authenticatedUsername);
        }

        /**
         * @param locale
         * @param authorization
         * @param userDTO
         * @return
         */
        @Override
        public ResponseEntity<ApplicantUserAuthenticationDTO> authenticateUser(String locale, String authorization,
                        UserDTO userDTO) {
                log.info("::AuthenticationServiceFacadeImpl::authenticateUser::");
                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
                                                userDTO.getSecretKey()));

                boolean result = authentication.isAuthenticated();

                ApplicantUserAuthenticationDTO userAuthDTO = new ApplicantUserAuthenticationDTO();

                if (result) {
                        /* get user login details */
                        ApplicantUserRegisterDetails userDetails = userRegisterDetailsService
                                        .getUserRegisterDetailsByUserName(userDTO.getUsername());
                        ApplicantUserLogin userLoginResult = applicantUserDetailsService
                                        .getUserDetailsByUserName(userDTO.getUsername());

                        /* return the user success message */
                        userAuthDTO.setUsername(userDTO.getUsername());
                        userAuthDTO.setAccessToken(jwtUtil.generateToken(userDTO.getUsername()));
                        // userAuthDTO.setRefreshToken(jwtUtil.generateRefreshToken(userDTO.getUsername()));
                        userAuthDTO.setChangePasswordRequired(getPasswordChangeReq(userLoginResult));
                        userAuthDTO.setStatus(CommonsConstants.SUCCESS);
                        userAuthDTO.setStatusDescription(
                                        LocaleConfig.getResourceValue("login.verify.success", null, locale,
                                                        CommonsConstants.AUTH_RESOURCE));
                        userAuthDTO.setCompleteName(userDetails.getFirstName() + " " + userDetails.getLastName());
                        userAuthDTO.setEmailId(userDetails.getEmailId());
                        userAuthDTO.setRoles(userDetails.getRole());

                        UserProfileAttachment userProfileAttachment = userProfileAttachmentService
                                        .getUserProfileAttachment(userDTO.getUsername());
                        if (userProfileAttachment != null) {
                                userAuthDTO.setProfilePic(Base64.getEncoder()
                                                .encodeToString(userProfileAttachment.getFileData()));
                        } else {
                                userAuthDTO.setProfilePic("");
                        }

                        /* insert login user history */
                        userLoginHistoryService.saveUserLoginHistory(
                                        new ApplicantUserLoginHistory(userDTO.getUserip(), userDTO.getUseragent(),
                                                        userDTO.getUsername()));

                }

                return new ResponseEntity<ApplicantUserAuthenticationDTO>(userAuthDTO, HttpStatus.OK);
        }


        @Override
        public ResponseEntity<ApplicantUserAuthenticationDTO> refresh(String locale,
                        RefreshJwtRequestDTO refreshJwtRequestDTO) {
                log.info("::AuthenticationServiceFacadeImpl::refresh::");

                String username = jwtUtil.extractUsername(refreshJwtRequestDTO.getRefreshToken());

                UserDetails userDetails = this.applicantUserDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(username, userDetails)){
                        String newAccessToken = jwtUtil.generateToken(username);
                        // String refreshToken = jwtUtil.generateRefreshToken(username);
        
                        ApplicantUserAuthenticationDTO userAuthDTO = new ApplicantUserAuthenticationDTO();
                        userAuthDTO.setUsername(username);
                        userAuthDTO.setAccessToken(newAccessToken);
                        // userAuthDTO.setRefreshToken(refreshToken);
                        userAuthDTO.setStatus(CommonsConstants.SUCCESS);
        
                        return new ResponseEntity<>(userAuthDTO, HttpStatus.OK);
                }

                throw new IllegalArgumentException("Invalid refresh token");

        }

        /**
         * @param userLoginResult
         * @return
         */
        private boolean getPasswordChangeReq(ApplicantUserLogin userLoginResult) {
                log.info("::AuthenticationServiceFacadeImpl::getPasswordChangeReq::");
                return userLoginResult.isChangePasswordRequired();
        }

        /**
         * 
         */
        @Override
        public ModelAndView confirmUserAccount(ModelAndView modelAndView, String confirmationToken) {
                log.info("::AuthenticationServiceFacadeImpl::confirmUserAccount::");

                try {

                        String decryptedString = new String(Base64.getDecoder().decode(confirmationToken),
                                        StandardCharsets.UTF_8);
                        log.info("decryptedString {}", decryptedString);

                        UserVerificationTokenDTO userVerificationTokenDTO = new ObjectMapper().readValue(
                                        decryptedString,
                                        UserVerificationTokenDTO.class);

                        log.info("userVerificationTokenDTO {}", userVerificationTokenDTO);

                        ApplicantVerificationToken token = applicantVerificationTokenService
                                        .getVerificationToken(userVerificationTokenDTO.getToken());
                        if (token != null) {
                                Calendar cal = Calendar.getInstance();
                                if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0
                                                || token.getApplicantUserLogin().isAccountEnabled()) {
                                        modelAndView.addObject("title",
                                                        LocaleConfig.getResourceValue("user.verify.email.failed.title",
                                                                        null, userVerificationTokenDTO.getLocale(),
                                                                        null));
                                        modelAndView.addObject("message",
                                                        LocaleConfig.getResourceValue("user.verify.email.failed.body",
                                                                        null, userVerificationTokenDTO.getLocale(),
                                                                        null));
                                        modelAndView.addObject("redirection", "true");
                                } else {
                                        applicantUserDetailsService.updateUserAccountEnabled(true,
                                                        token.getApplicantUserLogin().getUserName());
                                        modelAndView.addObject("title", LocaleConfig.getResourceValue(
                                                        "user.verify.email.success.title", null,
                                                        userVerificationTokenDTO.getLocale(), null));
                                        modelAndView.addObject("message",
                                                        LocaleConfig.getResourceValue("user.verify.email.success.body",
                                                                        null, userVerificationTokenDTO.getLocale(),
                                                                        null));
                                }
                        } else {
                                modelAndView.addObject("title",
                                                LocaleConfig.getResourceValue("user.verify.email.failed.title",
                                                                null, userVerificationTokenDTO.getLocale(), null));
                                modelAndView.addObject("message",
                                                LocaleConfig.getResourceValue("user.verify.email.failed.body", null,
                                                                userVerificationTokenDTO.getLocale(), null));
                                modelAndView.addObject("redirection", "true");
                        }
                        modelAndView.addObject("confirmurl", appUrl + "/applicant-api/");
                        modelAndView.setViewName("account-verified");
                } catch (Exception exe) {
                        log.error("confirmUserAccount-Exception {}", exe);
                        modelAndView.addObject("errorTitle",
                                        LocaleConfig.getResourceValue("user.verify.email.failed.title", null, "en",
                                                        null));
                        modelAndView.addObject("message",
                                        LocaleConfig.getResourceValue("user.verify.email.failed.body", null, "en",
                                                        null));
                        modelAndView.setViewName("error");
                }

                return modelAndView;

        }

        /**
         * 
         */
        @Override
        public ResponseEntity<ApplicantUserRegisterDetails> applicantProfile(String locale, String authorization,
                        String username) {
                log.info("::AuthenticationServiceFacadeImpl::applicantProfile::");

                ApplicantUserRegisterDetails applicantUserRegisterDetails = userRegisterDetailsService
                                .getUserRegisterDetailsByUserName(username);

                if (applicantUserRegisterDetails != null) {
                        UserProfileAttachment userProfileAttachment = userProfileAttachmentService
                                        .getUserProfileAttachment(username);
                        if (userProfileAttachment != null) {
                                applicantUserRegisterDetails
                                                .setProfilePic(Base64.getEncoder()
                                                                .encodeToString(userProfileAttachment.getFileData()));
                        } else {
                                applicantUserRegisterDetails.setProfilePic("");
                        }
                }

                return new ResponseEntity<>(applicantUserRegisterDetails, HttpStatus.OK);
        }

        /**
         * 
         */
        @Override
        public ResponseEntity<ApiResultDTO> modifyRegisteruser(String locale, String authorization,
                        ApplicantUserRegisterDetailsDTO userRegisterDetailsDTO) {
                log.info("::AuthenticationServiceFacadeImpl::modifyRegisteruser::");
                ApiResultDTO apiResultDTO;
                boolean result = userRegisterDetailsService.verifyUserNameExists(userRegisterDetailsDTO.getUserName());

                if (result) {
                        userRegisterDetailsService.saveUserRegisterDetails(
                                        modelMapper.map(userRegisterDetailsDTO, ApplicantUserRegisterDetails.class));
                        apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                                        LocaleConfig.getResourceValue("update.success", null, locale, null));
                } else {
                        apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                                        LocaleConfig.getResourceValue("error.invalid.request.notexist", null, locale,
                                                        null));
                }

                return new ResponseEntity<>(apiResultDTO, HttpStatus.OK);
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
                ApplicantUserRegisterDetails applicantUserDtls = userRegisterDetailsService
                                .getUserDetailsByEmailId(emailid);

                if (applicantUserDtls == null) {
                        log.info("Email ID doesn't exists in the system");
                } else {

                        if (applicantUserDtls.getActive().equals(CommonsConstants.YES)) {
                                String pswdresult = CommonsUtil.randomAlphaNumeric(RANDOM_NUM_LENGTH);
                                log.info("pswdresult-=-=-=-{}", pswdresult);

                                applicantUserDetailsService.forgetPasswordChange(applicantUserDtls.getUserName(),
                                                passwordEncoder.encode(pswdresult));

                                jmsTemplate.convertAndSend("auditbox",
                                                new AuditDetails("UPDATE", CommonsConstants.FORGET_PSWD, emailid,
                                                                "Forget Password",
                                                                address == null ? "UNKNOWN" : address));

                                Map<String, Object> model = new HashMap<String, Object>();
                                model.put("username", applicantUserDtls.getUserName());
                                model.put("firstname", applicantUserDtls.getFirstName());
                                model.put("pswrd", pswdresult);
                                jmsTemplate.convertAndSend("mailbox",
                                                new MailDTO(mailFrom, emailid, "Password reset request | Demande de réinitialisation du mot de passe", "emailsent",
                                                                model));
                        } else {
                                log.info("Account {} associated to EMail ID {} is not active",
                                                applicantUserDtls.getUserName(), emailid);
                        }
                }

                return new ResponseEntity<>(
                                new ApiResultDTO(CommonsConstants.SUCCESS, "Details sent to the provided Email"),
                                HttpStatus.OK);
        }


        // @Override
        // @Transactional
        // public ResponseEntity<ApiResultDTO> forgetPassword(String locale, String authorization, String emailid,
        //                                            String address, String recaptchaToken) {
        // log.info("::AuthenticationServiceFacadeImpl::forgetPassword::");

        // // ✅ Vérification du reCAPTCHA
        // if (!recaptchaService.verifyRecaptcha(recaptchaToken)) {
        //         return new ResponseEntity<>(new ApiResultDTO("ERROR", "Invalid reCAPTCHA"), HttpStatus.BAD_REQUEST);
        // }

        // /* get account by email id */
        // ApplicantUserRegisterDetails applicantUserDtls = userRegisterDetailsService.getUserDetailsByEmailId(emailid);

        // if (applicantUserDtls == null) {
        //         log.info("Email ID doesn't exist in the system");
        // } else {
        //         if (applicantUserDtls.getActive().equals(CommonsConstants.YES)) {
        //         String pswdresult = CommonsUtil.randomAlphaNumeric(RANDOM_NUM_LENGTH);
        //         log.info("pswdresult-=-=-=-{}", pswdresult);

        //         applicantUserDetailsService.forgetPasswordChange(applicantUserDtls.getUserName(),
        //                 passwordEncoder.encode(pswdresult));

        //         jmsTemplate.convertAndSend("auditbox",
        //                 new AuditDetails("UPDATE", CommonsConstants.FORGET_PSWD, emailid,
        //                         "Forget Password", address == null ? "UNKNOWN" : address));

        //         Map<String, Object> model = new HashMap<>();
        //         model.put("username", applicantUserDtls.getUserName());
        //         model.put("pswrd", pswdresult);
        //         jmsTemplate.convertAndSend("mailbox",
        //                 new MailDTO(mailFrom, emailid, "Forget Password Reset", "emailsent", model));
        //         } else {
        //         log.info("Account {} associated to Email ID {} is not active", applicantUserDtls.getUserName(), emailid);
        //         }
        // }

        //         return new ResponseEntity<>(new ApiResultDTO(CommonsConstants.SUCCESS, "Details sent to the registered Email ID"), HttpStatus.OK);
        // }               


        /**
         *
         */
        @Override
        public ResponseEntity<ApplicantUserAuthenticationDTO> updateCredentials(String locale, String authorization,
                        UserDTO userDTO) {
                log.info("::AuthenticationServiceFacadeImpl::updateCredentials::");
                HttpStatus httpstatus;

                ApplicantUserLogin userLoginResult = applicantUserDetailsService
                                .getUserDetailsByUserName(userDTO.getUsername());
                boolean result = verifyPwdMatcher(userDTO, userLoginResult);

                ApplicantUserAuthenticationDTO userAuthDTO;
                if (result) {
                        applicantUserDetailsService
                                        .updateUserCredentials(getApplicantUserLogin(userDTO, userLoginResult));
                        userAuthDTO = new ApplicantUserAuthenticationDTO(CommonsConstants.SUCCESS, LocaleConfig
                                        .getResourceValue("password.update.success", null, locale,
                                                        CommonsConstants.AUTH_RESOURCE));
                        httpstatus = HttpStatus.OK;
                } else {
                        userAuthDTO = new ApplicantUserAuthenticationDTO(CommonsConstants.ERROR, LocaleConfig
                                        .getResourceValue("password.update.old.incorrect", null, locale,
                                                        CommonsConstants.AUTH_RESOURCE));
                        httpstatus = HttpStatus.FORBIDDEN;
                }

                return new ResponseEntity<>(userAuthDTO, httpstatus);
        }

        /**
         * @param userDTO
         * @param userLoginResult
         * @return
         */
        private boolean verifyPwdMatcher(UserDTO userDTO, ApplicantUserLogin userLoginResult) {
                return passwordEncoder.matches(userDTO.getSecretKey(), userLoginResult.getPassword());
        }

        /**
         * @param userDTO
         * @param userLoginResult
         * @return
         */
        private ApplicantUserLogin getApplicantUserLogin(UserDTO userDTO, ApplicantUserLogin userLoginResult) {
                ApplicantUserLogin userLogin = new ApplicantUserLogin();
                userLogin.setUserName(userDTO.getUsername());
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


}
