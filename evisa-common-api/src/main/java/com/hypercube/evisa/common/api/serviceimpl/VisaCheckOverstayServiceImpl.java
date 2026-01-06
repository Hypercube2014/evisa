package com.hypercube.evisa.common.api.serviceimpl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;
import com.hypercube.evisa.common.api.repository.VisaCheckOverstayRepository;
import com.hypercube.evisa.common.api.service.ApplicantPaymentService;
import com.hypercube.evisa.common.api.service.ProductConfigService;
import com.hypercube.evisa.common.api.service.VisaCheckOverstayService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Service
@Data
@Slf4j
public class VisaCheckOverstayServiceImpl implements VisaCheckOverstayService {

	
    @Autowired(required = true)
	private VisaCheckOverstayRepository  visaCheckoverstayRepository;
    
    
  	/**
  	 * 
  	 */
  	@Autowired(required = true)
  	private ProductConfigService productConfigService;
  	
  	ApplicantPaymentService applicantPaymentService;
	
	@Override
	public Page<VisaCheckOverstayDTO> searchVisaCheckoverstay(VisaCheckOverstayDTO visaCheckOverstayDTO) {
		// TODO Auto-generated method stub
		
		 log.info("VisaCheckOverstayServiceImpl-searchVisaCheckoverstay");
	        
	  System.out.println("la liste des visa overstay"+visaCheckoverstayRepository.searchVisaCheckOverstay(visaCheckOverstayDTO));
	  
	  
	        
	        return visaCheckoverstayRepository.searchVisaCheckOverstay(visaCheckOverstayDTO);
	
	}

	@Override
	public VisaCheckOverstayDTO findByapplicationNumber(String applicationNumber){
		 log.info("VisaCheckOverstayServiceImpl-findByapplicationNumber");
		 VisaCheckOverstayDTO dto= new VisaCheckOverstayDTO();
		   try {
			   ApplicationOverStayDetails application=visaCheckoverstayRepository.findByApplicationNumber(applicationNumber);
			      
			      dto.setApplicationNumber(application.getApplicationNumber());
			      dto.setFileNumber(application.getFileNumber());
			      dto.setLoggedUser(application.getUsername());
			      dto.setMiddlename(application.getMiddle_name()); 
			      dto.setSurname(application.getSurname());
			      dto.setVisa_expiry(application.getVisaExpiry());
			   
				
		   }catch(Exception e) {
			   System.out.println(e.getMessage());
		   }    
		        return  dto;
	}
	
	
	public VisaCheckOverstayDTO estimatePenality(VisaCheckOverstayDTO dto) {
		System.out.println("dto.getDepartedestimate()"+dto.getPenalityAmount());
		System.out.println("dto.getDepartedestimate()"+dto.getDepartedestimate());
		
		long daysDiff = 0;
		double penaltyAmount = 0;
		double penalityPaid=0;
		VisaCheckOverstayDTO visadto= new VisaCheckOverstayDTO();
				long timeDiff = 0;
				long departedDate = dto.getDepartedestimate().getTime();
				// long visaToDate = (expiredDate == null ? appHeader.getVisaExpiry() :
				// expiredDate).getTime();
				long currentDate = new Date().getTime();
				
				System.out.println(departedDate);
				System.out.println(currentDate);
				//long visaToDate = currentDate.getTime();
				if (departedDate > currentDate) {
					System.out.println("dans if");
					timeDiff = departedDate - currentDate;
				}
				daysDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
				log.info("diffDays -=-=-=-=- {}", daysDiff);
                    System.out.println("daysDiff"+daysDiff);
				if (daysDiff > 0) {
					/* need to calculate the penalty amount */
					String penaltyValue = productConfigService.getConfigValueByConfigCode("VISA_PNLTY_CHRE");
					log.info("penaltyValue VISA_PNLTY_CHRE -=-=-=- {}", penaltyValue);
					penaltyAmount = daysDiff * Integer.parseInt(penaltyValue != null ? penaltyValue : "20");
					
					penalityPaid=penaltyAmount+dto.getPenalityAmount();
				}
				log.info("penaltyAmount -=-=-=-=- {}", penaltyAmount);
	
		
		visadto=dto;
		visadto.setPenalityAmount(penalityPaid);		
		System.out.println("visadto.getPenalityAmount()"+visadto.getPenalityAmount());
		return visadto;
	}


}
