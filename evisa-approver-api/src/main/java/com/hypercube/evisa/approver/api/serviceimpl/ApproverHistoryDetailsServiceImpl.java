package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.repository.ApproverHistoryDetailsRepository;
import com.hypercube.evisa.approver.api.service.ApproverHistoryDetailsService;
import com.hypercube.evisa.common.api.domain.ApproverHistoryDetails;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class ApproverHistoryDetailsServiceImpl implements ApproverHistoryDetailsService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApproverHistoryDetailsRepository approverHistoryDetailsRepository;

	/**
	 *
	 */
	@Override
	public ApproverHistoryDetails saveApproverHistoryDetails(ApproverHistoryDetails approverHistoryDetails) {
		log.info("ApproverHistoryDetailsServiceImpl-saveApproverHistoryDetails");
		return approverHistoryDetailsRepository.save(approverHistoryDetails);
	}

	/**
	 *
	 */
	@Override
	public List<ApproverHistoryDetails> findAllApplicationHistory(String applicationNumber) {
		log.info("ApproverHistoryDetailsServiceImpl-findAllApplicationHistory");
		return approverHistoryDetailsRepository.findByApplicationNumber(applicationNumber);
	}

}
