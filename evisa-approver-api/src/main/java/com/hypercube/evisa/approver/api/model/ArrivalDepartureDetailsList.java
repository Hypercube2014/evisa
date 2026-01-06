package com.hypercube.evisa.approver.api.model;

import java.util.List;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureDetails;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ArrivalDepartureDetailsList {

	/**
	 * 
	 */
	private List<ArrivalDepartureDetails> arrivalDepartureDetailsDTOs;

	/**
	 * @param arrivalDepartureDetailsDTOs
	 */
	public ArrivalDepartureDetailsList(List<ArrivalDepartureDetails> arrivalDepartureDetailsDTOs) {
		super();
		this.arrivalDepartureDetailsDTOs = arrivalDepartureDetailsDTOs;
	}

}
