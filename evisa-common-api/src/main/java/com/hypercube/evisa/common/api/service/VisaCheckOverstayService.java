package com.hypercube.evisa.common.api.service;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;

public interface VisaCheckOverstayService {
	
	Page<VisaCheckOverstayDTO> searchVisaCheckoverstay(VisaCheckOverstayDTO visaExtensionCheckSearchDTO);
	
	VisaCheckOverstayDTO findByapplicationNumber(String applicationNumber);
	
	
	public VisaCheckOverstayDTO estimatePenality(VisaCheckOverstayDTO dto);

}
