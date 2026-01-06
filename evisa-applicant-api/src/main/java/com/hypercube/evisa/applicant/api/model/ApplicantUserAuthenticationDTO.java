package com.hypercube.evisa.applicant.api.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class ApplicantUserAuthenticationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4083256839777087972L;

    /**
     * 
     */
    private String status;
    /**
     * 
     */
    private String statusDescription;
    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String completeName;
    /**
     * 
     */
    private String emailId;
    /**
     * 
     */
    private String title;
    /**
     * 
     */
    private String accessToken;

      /**
     * 
     */

    private String refreshToken;
    /**
     * 
     */
    private String profilePic;
    /**
     * 
     */
    private boolean changePasswordRequired;
    /**
     * 
     */
    private String roles;

    /**
     * @param status
     * @param statusDescription
     */
    public ApplicantUserAuthenticationDTO(String status, String statusDescription) {
        super();
        this.status = status;
        this.statusDescription = statusDescription;
    }

}
