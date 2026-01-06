package com.hypercube.evisa.common.api.model;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class VisaCheckOverstayDTO extends GenericSearchDTO{

	
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
    */
   private String applicationNumber;
   
   /**
    * 
    */
   private String fileNumber;
   
   /**
    * 
    */
   private String approver;
   
   /**
    * 
    */
   private Date visa_expiry;
   
 
   /**
    * 
    */
   private String surname;
   
   /**
    * 
    */
   private String middlename;
   
   
   /**
    * 
    */
   
   private double  penalityAmount;


   
   private Date departedestimate;
   
   private Date date1;
   
   private Date date2;
}
