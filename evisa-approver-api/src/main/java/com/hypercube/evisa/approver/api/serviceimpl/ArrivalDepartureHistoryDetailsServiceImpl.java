package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureHistoryDetails;
import com.hypercube.evisa.approver.api.repository.ArrivalDepartureHistoryDetailsRepository;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureHistoryDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ArrivalDepartureHistoryDetailsServiceImpl implements ArrivalDepartureHistoryDetailsService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ArrivalDepartureHistoryDetailsRepository arrDepHisDtlsRepo;

	/**
	 *
	 */
	@Override
	public ArrivalDepartureHistoryDetails saveArrivalDepartureHistoryDetails(
			ArrivalDepartureHistoryDetails arrDepHisDtls) {
		log.info("ArrivalDepartureHistoryDetailsServiceImpl-saveArrivalDepartureHistoryDetails");
		return arrDepHisDtlsRepo.save(arrDepHisDtls);
	}

	@Override
	public List<ArrivalDepartureHistoryDetails> getArrivalDepartureHistorydetails(String ApplicationNumber) {
		log.info("ArrivalDepartureHistoryDetailsServiceImpl-saveArrivalDepartureHistoryDetails");
		return arrDepHisDtlsRepo.findByApplicationNumber(ApplicationNumber);
	}

}
