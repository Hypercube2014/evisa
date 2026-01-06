package com.hypercube.evisa.approver.api.model;


import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayementCashDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applicationNumber;
	
	private String fileNumber;
	
	private double amount;
	
	private String  approver;
	
}
