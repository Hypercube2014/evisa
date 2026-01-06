package com.hypercube.evisa.approver.api.service;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureDetailsDTO;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureDetailsList;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureHistoryDetailsList;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ArrivalDepartureServiceFacade {

	/**
	 * @param arrDepDtlsDTO
	 * @return
	 */
	ApiResultDTO processArrival(ArrivalDepartureDetailsDTO arrDepDtlsDTO);

	/**
	 * @param arrDepDtlsDTO
	 * @return
	 */
	ApiResultDTO processDeparture(ArrivalDepartureDetailsDTO arrDepDtlsDTO);

	/**
	 * @param applicationNumber
	 * @return
	 */
	ArrivalDepartureDetailsList fetchArrivalDepartureDetails(String applicationNumber);

	/**
	 * @param arrdepid
	 * @return
	 */
	ArrivalDepartureDetails getArrivalDepartureById(Long arrdepid);

	/**
	 * @param applicationNumber
	 * @return
	 */
	ArrivalDepartureHistoryDetailsList arrivalDepartureHistoryDetails(String applicationNumber);

}
