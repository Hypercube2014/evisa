/**
 * 
 */
package com.hypercube.evisa.applicant.api.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hypercube.evisa.applicant.api.constants.UtilParamPropertyConstants;

/**
 * @author SivaSreenivas
 *
 */
@Component
public class UtilParamConfigDTO {
    
    /**
     * 
     */
    public UtilParamConfigDTO() {
        super();
    }
    
    /**
     * @param userPwdExpiryFlag
     */
    @Value("${applicant.userpassword.expiry.flag}")
    private void setUserPwdExpiryFlag(String userPwdExpiryFlag) {
        UtilParamPropertyConstants.setUserPwdExpiryFlag(userPwdExpiryFlag);
    }
    
    /**
     * @param userAccExpiryFlag
     */
    @Value("${applicant.useraccount.expiry.flag}")
    private void setUserAccFlag(String userAccExpiryFlag) {
        UtilParamPropertyConstants.setUserAccExpiryFlag(userAccExpiryFlag);
    }

}
