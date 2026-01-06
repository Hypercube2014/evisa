package com.hypercube.evisa.common.api.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.VisaCheckOverstayRepo;
import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;



@Repository
public interface VisaCheckOverstayRepository extends JpaRepository<ApplicationOverStayDetails, String>, VisaCheckOverstayRepo {

	
	List<ApplicationOverStayDetails> findAll();
	
	
	ApplicationOverStayDetails findByApplicationNumber(String applicationNumber);
}
