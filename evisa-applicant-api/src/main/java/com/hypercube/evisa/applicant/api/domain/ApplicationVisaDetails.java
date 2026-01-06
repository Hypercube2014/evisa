package com.hypercube.evisa.applicant.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "vevi_application_visa")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Setter
@Getter
@NoArgsConstructor
public class ApplicationVisaDetails {

    /**
     * 
     */
    @Id
    @Column(name = "FILE_NUMBER")
    private String fileNumber;

    /**
     * 
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 
     */
    @Column(name = "IS_EXPRESS_VISA")
    private boolean isExpressVisa;

    /**
     * 
     */
    @Column(name = "APPLICANT_TYPE")
    private String applicantType;

    /**
     * 
     */
    @Column(name = "VISA_TYPE")
    private String visaType;
    
    /**
     * 
     */
    @Column(name = "VISA_DESCRIPTION")
    private String visaDescription;

    /**
     * 
     */
    @Column(name = "VISA_DESCRIPTION_OTHER")
    private String visaDescriptionOther;

    /**
     * 
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    /**
     * @param fileNumber
     * @param isExpressVisa
     * @param applicantType
     * @param visaType
     * @param visaDescription
     * @param visaDescriptionOther
     * @param status
     */
    public ApplicationVisaDetails(String fileNumber, boolean isExpressVisa, String applicantType, String visaType,
            String visaDescription, String visaDescriptionOther, String status) {
        super();
        this.fileNumber = fileNumber;
        this.isExpressVisa = isExpressVisa;
        this.applicantType = applicantType;
        this.visaType = visaType;
        this.visaDescription = visaDescription;
        this.visaDescriptionOther = visaDescriptionOther;
        this.status = status;
    }

}
