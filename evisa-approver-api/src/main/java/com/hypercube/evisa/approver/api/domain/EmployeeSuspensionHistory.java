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
@Table(name = "TEVI_EMP_SUSPENSION_HISTORY")
@Data
@NoArgsConstructor
public class EmployeeSuspensionHistory {

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "suspension_history_seq")
	@SequenceGenerator(name = "suspension_history_seq", sequenceName = "suspension_history_seq", allocationSize = 1)
	@Column(name = "SUSPENSION_ID", length = 25)
	private Long suspensionId;

	/**
	 * 
	 */
	@Column(name = "USERNAME", length = 25, nullable = false)
	private String username;

	/**
	 * 
	 */
	@Column(name = "STATUS")
	private boolean status;

	/**
	 * 
	 */
	@Column(name = "REASONS", length = 10, nullable = false)
	private String reasons;

	/**
	 * 
	 */
	@Column(name = "REMARKS", length = 255, nullable = true)
	private String remarks;

	/**
	 * 
	 */
	@Column(name = "ACTION_BY", length = 25, nullable = false)
	private String actionBy;

	/**
	 * 
	 */
	@CreationTimestamp
	@Column(name = "ACTION_DATE", insertable = true, updatable = false)
	private Date actionDate;

	/**
	 * @param username
	 * @param status
	 * @param reasons
	 * @param remarks
	 * @param actionBy
	 */
	public EmployeeSuspensionHistory(String username, boolean status, String reasons, String remarks, String actionBy) {
		super();
		this.username = username;
		this.status = status;
		this.reasons = reasons;
		this.remarks = remarks;
		this.actionBy = actionBy;
	}

}
