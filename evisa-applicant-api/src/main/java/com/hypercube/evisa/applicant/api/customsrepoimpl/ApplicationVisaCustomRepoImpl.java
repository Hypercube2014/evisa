package com.hypercube.evisa.applicant.api.customsrepoimpl;

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

import com.hypercube.evisa.applicant.api.customsrepo.ApplicationVisaCustomRepo;
import com.hypercube.evisa.applicant.api.domain.ApplicationVisaDetails;
import com.hypercube.evisa.applicant.api.model.ApplicationVisaSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class ApplicationVisaCustomRepoImpl implements ApplicationVisaCustomRepo {
    
    private static final String FILE_NUMBER  = "fileNumber";

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Search Application Visa
     */
    @Override
    public Page<ApplicationVisaDetails> searchApplicationVisa(ApplicationVisaSearchDTO applicationVisaSearchDTO) {
        log.info("ApplicationVisaCustomRepoImpl-searchApplicationVisa");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ApplicationVisaDetails> cq = getCreateQueryForSystem(cb);
        Root<ApplicationVisaDetails> root = getRootForApplicationVisa(cq);

        projectionForApplicationVisa(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicationVisaDetails> q = createPredicatesForApplicationVisa(predicates, applicationVisaSearchDTO,
                root, cb, cq);
        int totalRecords = getApplicationVisaResultSize(getApplicationVisaResultList(q));
        Pageable sortedByIdDesc = getPageableForApplicationVisa(applicationVisaSearchDTO, q, Sort.by(FILE_NUMBER));

        /* return the Application Visa results DTO */
        return new PageImpl<>(getApplicationVisaResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param ApplicationVisaResultList
     * @return
     */
    private int getApplicationVisaResultSize(List<ApplicationVisaDetails> ApplicationVisaResultList) {
        log.info("ApplicationVisaCustomsRepoImpl::getApplicationVisaResultSize");
        /*
         * return ApplicationVisaCustomsRepoImpl:: getApplicationVisaResultSize
         */
        return ApplicationVisaResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<ApplicationVisaDetails> getApplicationVisaResultList(TypedQuery<ApplicationVisaDetails> q) {
        log.info("ApplicationVisaCustomsRepoImpl::getApplicationVisaResultList");
        /*
         * return ApplicationVisaCustomsRepoImpl:: getApplicationVisaResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForApplicationVisa(ApplicationVisaSearchDTO searchDTO,
            TypedQuery<ApplicationVisaDetails> q, Sort sort) {
        log.info("ApplicationVisaCustomsRepoImpl::getPageableForApplicationVisa");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return ApplicationVisaCustomsRepoImpl::getPageableForApplicationVisa
         */
        return PageRequest.of(searchDTO.getPageNumber() - 1, searchDTO.getPageSize(), sort.descending());
    }

    /**
     * @param predicates
     * @param searchDTO
     * @param root
     * @param cb
     * @param cq
     * @return
     */
    private TypedQuery<ApplicationVisaDetails> createPredicatesForApplicationVisa(List<Predicate> predicates,
            ApplicationVisaSearchDTO searchDTO, Root<ApplicationVisaDetails> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicationVisaDetails> cq) {
        log.info("ApplicationVisaCustomsRepoImpl::createPredicatesForApplicationVisa");

        if (checkMandatory(searchDTO.getFileNumber())) {
            predicates.add(
                    cb.like(cb.lower(root.get(FILE_NUMBER)), "%" + searchDTO.getFileNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getApplicantType())) {
            predicates.add(cb.equal(root.get("applicantType"), searchDTO.getApplicantType()));
        }

        if (checkMandatory(searchDTO.getIsExpressVisa())) {
            predicates.add(cb.equal(root.get("isExpressVisa"), Boolean.parseBoolean(searchDTO.getIsExpressVisa())));
        }

        if (checkMandatory(searchDTO.getVisaType())) {
            predicates.add(cb.equal(root.get("visaType"), searchDTO.getVisaType()));
        }

        if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            predicates.add(cb.between(root.get("createdDate"), searchDTO.getStartDate(), searchDTO.getEndDate()));
        }

        predicates.add(cb.equal(root.get("username"), searchDTO.getLoggedUser()));

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get(FILE_NUMBER)));
        /*
         * return ApplicationVisaCustomsRepoImpl::
         * createPredicatesForApplicationVisa
         */
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
     * @param cb
     * @param root
     */
    private void projectionForApplicationVisa(CriteriaQuery<ApplicationVisaDetails> cq, CriteriaBuilder cb,
            Root<ApplicationVisaDetails> root) {
        log.info("ApplicationVisaCustomsRepoImpl::projectionForApplicationVisa");
        cq.select(cb.construct(ApplicationVisaDetails.class, root.get(FILE_NUMBER), root.get("isExpressVisa"),
                root.get("applicantType"), root.get("visaType"), root.get("visaDescription"),
                root.get("visaDescriptionOther"), root.get("status")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<ApplicationVisaDetails> getRootForApplicationVisa(CriteriaQuery<ApplicationVisaDetails> cq) {
        log.info("ApplicationVisaCustomsRepoImpl::getRootForApplicationVisa");
        /*
         * return ApplicationVisaCustomsRepoImpl::getRootForApplicationVisa
         */
        return cq.from(ApplicationVisaDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ApplicationVisaDetails> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("ApplicationVisaCustomsRepoImpl::getCreateQueryForSystem");
        /* ApplicationVisaCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(ApplicationVisaDetails.class);
    }

}
