package com.hypercube.evisa.approver.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_EMPLOYEE_DETAILS")
@NoArgsConstructor
public class EmployeeDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7253410732272467667L;

	/**
	 * 
	 */
	@Id
	@Column(name = "USERNAME", length = 30, nullable = false, unique = true)
	private String username;

	/**
	 * 
	 */
	@Column(name = "FULL_NAME", length = 255, nullable = false)
	private String fullName;

	/**
	 * 
	 */
	@Column(name = "FATHER_NAME", length = 255, nullable = false)
	private String fatherName;

	/**
	 * 
	 */
	@Column(name = "DOB", nullable = false)
	private String dateOfBirth;

	/**
	 * 
	 */
	@Column(name = "GENDER", length = 1, nullable = false)
	private String gender;

	/**
	 * 
	 */
	@Column(name = "MOBILE_NO", length = 15, nullable = false)
	private String mobileNo;

	/**
	 * 
	 */
	@Column(name = "EMAIL", length = 100, nullable = false, unique = true)
	private String email;

	/**
	 * 
	 */
	@Column(name = "DOJ", nullable = false)
	private String dateOfJoining;

	/**
	 * 
	 */
	@Column(name = "EMERGENCY_CONTACT_NAME", length = 255, nullable = true)
	private String emergencyContactName;

	/**
	 * 
	 */
	@Column(name = "EMERGENCY_CONTACT_NUMBER", length = 15, nullable = true)
	private String emergencyContactNumber;

	/**
	 * 
	 */
	@Column(name = "PLACE_OF_BIRTH", length = 100, nullable = true)
	private String placeOfBirth;

	/**
	 * 
	 */
	@Column(name = "COUNTRY_OF_BIRTH", length = 3, nullable = true)
	private String countryOfBirth;

	/**
	 * 
	 */
	@Column(name = "PHYSICALLY_CHALLANGED")
	private boolean physicallyChallanged;

	/**
	 * 
	 */
	@Column(name = "PERSONAL_EMAIL", length = 100, nullable = true)
	private String personalEmail;

	/**
	 * 
	 */
	@Column(name = "EMPLOYMENT_STATUS", length = 1, nullable = false)
	private String employementStatus;

	/**
	 * 
	 */
	@Column(name = "BLOOD_GROUP", length = 5, nullable = true)
	private String bloodGroup;

	/**
	 * 
	 */
	@Column(name = "NATIONALITY", length = 3, nullable = true)
	private String nationality;

	/**
	 * 
	 */
	@Column(name = "ROLE", length = 10, nullable = false)
	private String role;

	/**
	 * 
	 */
	@Column(name = "REPORTING_TO", length = 30, nullable = true)
	private String reportingTo;

	/**
	 * 
	 */
	@Column(name = "CREATED_BY", length = 25, nullable = false)
	private String createdBy;

	/**
	 * 
	 */
	@CreationTimestamp
	@Column(name = "CREATED_DATE", insertable = true, updatable = false)
	private Date createdDate;

	/**
	 * 
	 */
	@Column(name = "UPDATED_BY", length = 25, nullable = true)
	private String updatedBy;

	/**
	 * 
	 */
	@UpdateTimestamp
	@Column(name = "UPDATED_DATE", insertable = false, updatable = true)
	private Date updatedDate;

	/**
	 * 
	 */
	@Transient
	private String profilePic;

	/**
	 * @param username
	 * @param fullName
	 * @param email
	 */
	public EmployeeDetails(String username, String fullName, String email) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.email = email;
	}

	/**
	 * @param username
	 * @param fullName
	 * @param email
	 * @param role
	 * @param employementStatus
	 */
	public EmployeeDetails(String username, String fullName, String email, String role, String employementStatus) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.email = email;
		this.role = role;
		this.employementStatus = employementStatus;
	}

}
