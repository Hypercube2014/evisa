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

import com.hypercube.evisa.common.api.customsrepo.ApplicantTravelHistoryCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantTravelHistory;
import com.hypercube.evisa.common.api.model.ApplicantTravelHistorySearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */

@Data
@Slf4j
public class ApplicantTravelHistoryCustomsRepoImpl implements ApplicantTravelHistoryCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     *
     */
    @Override
    public Page<ApplicantTravelHistory> searchApplicantTravelHistory(
            ApplicantTravelHistorySearchDTO applTravelHisSearchDTO) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl-searchApplicantTravelHistory");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ApplicantTravelHistory> cq = getCreateQueryForApplicantTravelHistory(cb);
        Root<ApplicantTravelHistory> root = getRootForApplicantTravelHistory(cq);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicantTravelHistory> q = createPredicatesForApplicantTravelHistory(predicates,
                applTravelHisSearchDTO, cb, cq, root);
        int totalRecords = getApplicantTravelHistoryResultSize(getApplicantTravelHistoryResults(q));
        Pageable sortedByIdDesc = getPageableForApplicantTravelHistory(applTravelHisSearchDTO, q,
                Sort.by("applicationNumber"));

        /* return the applicantTravelHistory search resultsdto */
        return new PageImpl<ApplicantTravelHistory>(getApplicantTravelHistoryResults(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param roleResults
     * @return
     */
    private int getApplicantTravelHistoryResultSize(List<ApplicantTravelHistory> applicantTravelHistoryResults) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl::getApplicantTravelHistoryResultSize");
        /* return the list size */
        return applicantTravelHistoryResults.size();
    }

    /**
     * @param results
     * @return
     */
    private List<ApplicantTravelHistory> getApplicantTravelHistoryResults(TypedQuery<ApplicantTravelHistory> results) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl::getApplicantTravelHistoryResults");
        /* return the result list */
        return results.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForApplicantTravelHistory(ApplicantTravelHistorySearchDTO searchDTO,
            TypedQuery<ApplicantTravelHistory> q, Sort sort) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl::getPageableForApplicantTravelHistory");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /* return the page request of applicantTravelHistory search dto */
        return PageRequest.of(searchDTO.getPageNumber() - 1, searchDTO.getPageSize(), sort.descending());
    }

    /**
     * @param predicates
     * @param moduleDetailsSearchDTO
     * @param cb
     * @param cq
     * @param rootModule
     * @return
     */
    private TypedQuery<ApplicantTravelHistory> createPredicatesForApplicantTravelHistory(List<Predicate> predicates,
            ApplicantTravelHistorySearchDTO searchDTO, CriteriaBuilder cb, CriteriaQuery<ApplicantTravelHistory> cq,
            Root<ApplicantTravelHistory> root) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl::createPredicatesForModule");

        predicates.add(cb.equal(cb.lower(root.get("surname")), searchDTO.getSurname().toLowerCase()));
        predicates.add(cb.equal(cb.lower(root.get("givenName")), searchDTO.getGivenName().toLowerCase()));
        predicates.add(cb.equal(root.get("gender"), searchDTO.getGender()));
        predicates.add(cb.equal(root.get("dob"), searchDTO.getDob()));
        predicates.add(cb.lessThan(root.get("submittedDate"), searchDTO.getSubmittedDate()));
        predicates.add(cb.equal(root.get("birthCountry"), searchDTO.getBirthCountry()));
        predicates.add(cb.not(root.get("applicationNumber").in(searchDTO.getApplicationNumber())));

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));

        return entityManager.createQuery(cq);
    }

    /**
     * @param cq
     * @return
     */
    private Root<ApplicantTravelHistory> getRootForApplicantTravelHistory(CriteriaQuery<ApplicantTravelHistory> cq) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl::getRootForApplicantTravelHistory");
        return cq.from(ApplicantTravelHistory.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ApplicantTravelHistory> getCreateQueryForApplicantTravelHistory(CriteriaBuilder cb) {
        log.info("ApplicantTravelHistoryCustomsRepoImpl::getCreateQueryForApplicantTravelHistory");
        return cb.createQuery(ApplicantTravelHistory.class);
    }

}
