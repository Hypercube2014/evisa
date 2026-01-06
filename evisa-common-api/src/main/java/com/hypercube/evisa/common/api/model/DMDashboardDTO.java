package com.hypercube.evisa.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
public class DMDashboardDTO {

    /**
     * 
     */
    private Long allocated;

    /**
     * 
     */
    private Long pending;

    /**
     * 
     */
    private Long approved;

    /**
     * 
     */
    private Long rejected;

    /**
     * 
     */
    private Long validation;

    /**
     * @param allocated
     */
    public DMDashboardDTO(Long allocated) {
        super();
        this.allocated = allocated;
    }

    /**
     * @param allocated
     * @param pending
     * @param approved
     * @param rejected
     * @param validation
     */
    public DMDashboardDTO(Long allocated, Long pending, Long approved, Long rejected, Long validation) {
        super();
        this.allocated = allocated;
        this.pending = pending;
        this.approved = approved;
        this.rejected = rejected;
        this.validation = validation;
    }

}
