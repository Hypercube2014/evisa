package com.hypercube.evisa.applicant.api.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.hypercube.evisa.applicant.api.model.ApplicantUserAuthenticationDTO;
import com.hypercube.evisa.common.api.domain.ApplicantUserRegisterDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantUserRegisterDetailsDTO;
import com.hypercube.evisa.common.api.model.RefreshJwtRequestDTO;
import com.hypercube.evisa.common.api.model.UserDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantAuthenticationServiceFacade {

        /**
         * @param locale
         * @param authorization
         * @param userRegisterDetailsDTO
         * @return
         */
        ResponseEntity<ApiResultDTO> registeruser(HttpServletRequest request, String locale, String authorization,
                        ApplicantUserRegisterDetailsDTO userRegisterDetailsDTO);

        /**
         * @param locale
         * @param authorization
         * @param userDTO
         * @return
         */
        ResponseEntity<ApplicantUserAuthenticationDTO> authenticateUser(String locale, String authorization,
                        UserDTO userDTO);

        /**
         * @param refreshJwtRequestDTO
         * @return
         */
        ResponseEntity<ApplicantUserAuthenticationDTO> refresh(String locale,
        RefreshJwtRequestDTO refreshJwtRequestDTO);

        boolean isAuthorized(String username);

        /**
         * @param modelAndView
         * @param confirmationToken
         * @return
         */
        ModelAndView confirmUserAccount(ModelAndView modelAndView, String confirmationToken);

        /**
         * @param locale
         * @param authorization
         * @param username
         * @return
         */
        ResponseEntity<ApplicantUserRegisterDetails> applicantProfile(String locale, String authorization,
                        String username);

        /**
         * @param locale
         * @param authorization
         * @param userRegisterDetailsDTO
         * @return
         */
        ResponseEntity<ApiResultDTO> modifyRegisteruser(String locale, String authorization,
                        ApplicantUserRegisterDetailsDTO userRegisterDetailsDTO);

        /**
         * @param locale
         * @param authorization
         * @param emailid
         * @param address
         * @return
         */
        ResponseEntity<ApiResultDTO> forgetPassword(String locale, String authorization, String emailid,
                        String address);

        /**
         * @param locale
         * @param authorization
         * @param userDTO
         * @return
         */
        ResponseEntity<ApplicantUserAuthenticationDTO> updateCredentials(String locale, String authorization,
                        UserDTO userDTO);

}
