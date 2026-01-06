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
@Getter
@Setter
@NoArgsConstructor
public class SystemFAQSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 3047298062665364528L;
    
    /**
     * 
     */
    private String question;
    
    /**
     * 
     */
    private String answer;
    
    /**
     * 
     */
    private String status;

}
