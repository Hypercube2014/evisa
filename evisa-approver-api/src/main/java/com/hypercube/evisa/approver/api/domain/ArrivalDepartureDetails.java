package com.hypercube.evisa.approver.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "TEVI_ARR_DEP_DETAILS")
@Data
@NoArgsConstructor
public class ArrivalDepartureDetails {

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "arr_dep_dtls_seq")
	@SequenceGenerator(name = "arr_dep_dtls_seq", sequenceName = "arr_dep_dtls_seq", allocationSize = 1)
	@Column(name = "arr_dep_id", length = 25, nullable = true)
	private Long arrDepId;

	/**
	 * 
	 */
	@Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
	private String applicationNumber;

	/**
	 * 
	 */
	@Column(name = "ARR_LOCATION", length = 5, nullable = false)
	private String arrLocation;

	/**
	 * 
	 */
	@Column(name = "ARR_CARRIER_NO", length = 10, nullable = false)
	private String arrCarrierNo;

	/**
	 * 
	 */
	@Column(name = "DEP_LOCATION", length = 5, nullable = true)
	private String depLocation;

	/**
	 * 
	 */
	@Column(name = "DEP_CARRIER_NO", length = 10, nullable = true)
	private String depCarrierNo;

	/**
	 * 
	 */
	@Column(name = "ARRIVED_DATE", nullable = true)
	private Date arrivedDate;

	/**
	 * 
	 */
	@Column(name = "DEPARTED_DATE", nullable = true)
	private Date departedDate;

	/**
	 * 
	 */
	@Column(name = "DEP_PENALITY_EXISTS")
	private boolean depPenalityExists;

	/**
	 * 
	 */
	@Column(name = "DEP_PENALITY_AMOUNT", length = 10, nullable = true)
	private Long depPenalityAmount;

	/**
	 * 
	 */
	@Column(name = "ARR_STATUS", length = 3, nullable = true)
	private String arrStatus;

	/**
	 * 
	 */
	@Column(name = "DEP_STATUS", length = 3, nullable = true)
	private String depStatus;

	/**
	 * @param applicationNumber
	 * @param arrLocation
	 * @param arrCarrierNo
	 * @param arrStatus
	 */
	public ArrivalDepartureDetails(String applicationNumber, String arrLocation, String arrCarrierNo,
			String arrStatus) {
		super();
		this.applicationNumber = applicationNumber;
		this.arrLocation = arrLocation;
		this.arrCarrierNo = arrCarrierNo;
		this.arrStatus = arrStatus;
	}

}
