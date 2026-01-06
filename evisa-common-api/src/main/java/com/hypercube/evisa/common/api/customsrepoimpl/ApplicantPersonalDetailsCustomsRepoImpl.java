/**
 * 
 */
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

import com.hypercube.evisa.common.api.customsrepo.ApplicantPersonalDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;
import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class ApplicantPersonalDetailsCustomsRepoImpl implements ApplicantPersonalDetailsCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<ApplicantPersonalDetailsSearchDTO> searchApplications(
            ApplicantPersonalDetailsSearchDTO applicantPersonalDetailsSearchDTO) {
        log.info("ApplicantPersonalDetailsCustomsRepoImpl-searchApplications");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ApplicantPersonalDetailsSearchDTO> cq = getCreateQueryForSystem(cb);
        Root<ApplicantPersonalDetails> root = getRootForApplicationPersonalDetails(cq);

        projectionForApplicationPersonalDetails(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicantPersonalDetailsSearchDTO> q = createPredicatesForApplicationPersonalDetails(predicates,
                applicantPersonalDetailsSearchDTO, root, cb, cq);
        int totalRecords = getApplicationPersonalDetailsResultSize(getApplicationPersonalDetailsResultList(q));
        Pageable sortedByIdDesc = getPageableForApplicationPersonalDetails(applicantPersonalDetailsSearchDTO, q,
                Sort.by("applicationNumber"));

        /* return the Application PersonalDetails results DTO */
        return new PageImpl<>(getApplicationPersonalDetailsResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param ApplicationFileResultList
     * @return
     */
    private int getApplicationPersonalDetailsResultSize(
            List<ApplicantPersonalDetailsSearchDTO> ApplicationPersonalDetailsResultList) {
        log.info("ApplicationFileCustomsRepoImpl::getApplicationPersonalDetailsResultSize");
        /*
         * return ApplicationFileCustomsRepoImpl::
         * getApplicationPersonalDetailsResultSize
         */
        return ApplicationPersonalDetailsResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<ApplicantPersonalDetailsSearchDTO> getApplicationPersonalDetailsResultList(
            TypedQuery<ApplicantPersonalDetailsSearchDTO> q) {
        log.info("ApplicationFileCustomsRepoImpl::getApplicationPersonalDetailsResultList");
        /*
         * return ApplicationFileCustomsRepoImpl::
         * getApplicationPersonalDetailsResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForApplicationPersonalDetails(ApplicantPersonalDetailsSearchDTO searchDTO,
            TypedQuery<ApplicantPersonalDetailsSearchDTO> q, Sort sort) {
        log.info("ApplicationFileCustomsRepoImpl::getPageableForApplicationPersonalDetails");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return ApplicationFileCustomsRepoImpl::
         * getPageableForApplicationPersonalDetails
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
    private TypedQuery<ApplicantPersonalDetailsSearchDTO> createPredicatesForApplicationPersonalDetails(
            List<Predicate> predicates, ApplicantPersonalDetailsSearchDTO searchDTO,
            Root<ApplicantPersonalDetails> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicantPersonalDetailsSearchDTO> cq) {
        log.info("ApplicationFileCustomsRepoImpl::createPredicatesForApplicationPersonalDetails");

        if (checkMandatory(searchDTO.getApplicationNumber())) {
            predicates.add(cb.like(cb.lower(root.get("applicationNumber")),
                    "%" + searchDTO.getApplicationNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getGivenName())) {
            predicates
                    .add(cb.like(cb.lower(root.get("givenName")), "%" + searchDTO.getGivenName().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getEmailAddress())) {
            predicates.add(
                    cb.like(cb.lower(root.get("emailAddress")), "%" + searchDTO.getEmailAddress().toLowerCase() + "%"));
        }

        predicates.add(cb.equal(root.get("fileNumber"), searchDTO.getFileNumber()));

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));
        /*
         * return ApplicationFileCustomsRepoImpl::
         * createPredicatesForApplicationFile
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
    private void projectionForApplicationPersonalDetails(CriteriaQuery<ApplicantPersonalDetailsSearchDTO> cq,
            CriteriaBuilder cb, Root<ApplicantPersonalDetails> root) {
        log.info("ApplicationFileCustomsRepoImpl::projectionForApplicationPersonalDetails");
        cq.select(cb.construct(ApplicantPersonalDetailsSearchDTO.class, root.get("applicationNumber"),
                root.get("fileNumber"), root.get("appliedFor"), root.get("givenName"), root.get("emailAddress")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<ApplicantPersonalDetails> getRootForApplicationPersonalDetails(
            CriteriaQuery<ApplicantPersonalDetailsSearchDTO> cq) {
        log.info("ApplicationFileCustomsRepoImpl::getRootForApplicationPersonalDetails");
        /*
         * return
         * ApplicationFileCustomsRepoImpl::getRootForApplicationPersonalDetails
         */
        return cq.from(ApplicantPersonalDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ApplicantPersonalDetailsSearchDTO> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("ApplicationFileCustomsRepoImpl::getCreateQueryForSystem");
        /* ApplicationFileCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(ApplicantPersonalDetailsSearchDTO.class);
    }

}
