package com.hypercube.evisa.approver.api.model;

import java.util.List;

import com.hypercube.evisa.approver.api.domain.ArrivalDepartureHistoryDetails;

import lombok.Data;

@Data
public class ArrivalDepartureHistoryDetailsList {

	private List<ArrivalDepartureHistoryDetails> arrivalDepartureHistoryDetailsDTOs;

	public ArrivalDepartureHistoryDetailsList(List<ArrivalDepartureHistoryDetails> arrivalDepartureHistoryDetailsDTOs) {
		super();
		this.arrivalDepartureHistoryDetailsDTOs = arrivalDepartureHistoryDetailsDTOs;
	}

}
