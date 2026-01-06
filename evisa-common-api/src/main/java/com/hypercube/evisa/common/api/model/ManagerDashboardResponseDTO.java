package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

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
public class ManagerDashboardResponseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 957207145061917564L;

    /**
     * 
     */
    private AgentTrackerDTO agentTrackerDTO;

    /**
     * 
     */
    private ImmigrationOfficerStatisticsDTO dmmStatistics;
    
    /**
     * 
     */
    private ApplicationTrackerStatsAgeCountry statsAgeContry;

    /**
     * @param agentTrackerDTO
     * @param dmmStatistics
     */
    public ManagerDashboardResponseDTO(AgentTrackerDTO agentTrackerDTO, ImmigrationOfficerStatisticsDTO dmmStatistics,ApplicationTrackerStatsAgeCountry statsAgeContry) {
        super();
        this.agentTrackerDTO = agentTrackerDTO;
        this.dmmStatistics = dmmStatistics;
        this.statsAgeContry= statsAgeContry;
    }

    public ManagerDashboardResponseDTO(ImmigrationOfficerStatisticsDTO dmmStatistics) {
        super();
       this.dmmStatistics = dmmStatistics;
    }

}
