package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "TEVI_APP_PAYMENT")
public class ApplicantPaymentDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 407689739159554951L;

    /**
     * 
     */
    @Id
    @Column(name = "FILE_NUMBER", length = 25, nullable = false)
    private String fileNumber;

    /**
     * 
     */
    @Column(name = "PAYMENT_CURRENCY", length = 3, nullable = false)
    private String paymentCurrency;

    /**
     * 
     */
    @Column(name = "AMOUNT_DUE", length = 25, nullable = false)
    private Long amountDue;

    /**
     * 
     */
    @Column(name = "AMOUNT_PAID", length = 25, nullable = true)
    private Long amountPaid;

    /**
     * 
     */
    @Column(name = "INSTR_TYPE", length = 10, nullable = false)
    private String instrType;

    /**
     * 
     */
    @Column(name = "PAYMENT_METHOD", length = 4, nullable = true)
    private String paymentMethod;

    /**
     * 
     */
    @Column(name = "PAYMENT_INSTRUCTIONS", length = 100, nullable = true)
    private String paymentInstructions;

    /**
     * 
     */
    @Column(name = "STATUS", length = 2, nullable = false)
    private String status;

    /**
     * 
     */
    @Column(name = "USERNAME", length = 25, nullable = false)
    private String username;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * @param fileNumber
     * @param paymentCurrency
     * @param amountDue
     * @param amountPaid
     * @param instrType
     * @param status
     * @param username
     */
    public ApplicantPaymentDetails(String fileNumber, String paymentCurrency, Long amountDue, Long amountPaid,
            String instrType, String status, String username,String paymentInstructions) {
        super();
        this.fileNumber = fileNumber;
        this.paymentCurrency = paymentCurrency;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.instrType = instrType;
        this.status = status;
        this.username = username;
        this.paymentInstructions= paymentInstructions;
    }

    /**
     * @param fileNumber
     * @param instrType
     */
    public ApplicantPaymentDetails(String fileNumber, String instrType) {
        super();
        this.fileNumber = fileNumber;
        this.instrType = instrType;
    }
    
    public ApplicantPaymentDetails(String fileNumber, String paymentCurrency, Long amountDue, Long amountPaid,
            String instrType, String status, String username) {
        super();
        this.fileNumber = fileNumber;
        this.paymentCurrency = paymentCurrency;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.instrType = instrType;
        this.status = status;
        this.username = username;
    }


}
