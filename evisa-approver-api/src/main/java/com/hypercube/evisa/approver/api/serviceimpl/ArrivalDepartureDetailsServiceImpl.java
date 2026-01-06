package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;
import com.hypercube.evisa.approver.api.repository.ArrivalDepartureDetailsRepository;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
@Service
public class ArrivalDepartureDetailsServiceImpl implements ArrivalDepartureDetailsService {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ArrivalDepartureDetailsRepository arrDepDtlsRepo;

	/**
	 *
	 */
	@Override
	public ArrivalDepartureDetails saveArrivalDepartureDtls(ArrivalDepartureDetails arrDepDtls) {
		log.info("ArrivalDepartureDetailsServiceImpl-saveArrivalDepartureDtls");

		return arrDepDtlsRepo.save(arrDepDtls);
	}

	/**
	 *
	 */
	@Override
	public ArrivalDepartureDetails findArrDepDtlsById(Long id) {
		log.info("ArrivalDepartureDetailsServiceImpl-findArrDepDtlsById");
		return arrDepDtlsRepo.findByArrDepId(id);
	}

	/**
	 *
	 */
	@Override
	public List<ArrivalDepartureDetails> findByApplicationNumber(String applicationNumber) {
		log.info("ArrivalDepartureDetailsServiceImpl-findByApplicationNumber");
		return arrDepDtlsRepo.findByApplicationNumberOrderByArrDepIdDesc(applicationNumber);
	}

}
