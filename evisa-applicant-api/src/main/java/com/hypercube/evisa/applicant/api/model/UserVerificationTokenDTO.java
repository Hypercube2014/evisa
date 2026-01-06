/**
 * 
 */
package com.hypercube.evisa.applicant.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class UserVerificationTokenDTO {

    /**
     * 
     */
    private String token;

    /**
     * 
     */
    private String locale;

    /**
     * @param token
     * @param locale
     */
    public UserVerificationTokenDTO(String token, String locale) {
        super();
        this.token = token;
        this.locale = locale;
    }

}
