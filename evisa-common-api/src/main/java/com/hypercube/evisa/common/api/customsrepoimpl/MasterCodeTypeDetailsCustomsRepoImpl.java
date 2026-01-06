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

import com.hypercube.evisa.common.api.customsrepo.MasterCodeTypeDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.MasterCodeTypeDetails;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class MasterCodeTypeDetailsCustomsRepoImpl implements MasterCodeTypeDetailsCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 
     */
    @Override
    public Page<MasterCodeTypeDetails> searchMasterCodeTypes(MasterCodeSearchDTO masterCodeSearchDTO) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl-searchMasterCodes");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MasterCodeTypeDetails> cq = getCreateQueryForSystem(cb);
        Root<MasterCodeTypeDetails> root = getRootForMasterCodeTypeDetails(cq);

        projectionForMasterCodeTypeDetails(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<MasterCodeTypeDetails> q = createPredicatesForMasterCodeTypeDetails(predicates, masterCodeSearchDTO,
                root, cb, cq);
        int totalRecords = getMasterCodeTypeDetailsResultSize(getMasterCodeTypeDetailsResultList(q));

        Pageable sortedByIdDesc = getPageableForMasterCodeTypeDetails(masterCodeSearchDTO, q, Sort.by("id"));

        /* return the MasterCodeDetails results DTO */
        return new PageImpl<>(getMasterCodeTypeDetailsResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param MasterCodeTypeDetailsResultList
     * @return
     */
    private int getMasterCodeTypeDetailsResultSize(List<MasterCodeTypeDetails> masterCodeTypeDetailsResultList) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::getMasterCodeTypeDetailsResultSize");
        /*
         * return MasterCodeTypeDetailsCustomsRepoImpl::
         * getMasterCodeTypeDetailsResultSize
         */
        return masterCodeTypeDetailsResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<MasterCodeTypeDetails> getMasterCodeTypeDetailsResultList(TypedQuery<MasterCodeTypeDetails> q) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::getMasterCodeTypeDetailsResultList");
        /*
         * return MasterCodeTypeDetailsCustomsRepoImpl::
         * getMasterCodeTypeDetailsResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForMasterCodeTypeDetails(MasterCodeSearchDTO searchDTO,
            TypedQuery<MasterCodeTypeDetails> q, Sort sort) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::getPageableForMasterCodeTypeDetails");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return MasterCodeTypeDetailsCustomsRepoImpl::
         * getPageableForMasterCodeTypeDetails
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
    private TypedQuery<MasterCodeTypeDetails> createPredicatesForMasterCodeTypeDetails(List<Predicate> predicates,
            MasterCodeSearchDTO searchDTO, Root<MasterCodeTypeDetails> root, CriteriaBuilder cb,
            CriteriaQuery<MasterCodeTypeDetails> cq) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::createPredicatesForMasterCodeTypeDetails");

        if (checkMandatory(searchDTO.getCode())) {
            predicates.add(cb.equal(cb.lower(root.get("codeType")), searchDTO.getCode().toLowerCase()));
        }

        if (checkMandatory(searchDTO.getDesc())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("description")), "%" + searchDTO.getDesc().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("descriptionOther")), "%" + searchDTO.getDesc().toLowerCase() + "%")));
        }

        if (checkMandatory(searchDTO.getActive())) {
            predicates.add(cb.equal(cb.lower(root.get("active")), searchDTO.getActive().toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("id")));
        /*
         * return MasterCodeTypeDetailsCustomsRepoImpl::
         * createPredicatesForMasterCodeTypeDetails
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
    private void projectionForMasterCodeTypeDetails(CriteriaQuery<MasterCodeTypeDetails> cq, CriteriaBuilder cb,
            Root<MasterCodeTypeDetails> root) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::projectionForMasterCodeTypeDetails");
        cq.select(cb.construct(MasterCodeTypeDetails.class, root.get("id"), root.get("codeType"),
                root.get("description"), root.get("descriptionOther"), root.get("active")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<MasterCodeTypeDetails> getRootForMasterCodeTypeDetails(CriteriaQuery<MasterCodeTypeDetails> cq) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::getRootForMasterCodeTypeDetails");
        /*
         * return
         * MasterCodeTypeDetailsCustomsRepoImpl::getRootForMasterCodeTypeDetails
         */
        return cq.from(MasterCodeTypeDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<MasterCodeTypeDetails> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("MasterCodeTypeDetailsCustomsRepoImpl::getCreateQueryForSystem");
        /* MasterCodeTypeDetailsCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(MasterCodeTypeDetails.class);
    }

}
