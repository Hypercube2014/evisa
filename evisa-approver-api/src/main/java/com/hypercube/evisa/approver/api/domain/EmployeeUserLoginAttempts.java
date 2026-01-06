package com.hypercube.evisa.approver.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "employee_user_login_attempts")
@NoArgsConstructor
public class EmployeeUserLoginAttempts {

	/**
	 * 
	 */
	@Id
	@Column(name = "username", length = 25, unique = true)
	private String username;

	/**
	 * 
	 */
	@Column(name = "attempts", length = 2)
	private int attempts;

	/**
	 * 
	 */
	@Column(name = "last_modified")
	private Date lastModified;

	/**
	 * @param username
	 * @param attempts
	 * @param lastModified
	 */
	public EmployeeUserLoginAttempts(String username, int attempts, Date lastModified) {
		this.username = username;
		this.attempts = attempts;
		this.lastModified = lastModified;
	}

}
