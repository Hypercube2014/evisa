package com.hypercube.evisa.approver.api.model;

import lombok.Data;

@Data
public class ArrivalDepartureHistoryDetailsDTO {

	private Long id;

	private Long arrDepId;

	private String applicationNumber;

	private String oprType;

	private String approver;

	private String approverRole;

	private String status;

	private String remarks;

}
