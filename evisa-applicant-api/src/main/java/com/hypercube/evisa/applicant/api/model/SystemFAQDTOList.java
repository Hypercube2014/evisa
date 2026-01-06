package com.hypercube.evisa.applicant.api.model;

import java.util.List;

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
public class SystemFAQDTOList {

    /**
     * 
     */
    private List<SystemFAQPrintDTO> systemFAQDTOs;

    /**
     * @param systemFAQDTOs
     */
    public SystemFAQDTOList(List<SystemFAQPrintDTO> systemFAQDTOs) {
        super();
        this.systemFAQDTOs = systemFAQDTOs;
    }

}
