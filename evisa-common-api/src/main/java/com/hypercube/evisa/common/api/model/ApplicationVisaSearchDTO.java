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
public class ApplicationVisaSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 481713011425743829L;
    
    /**
     * 
     */
    private String fileNumber;

    /**
     * 
     */
    private String isExpressVisa;

    /**
     * 
     */
    private String applicantType;

    /**
     * 
     */
    private String visaType;

}
