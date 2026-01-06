package com.hypercube.evisa.common.api.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Getter
@Setter
public class ManagerDashboardRequestDTO {
    
    /**
     * 
     */
    private String loggeduser;
    
    /**
     * 
     */
    private String period;
    
    /**
     * 
     */
    private List<String> countryList;

}
