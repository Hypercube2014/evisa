package com.hypercube.evisa.common.api.customsrepoimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hypercube.evisa.common.api.customsrepo.VisaExtensionCheckCustomsRepo;
import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class VisaExtensionCheckCustomsRepoImpl implements VisaExtensionCheckCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;
    
   

    /**
     *
     */
    @Override
    public Page<VisaExtensionCheck> searchVisaExtensionCheck(VisaExtensionSearchDTO visaExtensionCheckSearchDTO) {
        log.info("VisaExtensionCheckCustomsRepoImpl-searchVisaExtensionCheck");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<VisaExtensionCheck> cq = getCreateQueryForVisaExtensionCheck(cb);
        Root<VisaExtensionCheck> root = getRootForVisaExtensionCheck(cq);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<VisaExtensionCheck> q = createPredicatesForVisaExtensionCheck(predicates,
                visaExtensionCheckSearchDTO, root, cb, cq);
        int totalRecords = getVisaExtensionCheckResultSize(getVisaExtensionCheckResultList(q));
        
        System.out.println("total records");
         System.out.println(totalRecords);
         
        Pageable sortedByIdDesc = getPageableForVisaExtensionCheck(visaExtensionCheckSearchDTO, q,
                Sort.by("applicationNumber"));

        /* return the VisaExtensionCheck results DTO */
        System.out.println(getVisaExtensionCheckResultList(q).toString());
        return new PageImpl<>(getVisaExtensionCheckResultList(q), sortedByIdDesc, totalRecords);
        
    }

    /**
     * @param resultList
     * @return
     */
    private int getVisaExtensionCheckResultSize(List<VisaExtensionCheck> resultList) {
        log.info("VisaExtensionCheckCustomsRepoImpl::getVisaExtensionCheckResultSize");
        /*
         * return VisaExtensionCheckCustomsRepoImpl:: getVisaExtensionCheckResultSize
         */
        return resultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<VisaExtensionCheck> getVisaExtensionCheckResultList(TypedQuery<VisaExtensionCheck> q) {
        log.info("VisaExtensionCheckCustomsRepoImpl::getVisaExtensionCheckResultList");
        /*
         * return VisaExtensionCheckCustomsRepoImpl:: getVisaExtensionCheckResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForVisaExtensionCheck(VisaExtensionSearchDTO searchDTO,
            TypedQuery<VisaExtensionCheck> q, Sort sort) {
        log.info("VisaExtensionCheckCustomsRepoImpl::getPageableForVisaExtensionCheck");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return VisaExtensionCheckCustomsRepoImpl:: getPageableForVisaExtensionCheck
         */
        return PageRequest.of(searchDTO.getPageNumber() - 1, searchDTO.getPageSize(), sort.ascending());
    }
   

    /**
     * @param predicates
     * @param searchDTO
     * @param root
     * @param cb
     * @param cq
     * @return
     */
    private TypedQuery<VisaExtensionCheck> createPredicatesForVisaExtensionCheck(List<Predicate> predicates,
            VisaExtensionSearchDTO searchDTO, Root<VisaExtensionCheck> root, CriteriaBuilder cb,
            CriteriaQuery<VisaExtensionCheck> cq) {
        log.info("VisaExtensionCheckCustomsRepoImpl::createPredicatesForVisaExtensionCheck");

        /*
        if (checkMandatoryFields(searchDTO.getFileNumber())) {
            predicates.add(
                    cb.like(cb.lower(root.get("fileNumber")), "%"+ searchDTO.getFileNumber().toLowerCase() + "%"));
        }
*/
       /* if (checkMandatoryFields(searchDTO.getApplicationNumber())) { 
            predicates.add(cb.like(cb.lower(root.get("applicationNumber")),searchDTO.getApplicationNumber().toLowerCase()));
        }
       */
      // predicates.add(cb.equal(cb.lower(root.get("username")),searchDTO.getLoggedUser()));

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));
        
        /*
         * return VisaExtensionCheckCustomsRepoImpl::
         * createPredicatesForVisaExtensionCheck
         */
        
        //log.info(predicates.toString());
        
        return entityManager.createQuery(cq);
    }

    /**
     * @param value
     * @return
     */
    private boolean checkMandatoryFields(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * @param cq
     * @return
     */
    private Root<VisaExtensionCheck> getRootForVisaExtensionCheck(CriteriaQuery<VisaExtensionCheck> cq) {
        log.info("VisaExtensionCheckCustomsRepoImpl::getRootForVisaExtensionCheck");
        /*
         * return VisaExtensionCheckCustomsRepoImpl::getRootForVisaExtensionCheck
         */
        return cq.from(VisaExtensionCheck.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<VisaExtensionCheck> getCreateQueryForVisaExtensionCheck(CriteriaBuilder cb) {
        log.info("VisaExtensionCheckCustomsRepoImpl::getCreateQueryForVisaExtensionCheck");
        /* VisaExtensionCheckCustomsRepoImpl::getCreateQueryForVisaExtensionCheck */
        return cb.createQuery(VisaExtensionCheck.class);
    }

}
