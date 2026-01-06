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

import com.hypercube.evisa.common.api.customsrepo.RoleManagementCustomsRepo;
import com.hypercube.evisa.common.api.domain.RoleDetails;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.RoleDetailsSearchResultsDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class RoleManagementCustomsRepoImpl implements RoleManagementCustomsRepo {

    /**
     * static declarations for the role id
     */
    private static final String ROLEID = "roleId";

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Implementations of the search role details
     */
    @Override
    public Page<RoleDetailsSearchResultsDTO> searchRoleDetails(RoleDetailsSearchDTO roleDetailsSearchDTO) {
        log.info("::RoleDetailsSearchResultsDTO::searchRoleDetails");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<RoleDetailsSearchResultsDTO> cq = getCreateQueryForRole(cb);
        Root<RoleDetails> root = getRootForRole(cq);

        projectionForRole(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<RoleDetailsSearchResultsDTO> q = createPredicatesForRole(predicates, roleDetailsSearchDTO, cb, cq,
                root);
        int count = getRoleResultSize(getRoleResults(q));
        Pageable sortedByIdDesc = getPageableForRole(roleDetailsSearchDTO, q, Sort.by(ROLEID));

        /* return the routing search resultsdto */
        return new PageImpl<RoleDetailsSearchResultsDTO>(getRoleResults(q), sortedByIdDesc, count);
    }

    /**
     * @param roleResults
     * @return
     */
    private int getRoleResultSize(List<RoleDetailsSearchResultsDTO> roleResults) {
        log.info("RoleDetailsSearchResultsDTO::roleResults");
        /* return the list size */
        return roleResults.size();
    }

    /**
     * @param results
     * @return
     */
    private List<RoleDetailsSearchResultsDTO> getRoleResults(TypedQuery<RoleDetailsSearchResultsDTO> results) {
        log.info("RoleDetailsSearchResultsDTO::getRoleResults");
        /* return the result list */
        return results.getResultList();
    }

    /**
     * @param roleDetailsSearchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForRole(RoleDetailsSearchDTO roleDetailsSearchDTO,
            TypedQuery<RoleDetailsSearchResultsDTO> q, Sort sort) {
        log.info("RoleDetailsSearchResultsDTO::getPageableForRole");
        if (roleDetailsSearchDTO.getPageNumber() > 0 && roleDetailsSearchDTO.getPageSize() > 0) {
            q.setFirstResult((roleDetailsSearchDTO.getPageNumber() - 1) * roleDetailsSearchDTO.getPageSize());
            q.setMaxResults(roleDetailsSearchDTO.getPageSize());
        }
        /* return the page request of routing search dto */
        return PageRequest.of(roleDetailsSearchDTO.getPageNumber() - 1, roleDetailsSearchDTO.getPageSize(),
                sort.descending());
    }

    /**
     * @param predicates
     * @param roleDetailsSearchDTO
     * @param cb
     * @param cq
     * @param rootRole
     * @return
     */
    private TypedQuery<RoleDetailsSearchResultsDTO> createPredicatesForRole(List<Predicate> predicates,
            RoleDetailsSearchDTO roleDetailsSearchDTO, CriteriaBuilder cb,
            CriteriaQuery<RoleDetailsSearchResultsDTO> cq, Root<RoleDetails> rootRole) {
        log.info("RoleDetailsSearchResultsDTO::createPredicatesForRole");

        if (checkMandatory(roleDetailsSearchDTO.getRoleCode())) {
            predicates.add(cb.like(cb.lower(rootRole.get("roleCode")),
                    "%" + roleDetailsSearchDTO.getRoleCode().toLowerCase() + "%"));
        }

        if (checkMandatory(roleDetailsSearchDTO.getRoleDesc())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(rootRole.get("roleDesc")),
                            "%" + roleDetailsSearchDTO.getRoleDesc().toLowerCase() + "%"),
                    cb.like(cb.lower(rootRole.get("roleDescOther")),
                            "%" + roleDetailsSearchDTO.getRoleDesc().toLowerCase() + "%")));
        }

        if (checkMandatory(roleDetailsSearchDTO.getStatus())) {
            predicates.add(cb.equal(rootRole.get("status"), roleDetailsSearchDTO.getStatus()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(rootRole.get(ROLEID)));

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
    private void projectionForRole(CriteriaQuery<RoleDetailsSearchResultsDTO> cq, CriteriaBuilder cb,
            Root<RoleDetails> root) {
        log.info("RoleDetailsSearchResultsDTO::projectionForRole");
        cq.select(cb.construct(RoleDetailsSearchResultsDTO.class, root.get(ROLEID), root.get("roleCode"),
                root.get("roleDesc"), root.get("roleDescOther"), root.get("status")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<RoleDetails> getRootForRole(CriteriaQuery<RoleDetailsSearchResultsDTO> cq) {
        log.info("RoleDetailsSearchResultsDTO::getRootForRole");
        return cq.from(RoleDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<RoleDetailsSearchResultsDTO> getCreateQueryForRole(CriteriaBuilder cb) {
        log.info("RoleDetailsSearchResultsDTO::getCreateQueryForRole");
        return cb.createQuery(RoleDetailsSearchResultsDTO.class);
    }

}
