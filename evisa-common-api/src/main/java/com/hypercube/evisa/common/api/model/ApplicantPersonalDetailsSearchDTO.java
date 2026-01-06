/**
 * 
 */
package com.hypercube.evisa.common.api.model;

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
public class ApplicantPersonalDetailsSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -8749469708877418318L;

    /**
     * 
     */
    private String applicationNumber;

    /**
     * 
     */
    private String fileNumber;
    
    /**
     * 
     */
    private String appliedFor;

    /**
     * 
     */
    private String givenName;

    /**
     * 
     */
    private String emailAddress;

    /**
     * @param applicationNumber
     * @param fileNumber
     * @param appliedFor
     * @param givenName
     * @param emailAddress
     */
    public ApplicantPersonalDetailsSearchDTO(String applicationNumber, String fileNumber, String appliedFor,
            String givenName, String emailAddress) {
        super();
        this.applicationNumber = applicationNumber;
        this.fileNumber = fileNumber;
        this.appliedFor = appliedFor;
        this.givenName = givenName;
        this.emailAddress = emailAddress;
    }

}
