/**
 * 
 */
package com.hypercube.evisa.applicant.api.listener;

import java.util.Locale;


import org.springframework.context.ApplicationEvent;

import com.hypercube.evisa.common.api.domain.ApplicantUserLogin;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -6015156424062015091L;

    /**
     * 
     */
    private String appUrl;
    /**
     * 
     */
    private Locale locale;
    /**
     * 
     */
    private ApplicantUserLogin user;
    
    /**
     * @param appUrl
     * @param locale
     * @param user
     */
    public OnRegistrationCompleteEvent(ApplicantUserLogin user, Locale locale, String appUrl) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
    }

}
