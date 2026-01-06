package com.hypercube.evisa.common.api.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ReportsSearchDTO extends GenericSearchDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -2286083042768018069L;

    /**
     * 
     */
    private String period;

    /**
     * 
     */
    private String reportType;

    /**
     * 
     */
    private List<String> agentList;

    /**
     * @param agentList
     */
    public ReportsSearchDTO(List<String> agentList) {
        super();
        this.agentList = agentList;
    }

}
