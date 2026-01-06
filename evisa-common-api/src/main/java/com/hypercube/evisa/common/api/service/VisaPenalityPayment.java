package com.hypercube.evisa.common.api.service;
import java.util.Date;

import com.hypercube.evisa.common.api.domain.PayementCash;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;

public interface VisaPenalityPayment {


	PayementCash applypayements(VisaCheckOverstayDTO dto);
	PayementCash  selectpayementCash(String applicationNumber);
	PayementCash statistiqueCount(String approver, Date date1,Date date2);
	PayementCash StatistiqueSum(String approver, Date date1,Date date2);
}
