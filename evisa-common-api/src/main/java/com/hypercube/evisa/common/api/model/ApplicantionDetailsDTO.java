package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author Mohamed Khaireh
*
*/
@Data
@NoArgsConstructor

public class ApplicantionDetailsDTO {

    /**
     * 
     */
    private Long nonAllocated;

    /**
     * 
     */
    private Long nonClosed;

    /**
     * 
     */
    private Long allocated;

    /**
     * 
     */
    private Long closed;
    
    /**
     * 
     */
    private Long validation;


    public ApplicantionDetailsDTO(long nonAllocated, long nonClosed, long allocated, long closed, long validation) {
        super();
        this.nonAllocated = nonAllocated;
        this.nonClosed = nonClosed;
        this.allocated = allocated;
        this.closed = closed;
        this.validation = validation;
    }


}
