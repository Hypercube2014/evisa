package com.hypercube.evisa.approver.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MasterCodeTypeDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8746529243910971697L;

	/**
	 * 
	 */
	private Long id;

	/**
	 * 
	 */
	private String codeType;

	/**
	 * 
	 */
	private String description;

	/**
	 * 
	 */
	private String descriptionOther;

	/**
	 * 
	 */
	private String active;

	/**
	 * 
	 */
	private String createdBy;

	/**
	 * 
	 */
	private String updatedBy;

}
