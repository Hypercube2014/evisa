package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;
import com.hypercube.evisa.approver.api.domain.ArrivalDepartureHistoryDetails;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureDetailsDTO;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureDetailsList;
import com.hypercube.evisa.approver.api.model.ArrivalDepartureHistoryDetailsList;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureDetailsService;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureHistoryDetailsService;
import com.hypercube.evisa.approver.api.service.ArrivalDepartureServiceFacade;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ApplicationHeader;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class ArrivalDepartureServiceFacadeImpl implements ArrivalDepartureServiceFacade {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ModelMapper modelMapper;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ApplicationHeaderService applicationHeaderService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ArrivalDepartureDetailsService arrDepDtlsService;

	/**
	 * 
	 */
	@Autowired(required = true)
	private ArrivalDepartureHistoryDetailsService arrDepHisDtlsService;

	/**
	 *
	 */
	@Override
	@Transactional
	public ApiResultDTO processArrival(ArrivalDepartureDetailsDTO arrDepDtlsDTO) {
		log.info("ArrivalDepartureServiceFacadeImpl-processArrival");

		ApiResultDTO apiResultDTO;
		String arrDepIndicator = null;
		ArrivalDepartureDetails arrDepDtls;
		try {

			if ("APR".equals(arrDepDtlsDTO.getStatus())) {
				arrDepIndicator = "PD";
			} else if ("VAL".equals(arrDepDtlsDTO.getStatus())) {
				arrDepIndicator = "AV";
			} else {
				arrDepIndicator = "AR";
			}

			/* Verify the header */
			if (arrDepDtlsDTO.getId() == null) {
				arrDepDtls = new ArrivalDepartureDetails(arrDepDtlsDTO.getApplicationNumber(),
						arrDepDtlsDTO.getLocation(), arrDepDtlsDTO.getCarrierNo(), arrDepDtlsDTO.getStatus());
			} else {
				arrDepDtls = arrDepDtlsService.findArrDepDtlsById(arrDepDtlsDTO.getId());
				arrDepDtls.setArrLocation(arrDepDtlsDTO.getLocation());
				arrDepDtls.setArrCarrierNo(arrDepDtlsDTO.getCarrierNo());
				arrDepDtls.setArrStatus(arrDepDtlsDTO.getStatus());
			}
			arrDepDtls.setArrivedDate(!"VAL".equals(arrDepDtlsDTO.getStatus()) ? new Date() : null);

			ArrivalDepartureDetails resultDetails = arrDepDtlsService.saveArrivalDepartureDtls(arrDepDtls);

			arrDepHisDtlsService.saveArrivalDepartureHistoryDetails(new ArrivalDepartureHistoryDetails(
					resultDetails.getArrDepId(), arrDepDtlsDTO.getApplicationNumber(), arrDepDtlsDTO.getOprType(),
					arrDepDtlsDTO.getLoggeduser(), arrDepDtlsDTO.getRole(), arrDepDtlsDTO.getStatus(),
					arrDepDtlsDTO.getRemarks()));

			if ("APR".equals(resultDetails.getArrStatus())) {
				// get visa header details to update departure date expire
				// approved date + visa validity days <= visa valid To
				ApplicationHeader appHeader = applicationHeaderService
						.getHeaderApplication(arrDepDtlsDTO.getApplicationNumber());
				Date visaExpiryDate = CommonsUtil.addDays(arrDepDtls.getArrivedDate(),
						appHeader.getVisaValidDuration());
				if (visaExpiryDate.compareTo(appHeader.getVisaValidTo()) > 0) {
					visaExpiryDate = appHeader.getVisaValidTo();
				}
				// update application header details
				applicationHeaderService.updateVisaExpiryAlongWithArrivalDtls(arrDepDtlsDTO.getApplicationNumber(),
						arrDepIndicator, resultDetails.getArrDepId(), visaExpiryDate);
			} else {
				applicationHeaderService.updateArrivalDepartureIndicator(arrDepDtlsDTO.getApplicationNumber(),
						arrDepIndicator, resultDetails.getArrDepId());
			}

			apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Processed Successfully");

		} catch (Exception exe) {
			log.error("processArrivalDeparture-Exception {} {}", exe, exe.getCause());
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "Error While Processing, Please Contact Admin!");
		}
		return apiResultDTO;
	}

	/**
	 * @param arrDepDtlsDTO
	 * @return
	 */
	@Override
	@Transactional
	public ApiResultDTO processDeparture(ArrivalDepartureDetailsDTO arrDepDtlsDTO) {
		log.info("ArrivalDepartureServiceFacadeImpl-processArrival");
		ApiResultDTO apiResultDTO;
		try {

			String arrDepIndicator;
			if ("APR".equals(arrDepDtlsDTO.getStatus())) {
				if (arrDepDtlsDTO.isStayExpired()) {
					arrDepIndicator = "CO";
				} else {
					/* check whether the visa type is single or multiple entry */
					String entryType = applicationHeaderService
							.getEntryTypeByApplicationNumber(arrDepDtlsDTO.getApplicationNumber());
					arrDepIndicator = entryType.equals("S") ? "CO" : "PA";
				}
			} else if ("VAL".equals(arrDepDtlsDTO.getStatus())) {
				arrDepIndicator = "DV";
			} else {
				arrDepIndicator = "DR";
			}

			ArrivalDepartureDetails arrDepDtls = arrDepDtlsService.findArrDepDtlsById(arrDepDtlsDTO.getId());

			arrDepDtls.setDepLocation(arrDepDtlsDTO.getLocation());
			arrDepDtls.setDepCarrierNo(arrDepDtlsDTO.getCarrierNo());
			arrDepDtls.setDepStatus(arrDepDtlsDTO.getStatus());
			arrDepDtls.setDepartedDate("APR".equals(arrDepDtlsDTO.getStatus()) ? new Date() : null);
			arrDepDtlsService.saveArrivalDepartureDtls(arrDepDtls);

			arrDepHisDtlsService.saveArrivalDepartureHistoryDetails(
					new ArrivalDepartureHistoryDetails(arrDepDtlsDTO.getId(), arrDepDtlsDTO.getApplicationNumber(),
							arrDepDtlsDTO.getOprType(), arrDepDtlsDTO.getLoggeduser(), arrDepDtlsDTO.getRole(),
							arrDepDtlsDTO.getStatus(), arrDepDtlsDTO.getRemarks()));

			applicationHeaderService.updateArrivalDepartureIndicator(arrDepDtlsDTO.getApplicationNumber(),
					arrDepIndicator, "APR".equals(arrDepDtlsDTO.getStatus()) ? null : arrDepDtlsDTO.getId());

			apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS, "Processed Successfully");
		} catch (Exception exe) {
			log.error("processArrivalDeparture-Exception {} {}", exe, exe.getCause());
			apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR, "Error While Processing, Please Contact Admin!");
		}
		return apiResultDTO;
	}

	/**
	 *
	 */
	@Override
	public ArrivalDepartureDetailsList fetchArrivalDepartureDetails(String applicationNumber) {
		log.info("ArrivalDepartureServiceFacadeImpl-fetchArrivalDepartureDetails");
		return new ArrivalDepartureDetailsList(arrDepDtlsService.findByApplicationNumber(applicationNumber));
	}

	/**
	 *
	 */
	@Override
	public ArrivalDepartureDetails getArrivalDepartureById(Long arrdepid) {
		log.info("ArrivalDepartureServiceFacadeImpl-getArrivalDepartureById");
		return arrDepDtlsService.findArrDepDtlsById(arrdepid);
	}

	/**
	*
	*/
	@Override
	public ArrivalDepartureHistoryDetailsList arrivalDepartureHistoryDetails(String applicationNumber) {
		log.info("ArrivalDepartureServiceFacadeImpl-fetchArrivalDeparturehistoryDetails");
		return new ArrivalDepartureHistoryDetailsList(
				arrDepHisDtlsService.getArrivalDepartureHistorydetails(applicationNumber));
	}

}
