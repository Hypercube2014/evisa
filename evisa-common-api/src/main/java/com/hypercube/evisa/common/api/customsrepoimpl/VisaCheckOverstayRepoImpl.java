package com.hypercube.evisa.common.api.customsrepoimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hypercube.evisa.common.api.customsrepo.VisaCheckOverstayRepo;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.domain.ApplicationOverStayDetails;
import com.hypercube.evisa.common.api.domain.PayementCash;
import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;
import com.hypercube.evisa.common.api.service.ApplicantPaymentService;
import com.hypercube.evisa.common.api.service.ProductConfigService;
import com.hypercube.evisa.common.api.service.VisaPenalityPayment;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class VisaCheckOverstayRepoImpl implements VisaCheckOverstayRepo{

	

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;
    
	/**
	 * 
	 */
	@Autowired(required = true)
	private ProductConfigService productConfigService;
	/**
	 * 
	 */
	@Autowired(required = true)
	ApplicantPaymentService applicantPaymentService;
	
	 @Autowired
	 private VisaPenalityPayment visaPenalityPayment;
    
	@Override
	public Page<VisaCheckOverstayDTO> searchVisaCheckOverstay(VisaCheckOverstayDTO visaCheckoverstayDTO) {
		
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 CriteriaQuery<ApplicationOverStayDetails> cq =getCreateQueryForVisaCheckOverstay(cb);
		 Root<ApplicationOverStayDetails> root= getCreateQueryForVisaCheckOverstay(cq);
		 
		 List<Predicate> predicates = new ArrayList<>();
		 
		 TypedQuery<ApplicationOverStayDetails> q= createPredicatesForVisaCheckOverstay(predicates,
				 visaCheckoverstayDTO, root, cb, cq);
		 
		 int totalRecords= getVisaCheckOverstayResultSize(getVisaCheckOverstayResultList(q));
		 System.out.println("total records "+ totalRecords);
		 
		
	
		 Pageable sortedByidDesc= getPageableForVisaCheckOverstay(visaCheckoverstayDTO,q,Sort.by("applicationNumber"));
		 System.out.println("etVisaCheckOverstayResultList(q)"+ getVisaCheckOverstayResultList(q));
		 
		 List<VisaCheckOverstayDTO> list=new ArrayList<>();
		 
		 for (ApplicationOverStayDetails overstay : getVisaCheckOverstayResultList(q)) {
			 VisaCheckOverstayDTO applica=new VisaCheckOverstayDTO();
			 
				long daysDiff = 0;
				double penaltyAmount = 0;
				double penalityPaid=0;
			 if (totalRecords >0) {
					/*
					 * expiredDate = (appHeader.getExtensionApplied().equals("Y")) ?
					 * appVisaExtensionService.getNewExipredDate(applicationNumber) :
					 * appHeader.getVisaExpiry();
					 */
				    
					long timeDiff = 0;
					long currentDate = new Date().getTime();
					// long visaToDate = (expiredDate == null ? appHeader.getVisaExpiry() :
					// expiredDate).getTime();
					long visaToDate = overstay.getVisaExpiry().getTime();
					if (currentDate > visaToDate) {
						timeDiff = currentDate - visaToDate;
					}
					daysDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
					log.info("diffDays -=-=-=-=- {}", daysDiff);

					if (daysDiff > 0) {
						/* need to calculate the penalty amount */
						String penaltyValue = productConfigService.getConfigValueByConfigCode("VISA_PNLTY_CHRE");
						log.info("penaltyValue VISA_PNLTY_CHRE -=-=-=- {}", penaltyValue);
						penaltyAmount = daysDiff * Integer.parseInt(penaltyValue != null ? penaltyValue : "20");
					}
					log.info("penaltyAmount -=-=-=-=- {}", penaltyAmount);
				}
			 
			    System.out.println("applicationPayementDetails Deusus" +overstay.getApplicationNumber());
			 
			    ApplicantPaymentDetails applicationPayementDetails= applicantPaymentService.fetchApplicantPaymentDetails(overstay.getApplicationNumber());
				 if(applicationPayementDetails != null) {
					 System.out.println("applicationPayementDetails Dans");
					    if (applicationPayementDetails.getFileNumber().equals(overstay.getApplicationNumber())){
					    	penalityPaid =applicationPayementDetails.getAmountPaid();
					    	System.out.println("penalityPaid"+penalityPaid);
					    	penalityPaid=penalityPaid;
					    	System.out.println(penalityPaid);
							  penaltyAmount= penaltyAmount-penalityPaid;
						  }else {
							  
						  }
				    }
				  
				  PayementCash payementcash=visaPenalityPayment.selectpayementCash(overstay.getApplicationNumber());
				  if(payementcash != null) {
					  penaltyAmount = penaltyAmount-payementcash.getAmount();
				  }
						
				 System.out.println("applicationPayementDetails SOUS");
				applica.setApplicationNumber(overstay.getApplicationNumber());
				if(penaltyAmount>0) {
					applica.setPenalityAmount(penaltyAmount);
				}else {
					applica.setPenalityAmount(0);
				}
			
			    applica.setFileNumber(overstay.getFileNumber());
			    applica.setMiddlename(overstay.getMiddle_name());
			    applica.setSurname(overstay.getSurname());
			    applica.setVisa_expiry(overstay.getVisaExpiry());
			    applica.setApprover(overstay.getApprover());
			    applica.setLoggedUser(overstay.getUsername());
			    applica.setPageNumber(visaCheckoverstayDTO.getPageNumber());
			    applica.setPageSize(visaCheckoverstayDTO.getPageSize());
			    list.add(applica);
			 
		}

		return  new PageImpl<>(list, sortedByidDesc, totalRecords);
	}

	private Pageable getPageableForVisaCheckOverstay(VisaCheckOverstayDTO visaCheckoverstayDTO,
			TypedQuery<ApplicationOverStayDetails> q, Sort by) {
		
		 log.info("VisaCheckOverstayCustomsRepoImpl:getPageableForVisaCheckOverstay");
	        if (visaCheckoverstayDTO.getPageNumber() > 0 && visaCheckoverstayDTO.getPageSize() > 0) {
	            q.setFirstResult((visaCheckoverstayDTO.getPageNumber() - 1) * visaCheckoverstayDTO.getPageSize());
	            q.setMaxResults(visaCheckoverstayDTO.getPageSize());
	        }
	        /*
	         * return VisaExtensionCheckCustomsRepoImpl:: getPageableForVisaExtensionCheck
	         */
	        return PageRequest.of(visaCheckoverstayDTO.getPageNumber() - 1, visaCheckoverstayDTO.getPageSize(), by.ascending());
}

	private int getVisaCheckOverstayResultSize(List<ApplicationOverStayDetails> visaCheckOverstayResultList) {
		log.info("VisaCheckOverstayCustomsRepoImpl:getVisaCheckoverstayResultSize");

		return visaCheckOverstayResultList.size();
	}

	private List<ApplicationOverStayDetails> getVisaCheckOverstayResultList(TypedQuery<ApplicationOverStayDetails> q) {
		  log.info("VisaCheckOverstayCustomsRepoImpl:getVisaCheckoverstayResultList");
	    
             return q.getResultList();
	    }

	private TypedQuery<ApplicationOverStayDetails> createPredicatesForVisaCheckOverstay(List<Predicate> predicates,
			VisaCheckOverstayDTO visaCheckoverstayDTO, Root<ApplicationOverStayDetails> root, CriteriaBuilder cb,
			CriteriaQuery<ApplicationOverStayDetails> cq) {
	
		if("Secretaire".equals(visaCheckoverstayDTO.getLoggedUser())) {
			

		}
		else {
            predicates.add(cb.equal(cb.lower(root.get("username")), visaCheckoverstayDTO.getLoggedUser().toLowerCase()));
			
			cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));
		}
	
		  return entityManager.createQuery(cq);
	}

	private Root<ApplicationOverStayDetails> getCreateQueryForVisaCheckOverstay(CriteriaQuery<ApplicationOverStayDetails> cq) {
		
	       log.info("VisaCheckOverstayCustomsRepoImpl::getCreateQueryForVisaCheckOverstay");
	        /*
	         * return VisaExtensionCheckCustomsRepoImpl::getRootForVisaExtensionCheck
	         */
	        return cq.from(ApplicationOverStayDetails.class);
	    }
		
	private CriteriaQuery<ApplicationOverStayDetails> getCreateQueryForVisaCheckOverstay(CriteriaBuilder cb) {
	    log.info("VisaCheckoverstayCustomsRepoImpl::getCreateQueryForVisaCheckOverstay");
        /* VisaExtensionCheckCustomsRepoImpl::getCreateQueryForVisaExtensionCheck */
        return cb.createQuery(ApplicationOverStayDetails.class);
	
	}

}
