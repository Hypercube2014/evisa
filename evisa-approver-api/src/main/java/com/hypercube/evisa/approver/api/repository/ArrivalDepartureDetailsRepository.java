package com.hypercube.evisa.approver.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ArrivalDepartureDetailsRepository extends JpaRepository<ArrivalDepartureDetails, Long> {

	/**
	 * @param arrDepId
	 * @return
	 */
	ArrivalDepartureDetails findByArrDepId(Long arrDepId);

	/**
	 * @param applicationNumber
	 * @return
	 */
	List<ArrivalDepartureDetails> findByApplicationNumberOrderByArrDepIdDesc(String applicationNumber);

}
