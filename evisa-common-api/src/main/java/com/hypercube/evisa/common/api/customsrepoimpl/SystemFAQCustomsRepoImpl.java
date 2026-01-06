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

import com.hypercube.evisa.common.api.customsrepo.SystemFAQCustomsRepo;
import com.hypercube.evisa.common.api.domain.SystemFAQ;
import com.hypercube.evisa.common.api.model.SystemFAQSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class SystemFAQCustomsRepoImpl implements SystemFAQCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 
     */
    @Override
    public Page<SystemFAQ> searchSystemFaq(String locale, SystemFAQSearchDTO systemFAQSearchDTO) {
        log.info("SystemFAQCustomsRepoImpl-searchSystemFaq");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SystemFAQ> cq = getCreateQueryForSystem(cb);
        Root<SystemFAQ> root = getRootForSystemFAQ(cq);

        projectionForSystemFAQ(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<SystemFAQ> q = createPredicatesForSystemFAQ(predicates, systemFAQSearchDTO, root, cb, cq);
        int totalRecords = getSystemFAQResultSize(getSystemFAQResultList(q));

        Pageable sortedByIdDesc = getPageableForSystemFAQ(systemFAQSearchDTO, q, Sort.by("faqId"));

        /* return the SystemFAQ results DTO */
        return new PageImpl<>(getSystemFAQResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param SystemFAQResultList
     * @return
     */
    private int getSystemFAQResultSize(List<SystemFAQ> systemFAQResultList) {
        log.info("SystemFAQCustomsRepoImpl::getSystemFAQResultSize");
        /*
         * return SystemFAQCustomsRepoImpl:: getSystemFAQResultSize
         */
        return systemFAQResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<SystemFAQ> getSystemFAQResultList(TypedQuery<SystemFAQ> q) {
        log.info("SystemFAQCustomsRepoImpl::getSystemFAQResultList");
        /*
         * return SystemFAQCustomsRepoImpl:: getSystemFAQResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForSystemFAQ(SystemFAQSearchDTO searchDTO, TypedQuery<SystemFAQ> q, Sort sort) {
        log.info("SystemFAQCustomsRepoImpl::getPageableForSystemFAQ");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return SystemFAQCustomsRepoImpl:: getPageableForSystemFAQ
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
    private TypedQuery<SystemFAQ> createPredicatesForSystemFAQ(List<Predicate> predicates, SystemFAQSearchDTO searchDTO,
            Root<SystemFAQ> root, CriteriaBuilder cb, CriteriaQuery<SystemFAQ> cq) {
        log.info("SystemFAQCustomsRepoImpl::createPredicatesForSystemFAQ");

        if (checkMandatory(searchDTO.getQuestion())) {
            predicates.add(
                    cb.or(cb.like(cb.lower(root.get("faqQuestion")), "%" + searchDTO.getQuestion().toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("faqQuestionOther")),
                                    "%" + searchDTO.getQuestion().toLowerCase() + "%")));
        }

        if (checkMandatory(searchDTO.getAnswer())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("faqAnswer")), "%" + searchDTO.getAnswer().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("faqAnswerOther")), "%" + searchDTO.getAnswer().toLowerCase() + "%")));
        }

        if (checkMandatory(searchDTO.getStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("status")), searchDTO.getStatus().toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("faqId")));
        /*
         * return SystemFAQCustomsRepoImpl:: createPredicatesForSystemFAQ
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
    private void projectionForSystemFAQ(CriteriaQuery<SystemFAQ> cq, CriteriaBuilder cb, Root<SystemFAQ> root) {
        log.info("SystemFAQCustomsRepoImpl::projectionForSystemFAQ");
        cq.select(cb.construct(SystemFAQ.class, root.get("faqId"), root.get("faqType"), root.get("faqQuestion"),
                root.get("faqQuestionOther"), root.get("faqAnswer"), root.get("faqAnswerOther"), root.get("status")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<SystemFAQ> getRootForSystemFAQ(CriteriaQuery<SystemFAQ> cq) {
        log.info("SystemFAQCustomsRepoImpl::getRootForSystemFAQ");
        /*
         * return SystemFAQCustomsRepoImpl::getRootForSystemFAQ
         */
        return cq.from(SystemFAQ.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<SystemFAQ> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("SystemFAQCustomsRepoImpl::getCreateQueryForSystem");
        /* SystemFAQCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(SystemFAQ.class);
    }

}
