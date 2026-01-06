package com.hypercube.evisa.common.api.model;

import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImmigrationOfficerStatisticsDTO {

	/**
     * 
     */
    List<VisaProcessingDTO> visaProcessingDTOs;

    /**
     * 
     */
    List<CountDTO> travelPurposeStats;

    /**
     * 
     */
    List<CountDTO> countryStats;
    
    /**
     * 
     */
    AgeStatisticsDTO ageStats;
    
    /**
     * 
     */
    List<CountDTO> countryStatsPeriod;
    
    /**
     * 
     */
    AgeStatisticsDTO ageStatsPeriod;

    /**
     * @param visaProcessingDTOs
     * @param travelPurposeStats
     * @param countryStats
     * @param countryStatsPeriod
     * @param ageStats
     * @param ageStatsPeriod
     */
    public ImmigrationOfficerStatisticsDTO(List<VisaProcessingDTO> visaProcessingDTOs,
            List<CountDTO> travelPurposeStats, List<CountDTO> countryStats, AgeStatisticsDTO ageStats) {
      
        this.visaProcessingDTOs = visaProcessingDTOs;
        this.travelPurposeStats = travelPurposeStats;
        this.countryStats = countryStats;
        this.ageStats = ageStats;
    
    }

	

	
  

}
