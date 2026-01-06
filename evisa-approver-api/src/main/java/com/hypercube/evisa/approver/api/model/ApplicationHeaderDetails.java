package com.hypercube.evisa.approver.api.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mohamed Khaireh
 *
 */

@Entity
@Table(name = "tevi_application_header")
@Getter
@Setter
public class ApplicationHeaderDetails {
    @Id

    @Column(name = "application_number")
    private Long applicationNumber;

    @Column(name = "allocated_date")
    private Timestamp allocatedDate;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "closed_date")
    private Timestamp closedDate;

    @Column(name = "document_status")
    private String documentStatus;

    @Column(name = "file_number")
    private String fileNumber;

    @Column(name = "organisation_category")
    private String organisationCategory;

    @Column(name = "organisation_name")
    private String organisationName;

    @Column(name = "sponsor_name")
    private String sponsorName;

    @Column(name = "submitted_date")
    private String submittedDate;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "visa_status")
    private String visaStatus;

    @Column(name = "visa_valid_duration")
    private String visaValidDuration;

    @Column(name = "visa_valid_from")
    private Timestamp visaValidFrom;

    @Column(name = "visa_valid_to")
    private Timestamp visaValidTo;

    @Column(name = "arr_dep_indicator")
    private String arrDepIndicator;

    @Column(name = "arr_dep_id")
    private String arrDepId;

    @Column(name = "extension_applied")
    private String extensionApplied;

    @Column(name = "visa_expiry")
    private String visaExpiry;

    @Column(name = "statuspayement_penality")
    private String statuspayementPenality;

    @Column(name = "closed_date_for_avg_stat")
    private Timestamp closedDateForAvgStat;


    public ApplicationHeaderDetails() {
    }



    public ApplicationHeaderDetails(Long applicationNumber, Timestamp allocatedDate, String businessAddress, Timestamp closedDate, String documentStatus, String fileNumber, String organisationCategory, String organisationName, String sponsorName, String submittedDate, String assignedTo, String visaStatus, String visaValidDuration, Timestamp visaValidFrom, Timestamp visaValidTo, String arrDepIndicator, String arrDepId, String extensionApplied, String visaExpiry, String statuspayementPenality, Timestamp closedDateForAvgStat) {
        this.applicationNumber = applicationNumber;
        this.allocatedDate = allocatedDate;
        this.businessAddress = businessAddress;
        this.closedDate = closedDate;
        this.documentStatus = documentStatus;
        this.fileNumber = fileNumber;
        this.organisationCategory = organisationCategory;
        this.organisationName = organisationName;
        this.sponsorName = sponsorName;
        this.submittedDate = submittedDate;
        this.assignedTo = assignedTo;
        this.visaStatus = visaStatus;
        this.visaValidDuration = visaValidDuration;
        this.visaValidFrom = visaValidFrom;
        this.visaValidTo = visaValidTo;
        this.arrDepIndicator = arrDepIndicator;
        this.arrDepId = arrDepId;
        this.extensionApplied = extensionApplied;
        this.visaExpiry = visaExpiry;
        this.statuspayementPenality = statuspayementPenality;
        this.closedDateForAvgStat = closedDateForAvgStat;
    }
    

    @Override
    public String toString() {
        return "{" +
            " applicationNumber='" + getApplicationNumber() + "'" +
            ", allocatedDate='" + getAllocatedDate() + "'" +
            ", businessAddress='" + getBusinessAddress() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", fileNumber='" + getFileNumber() + "'" +
            ", organisationCategory='" + getOrganisationCategory() + "'" +
            ", organisationName='" + getOrganisationName() + "'" +
            ", sponsorName='" + getSponsorName() + "'" +
            ", submittedDate='" + getSubmittedDate() + "'" +
            ", assignedTo='" + getAssignedTo() + "'" +
            ", visaStatus='" + getVisaStatus() + "'" +
            ", visaValidDuration='" + getVisaValidDuration() + "'" +
            ", visaValidFrom='" + getVisaValidFrom() + "'" +
            ", visaValidTo='" + getVisaValidTo() + "'" +
            ", arrDepIndicator='" + getArrDepIndicator() + "'" +
            ", arrDepId='" + getArrDepId() + "'" +
            ", extensionApplied='" + getExtensionApplied() + "'" +
            ", visaExpiry='" + getVisaExpiry() + "'" +
            ", statuspayementPenality='" + getStatuspayementPenality() + "'" +
            ", closedDateForAvgStat='" + getClosedDateForAvgStat() + "'" +
            "}";
    }

}
