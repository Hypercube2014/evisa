package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;


public interface VisaCheckOverstayRepo {
	Page<VisaCheckOverstayDTO> searchVisaCheckOverstay(VisaCheckOverstayDTO visaCheckDTO);
}
