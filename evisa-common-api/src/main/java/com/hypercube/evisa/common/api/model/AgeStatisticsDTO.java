package com.hypercube.evisa.common.api.model;

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
public class AgeStatisticsDTO {

    /**
     * 
     */
    private Long age0;

    /**
     * 
     */
    private Long age21;

    /**
     * 
     */
    private Long age41;

    /**
     * 
     */
    private Long age61;

    /**
     * @param age0
     * @param age21
     * @param age41
     * @param age61
     */
    public AgeStatisticsDTO(Long age0, Long age21, Long age41, Long age61) {
        super();
        this.age0 = age0;
        this.age21 = age21;
        this.age41 = age41;
        this.age61 = age61;
    }

}
