package com.hypercube.evisa.approver.api.service;

import java.util.List;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;

/**
 * @author SivaSreenivas
 *
 */
public interface ArrivalDepartureDetailsService {

	/**
	 * @param arrDepDtls
	 * @return
	 */
	ArrivalDepartureDetails saveArrivalDepartureDtls(ArrivalDepartureDetails arrDepDtls);

	/**
	 * @param id
	 * @return
	 */
	ArrivalDepartureDetails findArrDepDtlsById(Long id);

	/**
	 * @param applicationNumber
	 * @return
	 */
	List<ArrivalDepartureDetails> findByApplicationNumber(String applicationNumber);

}
