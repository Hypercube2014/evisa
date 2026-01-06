package com.hypercube.evisa.approver.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureHistoryDetails;

/**
 * @author SivaSreenivas
 *
 */

@Repository
public interface ArrivalDepartureHistoryDetailsRepository extends JpaRepository<ArrivalDepartureHistoryDetails, Long> {

	List<ArrivalDepartureHistoryDetails> findByApplicationNumber(String applicationNumber);

}
