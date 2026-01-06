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

@Entity
@Data
@Table(name = "EMPLOYEE_USER_LOGIN_HISTORY")
@NoArgsConstructor
public class EmployeeUserLoginHistory {

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_LOGIN_HISTORY_SEQ")
	@SequenceGenerator(name = "USER_LOGIN_HISTORY_SEQ", sequenceName = "USER_LOGIN_HISTORY_SEQ", allocationSize = 1)
	@Column(name = "LOGIN_HISTORY_ID", length = 25)
	private Long loginHistoryId;

	/**
	 * 
	 */
	@Column(name = "LOGIN_IP", length = 15, nullable = true)
	private String loginIp;

	/**
	 * 
	 */
	@Column(name = "USER_AGENT", length = 150, nullable = true)
	private String userAgent;

	/**
	 * 
	 */
	@CreationTimestamp
	@Column(name = "LOGIN_TIME", nullable = false, updatable = false)
	private Date loginTime;

	/**
	 * 
	 */
	@Column(name = "username", length = 25)
	private String userName;

	/**
	 * @param loginIp
	 * @param userAgent
	 * @param userName
	 */
	public EmployeeUserLoginHistory(String loginIp, String userAgent, String userName) {
		super();
		this.loginIp = loginIp;
		this.userAgent = userAgent;
		this.userName = userName;
	}

}
