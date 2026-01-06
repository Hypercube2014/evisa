package com.hypercube.evisa.approver.api.service;

import java.util.List;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureHistoryDetails;

/**
 * @author SivaSreenivas
 *
 */
public interface ArrivalDepartureHistoryDetailsService {

	/**
	 * @param arrDepHisDtls
	 * @return
	 */
	ArrivalDepartureHistoryDetails saveArrivalDepartureHistoryDetails(ArrivalDepartureHistoryDetails arrDepHisDtls);

	List<ArrivalDepartureHistoryDetails> getArrivalDepartureHistorydetails(String ApplicationNumber);

}
