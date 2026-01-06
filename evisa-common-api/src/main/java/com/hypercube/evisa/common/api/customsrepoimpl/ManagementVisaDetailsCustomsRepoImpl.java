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

import com.hypercube.evisa.common.api.customsrepo.ManagementVisaDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.VisaDetailsSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class ManagementVisaDetailsCustomsRepoImpl implements ManagementVisaDetailsCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 
     */
    @Override
    public Page<ManagementVisaDetails> searchVisaDetails(VisaDetailsSearchDTO visaDetailsSearchDTO) {
        log.info("ManagementVisaDetailsCustomsRepoImpl-searchVisaDetails");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ManagementVisaDetails> cq = getCreateQueryForSystem(cb);
        Root<ManagementVisaDetails> root = getRootForManagementVisaDetails(cq);

        projectionForManagementVisaDetails(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ManagementVisaDetails> q = createPredicatesForManagementVisaDetails(predicates, visaDetailsSearchDTO,
                root, cb, cq);

        int totalRecords = getManagementVisaDetailsResultSize(getManagementVisaDetailsResultList(q));

        Pageable sortedByIdDesc = getPageableForManagementVisaDetails(visaDetailsSearchDTO, q, Sort.by("visaId"));

        /* return the ManagementVisaDetails results DTO */
        return new PageImpl<>(getManagementVisaDetailsResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param ManagementVisaDetailsResultList
     * @return
     */
    private int getManagementVisaDetailsResultSize(List<ManagementVisaDetails> managementVisaDetailsResultList) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::getManagementVisaDetailsResultSize");
        /*
         * return ManagementVisaDetailsCustomsRepoImpl::
         * getManagementVisaDetailsResultSize
         */
        return managementVisaDetailsResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<ManagementVisaDetails> getManagementVisaDetailsResultList(TypedQuery<ManagementVisaDetails> q) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::getManagementVisaDetailsResultList");
        /*
         * return ManagementVisaDetailsCustomsRepoImpl::
         * getManagementVisaDetailsResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForManagementVisaDetails(VisaDetailsSearchDTO searchDTO,
            TypedQuery<ManagementVisaDetails> q, Sort sort) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::getPageableForManagementVisaDetails");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return ManagementVisaDetailsCustomsRepoImpl::
         * getPageableForManagementVisaDetails
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
    private TypedQuery<ManagementVisaDetails> createPredicatesForManagementVisaDetails(List<Predicate> predicates,
            VisaDetailsSearchDTO searchDTO, Root<ManagementVisaDetails> root, CriteriaBuilder cb,
            CriteriaQuery<ManagementVisaDetails> cq) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::createPredicatesForManagementVisaDetails");

        if (checkMandatory(searchDTO.getVisaType())) {
            predicates.add(cb.equal(cb.lower(root.get("visaType")), searchDTO.getVisaType().toLowerCase()));
        }

        if (checkMandatory(searchDTO.getVisaDescription())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("visaDescription")),
                            "%" + searchDTO.getVisaDescription().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("visaDescriptionOther")),
                            "%" + searchDTO.getVisaDescription().toLowerCase() + "%")));
        }

        if (checkMandatory(searchDTO.getIsExpressVisaAllowed())) {
            predicates.add(
                    cb.equal(root.get("isExpressVisaAllowed"), Boolean.valueOf(searchDTO.getIsExpressVisaAllowed())));
        }

        if (checkMandatory(searchDTO.getStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("status")), searchDTO.getStatus().toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("visaId")));
        /*
         * return ManagementVisaDetailsCustomsRepoImpl::
         * createPredicatesForManagementVisaDetails
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
    private void projectionForManagementVisaDetails(CriteriaQuery<ManagementVisaDetails> cq, CriteriaBuilder cb,
            Root<ManagementVisaDetails> root) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::projectionForManagementVisaDetails");
        cq.select(cb.construct(ManagementVisaDetails.class, root.get("visaId"), root.get("visaType"),
                root.get("visaDescription"), root.get("visaDescriptionOther"), root.get("visaFee"),
                root.get("isExpressVisaAllowed"), root.get("status")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<ManagementVisaDetails> getRootForManagementVisaDetails(CriteriaQuery<ManagementVisaDetails> cq) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::getRootForManagementVisaDetails");
        /*
         * return
         * ManagementVisaDetailsCustomsRepoImpl::getRootForManagementVisaDetails
         */
        return cq.from(ManagementVisaDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ManagementVisaDetails> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("ManagementVisaDetailsCustomsRepoImpl::getCreateQueryForSystem");
        /* ManagementVisaDetailsCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(ManagementVisaDetails.class);
    }

}
