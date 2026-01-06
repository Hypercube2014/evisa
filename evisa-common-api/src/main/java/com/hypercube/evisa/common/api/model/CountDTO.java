package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class CountDTO {

    /**
     * 
     */
    private String statusCode;

    /**
     * 
     */
    private long count;
    
    /**
     * 
     */
    private String agentUsername;

    /**
     * @param statusCode
     * @param count
     */
    public CountDTO(String statusCode, long count) {
        super();
        this.statusCode = statusCode;
        this.count = count;
    }
    
    /**
     * @param agentUsername
     * @param statusCode
     * @param count
     */
    public CountDTO(String agentUsername, String statusCode, long count) {
        super();
        this.agentUsername =agentUsername;
        this.statusCode = statusCode;
        this.count = count;
    }

}
