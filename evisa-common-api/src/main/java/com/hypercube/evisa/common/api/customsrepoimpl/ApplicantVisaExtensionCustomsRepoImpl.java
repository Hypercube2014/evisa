package com.hypercube.evisa.common.api.customsrepoimpl;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.hypercube.evisa.common.api.customsrepo.ApplicantVisaExtensionCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Setter
@Getter
public class ApplicantVisaExtensionCustomsRepoImpl implements ApplicantVisaExtensionCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     *
     */
    @Override
    public Page<ApplicantVisaExtension> searchApplicantVisaExtension(VisaExtensionSearchDTO visaExtensionSearchDTO) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl-searchApplicantVisaExtension");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicantVisaExtension> cq = getCreateQueryForApplicantVisaExtension(cb);
        Root<ApplicantVisaExtension> root = getRootForApplicantVisaExtension(cq);

        projectionForApplicantVisaExtension(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicantVisaExtension> q = createPredicatesForApplicantVisaExtension(predicates,
                visaExtensionSearchDTO, root, cb, cq);
        int totalRecords = getApplicantVisaExtensionResultSize(getApplicantVisaExtensionResultList(q));

        Pageable sortedByIdDesc = getPageableForApplicantVisaExtension(visaExtensionSearchDTO, q, Sort.by("code"));

        /* return the MasterCodeDetails results DTO */
        return new PageImpl<>(getApplicantVisaExtensionResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param MasterCodeDetailsResultList
     * @return
     */
    private int getApplicantVisaExtensionResultSize(List<ApplicantVisaExtension> applicantionVisaExtensionResultList) {
        log.info("MasterCodeDetailsCustomsRepoImpl::getApplicantVisaExtensionResultSize");
        /*
         * return ApplicantVisaExtensionCustomsRepoImpl::
         * getApplicantVisaExtensionResultSize
         */
        return applicantionVisaExtensionResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<ApplicantVisaExtension> getApplicantVisaExtensionResultList(TypedQuery<ApplicantVisaExtension> q) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl::getApplicantVisaExtensionResultList");
        /*
         * return ApplicantVisaExtensionCustomsRepoImpl::
         * getApplicantVisaExtensionResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForApplicantVisaExtension(VisaExtensionSearchDTO searchDTO,
            TypedQuery<ApplicantVisaExtension> q, Sort sort) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl::getPageableForApplicantVisaExtension");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return ApplicantVisaExtensionCustomsRepoImpl::
         * getPageableForApplicantVisaExtension
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
    private TypedQuery<ApplicantVisaExtension> createPredicatesForApplicantVisaExtension(List<Predicate> predicates,
            VisaExtensionSearchDTO searchDTO, Root<ApplicantVisaExtension> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicantVisaExtension> cq) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl::createPredicatesForApplicantVisaExtension");

        if (checkMandatory(searchDTO.getApplicationNumber())) {
            predicates.add(cb.like(cb.lower(root.get("applicationNumber")),
                    "%" + searchDTO.getApplicationNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getExtensionId())) {
            predicates.add(cb.like(cb.lower(root.get("visaExtensionId")),
                    "%" + searchDTO.getExtensionStatus().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getExtensionStatus())) {
            predicates
                    .add(cb.equal(cb.lower(root.get("extensionStatus")), searchDTO.getExtensionStatus().toLowerCase()));
        }

        if (checkMandatory(searchDTO.getLoggedUser())) {
            if (checkMandatory(searchDTO.getRole()) && "DM".equals(searchDTO.getRole())) {
                predicates.add(cb.equal(cb.lower(root.get("assignedTo")), searchDTO.getLoggedUser().toLowerCase()));
            } else {
                predicates.add(cb.equal(cb.lower(root.get("username")), searchDTO.getLoggedUser().toLowerCase()));
            }
        } else {
            if (checkMandatory(searchDTO.getDocumentStatus())) {
                predicates.add(
                        cb.equal(cb.lower(root.get("documentStatus")), searchDTO.getDocumentStatus().toLowerCase()));
            } else {
                predicates.add(cb.not(root.get("documentStatus").in(Arrays.asList("PP"))));
            }
        }

        if (checkMandatory(searchDTO.getDocumentStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("documentStatus")), searchDTO.getDocumentStatus().toLowerCase()));
        } else {
           predicates.add(cb.not(root.get("documentStatus").in(Arrays.asList("PP"))));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("visaExtensionId")));
        /*
         * return ApplicantVisaExtensionCustomsRepoImpl::
         * createPredicatesForApplicantVisaExtension
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
    private void projectionForApplicantVisaExtension(CriteriaQuery<ApplicantVisaExtension> cq, CriteriaBuilder cb,
            Root<ApplicantVisaExtension> root) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl::projectionForApplicantVisaExtension");
        cq.select(cb.construct(ApplicantVisaExtension.class, root.get("visaExtensionId"), root.get("username"),
                root.get("applicationNumber"), root.get("daysOfExtension"), root.get("totalAmount"),
                root.get("extensionStatus"), root.get("documentStatus")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<ApplicantVisaExtension> getRootForApplicantVisaExtension(CriteriaQuery<ApplicantVisaExtension> cq) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl::getRootForApplicantVisaExtension");
        /*
         * return
         * ApplicantVisaExtensionCustomsRepoImpl::getRootForApplicantVisaExtension
         */
        return cq.from(ApplicantVisaExtension.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ApplicantVisaExtension> getCreateQueryForApplicantVisaExtension(CriteriaBuilder cb) {
        log.info("ApplicantVisaExtensionCustomsRepoImpl::getCreateQueryForApplicantVisaExtension");
        /*
         * ApplicantVisaExtensionCustomsRepoImpl::
         * getCreateQueryForApplicantVisaExtension
         */
        return cb.createQuery(ApplicantVisaExtension.class);
    }

}
