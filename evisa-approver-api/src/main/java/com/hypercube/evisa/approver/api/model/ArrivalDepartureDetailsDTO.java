package com.hypercube.evisa.approver.api.model;

import java.util.Date;

import javax.persistence.Transient;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ArrivalDepartureDetailsDTO {

	/**
	 * 
	 */
	private Long id;

	/**
	 * 
	 */
	private String applicationNumber;

	/**
	 * 
	 */
	private String oprType;

	/**
	 * 
	 */
	private Date actionDate;

	/**
	 * 
	 */
	private String location;

	/**
	 * 
	 */
	private String carrierNo;

	/**
	 * 
	 */
	private String status;

	/**
	 * 
	 */
	@Transient
	private String loggeduser;

	/**
	 * 
	 */
	@Transient
	private String role;

	/**
	 * 
	 */
	@Transient
	private String remarks;

	/**
	 * 
	 */
	private boolean stayExpired;

}
