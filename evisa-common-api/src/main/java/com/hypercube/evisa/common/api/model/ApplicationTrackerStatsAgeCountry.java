package com.hypercube.evisa.common.api.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationTrackerStatsAgeCountry   {


	List<CountDTO> countryStatsPeriod;
	    
	    /**
	     * 
	     */
    AgeStatisticsDTO ageStatsPeriod;

		public ApplicationTrackerStatsAgeCountry(List<CountDTO> countryStatsPeriod, AgeStatisticsDTO ageStatsPeriod) {
			super();
			this.countryStatsPeriod = countryStatsPeriod;
			this.ageStatsPeriod = ageStatsPeriod;
		}
	    
	    
	    
}
