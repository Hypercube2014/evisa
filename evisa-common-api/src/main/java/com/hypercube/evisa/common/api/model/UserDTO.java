/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class UserDTO {
    
    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String secretKey;
    /**
     * 
     */
    private String newsecretkey;
    /**
     * 
     */
    private String userip;
    /**
     * 
     */
    private String useragent;

}
