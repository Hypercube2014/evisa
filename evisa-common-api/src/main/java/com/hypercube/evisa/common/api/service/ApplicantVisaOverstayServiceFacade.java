package com.hypercube.evisa.common.api.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;

public interface ApplicantVisaOverstayServiceFacade {

	ChargeResponseDTO applyVisaOverstay(HttpServletRequest request, VisaCheckOverstayDTO visaDto) throws IOException;

}
