package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mohamed Khaireh
 *
 */
@Data
@NoArgsConstructor
public class SBCOTrackerDTO {

    /**
     * 
     */
    private Long approved;

    /**
     * 
     */
    private Long rejected;

    /**
     * @param allocated
     */
    public SBCOTrackerDTO(Long approved, Long rejected) {
        super();
        this.approved = approved;
        this.rejected = rejected;
    }

}
