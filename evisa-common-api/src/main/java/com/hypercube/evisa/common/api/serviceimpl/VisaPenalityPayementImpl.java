package com.hypercube.evisa.common.api.serviceimpl;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.hypercube.evisa.common.api.domain.PayementCash;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;
import com.hypercube.evisa.common.api.repository.PayementCashRepository;
import com.hypercube.evisa.common.api.service.VisaPenalityPayment;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class VisaPenalityPayementImpl implements VisaPenalityPayment{

	@Autowired(required= true)
	private PayementCashRepository repo;
	
	@Override
	public PayementCash applypayements(VisaCheckOverstayDTO dto) {
		log.info("VisaPenalityPayementImpl--applypayements");
		  PayementCash cash=new PayementCash(dto.getApplicationNumber(),dto.getFileNumber(),dto.getPenalityAmount(),dto.getLoggedUser());  
		
		System.out.println("Enregistre");
		

		
				return repo.save(cash);
	}
	
	
	public PayementCash  selectpayementCash(String applicationNumber){
		PayementCash payementCash = null;
		try {
	       payementCash= repo.selectPaymentCash(applicationNumber);
		
		}catch(Exception e ) {
			
		}
		
				
		return payementCash;
	}
	
	@Override
	public PayementCash statistiqueCount(String approver, Date date1,Date date2) {
		log.info("VisaPenalityPayementImpl--statistiqueCount");
		
		PayementCash payementCash = null;
		try {
	       payementCash= repo.statistiqueCount(approver, date1, date2);
		
		}catch(Exception e ) {
			
		}
		
		return payementCash;
	}


	@Override
	public PayementCash StatistiqueSum(String approver, Date date1, Date date2) {
		// TODO Auto-generated method stub
		log.info("VisaPenalityPayementImpl--StatistiqueSum"); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    Date startDate = null;
		    Date endDate = null;
		    
		    try {
		        String startDateString = format.format(date1);
		        String endDateString = format.format(date2);
		        
		        startDate = format.parse(startDateString);
		        endDate = format.parse(endDateString);
		        
		        // Effectuez ici les opérations statistiques sur les dates
		        // et retournez le résultat approprié
		        
		    } catch (ParseException e) {
		        e.printStackTrace();
		        // Gérez l'exception de conversion de date
		        // ou renvoyez une valeur par défaut appropriée
		    }
		
		    System.out.println("startDate"+startDate);
		    System.out.println("endDate"+endDate);
		PayementCash payementCash = null;
		try {
	       payementCash= repo.StatistiqueSum(approver, startDate, endDate);
		
		}catch(Exception e ) {
			
		}
		 return payementCash;
	}
}
