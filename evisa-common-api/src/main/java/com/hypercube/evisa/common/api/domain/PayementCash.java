package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tevi_app_payement_cash")
@Data
@NoArgsConstructor
@DynamicUpdate
public class PayementCash implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tevi_app_payement_cash_id_seq")
    @SequenceGenerator(name = "tevi_app_payement_cash_id_seq", sequenceName = "tevi_app_payement_cash_id_seq", allocationSize = 1)
    @Column(name = "PAYEMENTSCASH_ID", length = 25)
	private long idPayementcash;
	
	@Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
	private String applicationNumber;
	@Column(name = "FILE_NUMBER", length = 25, nullable = false)
	private String fileNumber;
	@Column(name = "AMOUNT", length = 25, nullable = false)
	private double amount;
	@Column(name = "APPROVER", length = 25, nullable = false)
	private String  approver;
	
	@CreationTimestamp
	@Column(name = "CREATED_DATE", insertable = true, updatable = false)
	private Date createdDate;

	/**
	  * 
	 */
	@UpdateTimestamp
	@Column(name = "UPDATED_DATE", insertable = false, updatable = true)
	private Date updatedDate;
	
	
	public PayementCash(String applicationNumber, String fileNumber, double amount,String approver) {
		this.applicationNumber=applicationNumber;
		this.fileNumber=fileNumber;
		this.amount=amount;
		this.approver=approver;
		this.createdDate=new Date();
	}
	
	
	public PayementCash(String applicationNumber,double amount) {
		this.applicationNumber=applicationNumber;
		this.amount=amount;
	}
	
	public PayementCash(long idPayementcash) {
		this.idPayementcash= idPayementcash;
	}
	public PayementCash(double amount) {
		this.amount = amount;
	}

}
