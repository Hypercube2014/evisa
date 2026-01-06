package com.hypercube.evisa.approver.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author MohamedKhaireh
 *
 */
@Getter
@Setter
public class ArrivalDepartureHistoryRequestDTO {
    
    /**
     * 
     */
    private String loggedUser;
    
    /**
     * 
     */
    private String period;

}

