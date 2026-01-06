package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.ApplicantUserLoginAttempts;

/**
 * @author sivasreenivas
 *
 */
public interface ApplicantUserLoginAttemptService {

    /**
     * @param username
     */
    void updateFailAttempts(String username);

    /**
     * @param username
     */
    void resetFailAttempts(String username);

    /**
     * @param username
     * @return
     */
    ApplicantUserLoginAttempts getUserAttempts(String username);

    /**
     * @param status
     * @param username
     */
    void updateUserAccountLock(boolean status, String username);

    /**
     * @param b
     * @param userName
     * @param role
     */
    void updateUserAccountEnableAndRole(boolean result, String userName, String role);

    /**
     * @param accountenabled
     * @param username
     * @return
     */
    void updateUserAccountEnabled(boolean accountenabled, String username);

    /**
     * @param roleId
     * @param loggeduser
     * @param locale
     * @return
     */
    boolean verifyUserName(String userName);
}
