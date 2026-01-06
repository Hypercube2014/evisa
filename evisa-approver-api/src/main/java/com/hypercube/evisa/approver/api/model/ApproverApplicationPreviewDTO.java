package com.hypercube.evisa.approver.api.model;

import java.util.Date;

import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.model.AttachmentUrlList;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ApproverApplicationPreviewDTO {

	/**
	 * 
	 */
	private String applicationNumber;

	/**
	 * 
	 */
	private Date visaExpiryDate;

	/**
	 * 
	 */
	private boolean penality;

	/**
	 * 
	 */
	private long penalityAmount;

	/**
	 * 
	 */
	private boolean extensionApplied;

	/**
	 * 
	 */
	private ApplicantPersonalDetails applicantPersonalDetails;

	/**
	 * 
	 */
	private ApplicantPassportTravelDetails applicantPassportTravelDetails;

	/**
	 * 
	 */
	private AttachmentUrlList applicationAttachmentDetailsList;

	/**
	 * 
	 */
	private ApproverHistoryDetailsDTOList approverHistoryDetailsDTOList;

	/**
	 * @param applicantPersonalDetails
	 * @param applicantPassportTravelDetails
	 * @param applicationAttachmentDetailsList
	 * @param approverHistoryDetailsDTOList
	 */
	public ApproverApplicationPreviewDTO(ApplicantPersonalDetails applicantPersonalDetails,
			ApplicantPassportTravelDetails applicantPassportTravelDetails,
			AttachmentUrlList applicationAttachmentDetailsList,
			ApproverHistoryDetailsDTOList approverHistoryDetailsDTOList) {
		super();
		this.applicantPersonalDetails = applicantPersonalDetails;
		this.applicantPassportTravelDetails = applicantPassportTravelDetails;
		this.applicationAttachmentDetailsList = applicationAttachmentDetailsList;
		this.approverHistoryDetailsDTOList = approverHistoryDetailsDTOList;
	}

	/**
	 * @param applicationNumber
	 * @param visaExpiryDate
	 * @param extensionApplied
	 * @param penality
	 * @param penalityAmount
	 * @param applicantPersonalDetails
	 * @param applicantPassportTravelDetails
	 * @param applicationAttachmentDetailsList
	 */
	public ApproverApplicationPreviewDTO(String applicationNumber, Date visaExpiryDate, boolean extensionApplied,
			boolean penality, long penalityAmount, ApplicantPersonalDetails applicantPersonalDetails,
			ApplicantPassportTravelDetails applicantPassportTravelDetails,
			AttachmentUrlList applicationAttachmentDetailsList) {
		super();
		this.applicationNumber = applicationNumber;
		this.visaExpiryDate = visaExpiryDate;
		this.extensionApplied = extensionApplied;
		this.penality = penality;
		this.penalityAmount = penalityAmount;
		this.applicantPersonalDetails = applicantPersonalDetails;
		this.applicantPassportTravelDetails = applicantPassportTravelDetails;
		this.applicationAttachmentDetailsList = applicationAttachmentDetailsList;
	}

}
