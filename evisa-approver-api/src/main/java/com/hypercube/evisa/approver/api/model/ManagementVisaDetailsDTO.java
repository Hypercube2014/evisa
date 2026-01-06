package com.hypercube.evisa.approver.api.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ManagementVisaDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7075878305468637728L;

	/**
	 * 
	 */
	private Long visaId;

	/**
	 * 
	 */
	private String visaType;

	/**
	 * 
	 */
	private String visaDescription;

	/**
	 * 
	 */
	private String visaDescriptionOther;

	/**
	 * 
	 */
	private int visaDuration;

	/**
	 * 
	 */
	private String entryType;

	/**
	 * 
	 */
	private Date visaDurationFrom;

	/**
	 * 
	 */
	private Date visaDurationTo;

	/**
	 * 
	 */
	private Long visaFee;

	/**
	 * 
	 */
	private boolean isExpressVisaAllowed;

	/**
	 * 
	 */
	private Long expressVisaFee;

	/**
	 * 
	 */
	private String currency;

	/**
	 * 
	 */
	private String createdBy;

	/**
	 * 
	 */
	private String updatedBy;

	/**
	 * 
	 */
	private String status;

}
