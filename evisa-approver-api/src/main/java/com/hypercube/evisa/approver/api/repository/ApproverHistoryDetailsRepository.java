package com.hypercube.evisa.approver.api.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApproverHistoryDetails;


/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApproverHistoryDetailsRepository extends JpaRepository<ApproverHistoryDetails, Long> {

	/**
	 * @param applicationNumber
	 * @return
	 */
	List<ApproverHistoryDetails> findByApplicationNumber(String applicationNumber);

}
