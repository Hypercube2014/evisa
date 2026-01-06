package com.hypercube.evisa.approver.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "TEVI_ARR_DEP_HISTORY")
@Data
@NoArgsConstructor
public class ArrivalDepartureHistoryDetails {

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "arr_dep_history_seq")
	@SequenceGenerator(name = "arr_dep_history_seq", sequenceName = "arr_dep_history_seq", allocationSize = 1)
	@Column(name = "ID", length = 25)
	private Long id;

	/**
	 * 
	 */
	@Column(name = "arr_dep_id", length = 25, nullable = false)
	private Long arrDepId;

	/**
	 * 
	 */
	@Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
	private String applicationNumber;

	/**
	 * 
	 */
	@Column(name = "OPR_TYPE", length = 1, nullable = false)
	private String oprType;

	/**
	 * 
	 */
	@Column(name = "APPROVER", length = 25, nullable = false)
	private String approver;

	/**
	 * 
	 */
	@Column(name = "APPROVER_ROLE", length = 10, nullable = false)
	private String approverRole;

	/**
	 * 
	 */
	@Column(name = "STATUS", length = 3, nullable = false)
	private String status;

	/**
	 * 
	 */
	@Column(name = "REMARKS", length = 255, nullable = true)
	private String remarks;

	/**
	 * 
	 */
	@CreationTimestamp
	@Column(name = "ACTION_DATE", insertable = true, updatable = false)
	private Date actionDate;

	/**
	 * @param arrDepId
	 * @param applicationNumber
	 * @param oprType
	 * @param approver
	 * @param approverRole
	 * @param status
	 * @param remarks
	 */
	public ArrivalDepartureHistoryDetails(Long arrDepId, String applicationNumber, String oprType, String approver,
			String approverRole, String status, String remarks) {
		super();
		this.arrDepId = arrDepId;
		this.applicationNumber = applicationNumber;
		this.oprType = oprType;
		this.approver = approver;
		this.approverRole = approverRole;
		this.status = status;
		this.remarks = remarks;
	}

}
