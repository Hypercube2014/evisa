package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.ApplicantUserRegisterDetails;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantUserRegisterDetailsService {

    /**
     * @param userName
     * @return
     */
    boolean verifyUserNameExists(String userName);

    /**
     * @param userRegisterDetails
     * @return
     */
    ApplicantUserRegisterDetails saveUserRegisterDetails(ApplicantUserRegisterDetails userRegisterDetails);

    /**
     * @param username
     * @return
     */
    ApplicantUserRegisterDetails getUserRegisterDetailsByUserName(String username);

    /**
     * @param active
     * @return
     */
    int updateAccountStatus(String active, String username);

    /**
     * @param emailId
     * @return
     */
    boolean verifyEmailExists(String emailId);

    /**
     * @param emailid
     * @return
     */
    ApplicantUserRegisterDetails getUserDetailsByEmailId(String emailid);

}
