/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.ApplicantUserLogin;
import com.hypercube.evisa.common.api.domain.ApplicantVerificationToken;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantVerificationTokenService {
    
    /**
     * @param verificationToken
     * @return
     */
    ApplicantUserLogin getUser(String verificationToken);

    /**
     * @param user
     * @param token
     */
    void createVerificationToken(ApplicantUserLogin user, String token, int expiry);

    /**
     * @param VerificationToken
     * @return
     */
    ApplicantVerificationToken getVerificationToken(String VerificationToken);

}
