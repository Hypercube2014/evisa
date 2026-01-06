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

import com.hypercube.evisa.common.api.customsrepo.ApplicantPaymentCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;
import com.hypercube.evisa.common.api.model.PaymentSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class ApplicantPaymentCustomsRepoImpl implements ApplicantPaymentCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     *
     */
    @Override
    public Page<ApplicantPaymentDetails> searchApplicantPayments(PaymentSearchDTO paymentSearchDTO) {
        log.info("ApplicantPaymentCustomsRepoImpl::searchApplicantPayments");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ApplicantPaymentDetails> cq = getCreateQueryForApplicantPayment(cb);
        Root<ApplicantPaymentDetails> root = getRootForApplicantPayment(cq);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicantPaymentDetails> q = createPredicatesForApplicantPayment(predicates, paymentSearchDTO, cb,
                cq, root);
        int totalRecords = getApplicantPaymentResultSize(getApplicantPaymentResults(q));
        Pageable sortedByIdDesc = getPageableForApplicantPayment(paymentSearchDTO, q, Sort.by("createdDate"));

        /* return the routing search resultsdto */
        return new PageImpl<ApplicantPaymentDetails>(getApplicantPaymentResults(q), sortedByIdDesc, totalRecords);
        
        }

    /**
     * @param applicantPaymentResults
     * @return
     */
    private int getApplicantPaymentResultSize(List<ApplicantPaymentDetails> applicantPaymentResults) {
        log.info("ApplicantPaymentCustomsRepoImpl::getApplicantPaymentResultSize");
        /* return the list size */
        return applicantPaymentResults.size();
    }

    /**
     * @param results
     * @return
     */
    private List<ApplicantPaymentDetails> getApplicantPaymentResults(TypedQuery<ApplicantPaymentDetails> results) {
        log.info("ApplicantPaymentCustomsRepoImpl::getApplicantPaymentResults");
        /* return the result list */
        return results.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForApplicantPayment(PaymentSearchDTO searchDTO, TypedQuery<ApplicantPaymentDetails> q,
            Sort sort) {
        log.info("ApplicantPaymentCustomsRepoImpl::getPageableForApplicantPayment");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /* return the page request of payment search dto */
        return PageRequest.of(searchDTO.getPageNumber() - 1, searchDTO.getPageSize(), sort.descending());
    }

    /**
     * @param predicates
     * @param searchDTO
     * @param cb
     * @param cq
     * @param root
     * @return
     */
    private TypedQuery<ApplicantPaymentDetails> createPredicatesForApplicantPayment(List<Predicate> predicates,
            PaymentSearchDTO searchDTO, CriteriaBuilder cb, CriteriaQuery<ApplicantPaymentDetails> cq,
            Root<ApplicantPaymentDetails> root) {
        log.info("ApplicantPaymentCustomsRepoImpl::createPredicatesForApplicantPayment");

        if (checkMandatory(searchDTO.getFileNumber())) {
            predicates.add(
                    cb.like(cb.lower(root.get("fileNumber")), "%" + searchDTO.getFileNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getInstrType())) {
            predicates.add(cb.equal(root.get("instrType"), searchDTO.getInstrType()));
        }

        // if (checkMandatory(searchDTO.getLoggedUser())) {
        predicates.add(cb.equal(root.get("username"), searchDTO.getLoggedUser()));
        // }

        if (checkMandatory(searchDTO.getStatus())) {
        predicates.add(cb.equal(root.get("status"), searchDTO.getStatus()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("createdDate")));

        return entityManager.createQuery(cq);
    }

    /**
     * @param value
     * @return
     */
    private boolean checkMandatory(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * @param cq
     * @return
     */
    private Root<ApplicantPaymentDetails> getRootForApplicantPayment(CriteriaQuery<ApplicantPaymentDetails> cq) {
        log.info("ApplicantPaymentCustomsRepoImpl::getRootForApplicantPayment");
        return cq.from(ApplicantPaymentDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ApplicantPaymentDetails> getCreateQueryForApplicantPayment(CriteriaBuilder cb) {
        log.info("ApplicantPaymentCustomsRepoImpl::getCreateQueryForApplicantPayment");
        return cb.createQuery(ApplicantPaymentDetails.class);
    }

}
