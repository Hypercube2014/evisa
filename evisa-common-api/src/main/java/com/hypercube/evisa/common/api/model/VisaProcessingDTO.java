package com.hypercube.evisa.common.api.model;

import java.util.List;

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
public class VisaProcessingDTO {

    /**
     * 
     */
    private String month;

    /**
     * 
     */
    private List<CountDTO> visaProcessed;

    /**
     * @param month
     * @param visaProcessed
     */
    public VisaProcessingDTO(String month, List<CountDTO> visaProcessed) {
        super();
        this.month = month;
        this.visaProcessed = visaProcessed;
    }

}
