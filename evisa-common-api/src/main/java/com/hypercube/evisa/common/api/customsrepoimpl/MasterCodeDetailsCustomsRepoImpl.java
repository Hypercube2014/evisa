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

import com.hypercube.evisa.common.api.customsrepo.MasterCodeDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.MasterCodeDetails;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class MasterCodeDetailsCustomsRepoImpl implements MasterCodeDetailsCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 
     */
    @Override
    public Page<MasterCodeDetails> searchMasterCodes(String locale, MasterCodeSearchDTO masterCodeSearchDTO) {
        log.info("MasterCodeDetailsCustomsRepoImpl-searchMasterCodes");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MasterCodeDetails> cq = getCreateQueryForSystem(cb);
        Root<MasterCodeDetails> root = getRootForMasterCodeDetails(cq);

        projectionForMasterCodeDetails(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<MasterCodeDetails> q = createPredicatesForMasterCodeDetails(predicates, masterCodeSearchDTO, root,
                cb, cq);
        int totalRecords = getMasterCodeDetailsResultSize(getMasterCodeDetailsResultList(q));

        Pageable sortedByIdDesc = getPageableForMasterCodeDetails(masterCodeSearchDTO, q, Sort.by("code"));

        /* return the MasterCodeDetails results DTO */
        return new PageImpl<>(getMasterCodeDetailsResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param MasterCodeDetailsResultList
     * @return
     */
    private int getMasterCodeDetailsResultSize(List<MasterCodeDetails> masterCodeDetailsResultList) {
        log.info("MasterCodeDetailsCustomsRepoImpl::getMasterCodeDetailsResultSize");
        /*
         * return masterCodeDetailsCustomsRepoImpl::
         * getMasterCodeDetailsResultSize
         */
        return masterCodeDetailsResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<MasterCodeDetails> getMasterCodeDetailsResultList(TypedQuery<MasterCodeDetails> q) {
        log.info("MasterCodeDetailsCustomsRepoImpl::getMasterCodeDetailsResultList");
        /*
         * return MasterCodeDetailsCustomsRepoImpl::
         * getMasterCodeDetailsResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForMasterCodeDetails(MasterCodeSearchDTO searchDTO, TypedQuery<MasterCodeDetails> q,
            Sort sort) {
        log.info("MasterCodeDetailsCustomsRepoImpl::getPageableForMasterCodeDetails");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return MasterCodeDetailsCustomsRepoImpl::
         * getPageableForMasterCodeDetails
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
    private TypedQuery<MasterCodeDetails> createPredicatesForMasterCodeDetails(List<Predicate> predicates,
            MasterCodeSearchDTO searchDTO, Root<MasterCodeDetails> root, CriteriaBuilder cb,
            CriteriaQuery<MasterCodeDetails> cq) {
        log.info("MasterCodeDetailsCustomsRepoImpl::createPredicatesForMasterCodeDetails");

        if (checkMandatory(searchDTO.getCode())) {
            predicates.add(cb.equal(cb.lower(root.get("code")), searchDTO.getCode().toLowerCase()));
        }

        if (checkMandatory(searchDTO.getDesc())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("description")), "%" + searchDTO.getDesc().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("descriptionOther")), "%" + searchDTO.getDesc().toLowerCase() + "%")));
        }

        if (checkMandatory(searchDTO.getActive())) {
            predicates.add(cb.equal(cb.lower(root.get("active")), searchDTO.getActive().toLowerCase()));
        }

        predicates.add(cb.equal(cb.lower(root.get("codeType")), searchDTO.getCodeType().toLowerCase()));

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("id")));
        /*
         * return MasterCodeDetailsCustomsRepoImpl::
         * createPredicatesForMasterCodeDetails
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
    private void projectionForMasterCodeDetails(CriteriaQuery<MasterCodeDetails> cq, CriteriaBuilder cb,
            Root<MasterCodeDetails> root) {
        log.info("MasterCodeDetailsCustomsRepoImpl::projectionForMasterCodeDetails");
        cq.select(cb.construct(MasterCodeDetails.class, root.get("id"), root.get("code"), root.get("description"),
                root.get("descriptionOther"), root.get("active")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<MasterCodeDetails> getRootForMasterCodeDetails(CriteriaQuery<MasterCodeDetails> cq) {
        log.info("MasterCodeDetailsCustomsRepoImpl::getRootForMasterCodeDetails");
        /*
         * return MasterCodeDetailsCustomsRepoImpl::getRootForMasterCodeDetails
         */
        return cq.from(MasterCodeDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<MasterCodeDetails> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("MasterCodeDetailsCustomsRepoImpl::getCreateQueryForSystem");
        /* MasterCodeDetailsCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(MasterCodeDetails.class);
    }

}
