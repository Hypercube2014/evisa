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

import com.hypercube.evisa.common.api.customsrepo.ModuleDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.ModuleDetails;
import com.hypercube.evisa.common.api.model.ModuleDetailSearchResultsDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailsSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class ModuleDetailsCustomsRepoImpl implements ModuleDetailsCustomsRepo {

    /**
     * static declarations for the role id
     */
    private static final String ORDERNO = "orderNo";

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Implementations of the search role details
     */
    @Override
    public Page<ModuleDetailSearchResultsDTO> searchModuleDetails(ModuleDetailsSearchDTO moduleDetailsSearchDTO) {
        log.info("::ModuleDetailSearchResultsDTO::searchModuleDetails");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ModuleDetailSearchResultsDTO> cq = getCreateQueryForModule(cb);
        Root<ModuleDetails> root = getRootForModule(cq);

        projectionForModule(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ModuleDetailSearchResultsDTO> q = createPredicatesForModule(predicates, moduleDetailsSearchDTO, cb,
                cq, root);
        int totalRecords = getRoleResultSize(getModuleResults(q));
        Pageable sortedByIdDesc = getPageableForModule(moduleDetailsSearchDTO, q, Sort.by(ORDERNO));

        /* return the routing search resultsdto */
        return new PageImpl<ModuleDetailSearchResultsDTO>(getModuleResults(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param roleResults
     * @return
     */
    private int getRoleResultSize(List<ModuleDetailSearchResultsDTO> moduleResults) {
        log.info("ModuleDetailSearchResultsDTO::moduleResults");
        /* return the list size */
        return moduleResults.size();
    }

    /**
     * @param results
     * @return
     */
    private List<ModuleDetailSearchResultsDTO> getModuleResults(TypedQuery<ModuleDetailSearchResultsDTO> results) {
        log.info("ModuleDetailSearchResultsDTO::getModuleResults");
        /* return the result list */
        return results.getResultList();
    }

    /**
     * @param moduleDetailsSearchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForModule(ModuleDetailsSearchDTO moduleDetailsSearchDTO,
            TypedQuery<ModuleDetailSearchResultsDTO> q, Sort sort) {
        log.info("ModuleDetailSearchResultsDTO::getPageableForModule");
        if (moduleDetailsSearchDTO.getPageNumber() > 0 && moduleDetailsSearchDTO.getPageSize() > 0) {
            q.setFirstResult((moduleDetailsSearchDTO.getPageNumber() - 1) * moduleDetailsSearchDTO.getPageSize());
            q.setMaxResults(moduleDetailsSearchDTO.getPageSize());
        }
        /* return the page request of routing search dto */
        return PageRequest.of(moduleDetailsSearchDTO.getPageNumber() - 1, moduleDetailsSearchDTO.getPageSize(),
                sort.descending());
    }

    /**
     * @param predicates
     * @param moduleDetailsSearchDTO
     * @param cb
     * @param cq
     * @param rootModule
     * @return
     */
    private TypedQuery<ModuleDetailSearchResultsDTO> createPredicatesForModule(List<Predicate> predicates,
            ModuleDetailsSearchDTO moduleDetailsSearchDTO, CriteriaBuilder cb,
            CriteriaQuery<ModuleDetailSearchResultsDTO> cq, Root<ModuleDetails> rootModule) {
        log.info("ModuleDetailSearchResultsDTO::createPredicatesForModule");

        if (checkMandatory(moduleDetailsSearchDTO.getModuleCode())) {
            predicates.add(cb.like(cb.lower(rootModule.get("moduleCode")),
                    "%" + moduleDetailsSearchDTO.getModuleCode().toLowerCase() + "%"));
        }

        if (checkMandatory(moduleDetailsSearchDTO.getModuleDesc())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(rootModule.get("moduleDesc")),
                            "%" + moduleDetailsSearchDTO.getModuleDesc().toLowerCase() + "%"),
                    cb.like(cb.lower(rootModule.get("moduleDescOther")),
                            "%" + moduleDetailsSearchDTO.getModuleDesc().toLowerCase() + "%")));
        }

        if (checkMandatory(moduleDetailsSearchDTO.getActive())) {
            predicates.add(cb.equal(rootModule.get("active"), moduleDetailsSearchDTO.getActive()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(rootModule.get(ORDERNO)));

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
    private void projectionForModule(CriteriaQuery<ModuleDetailSearchResultsDTO> cq, CriteriaBuilder cb,
            Root<ModuleDetails> root) {
        log.info("ModuleDetailSearchResultsDTO::projectionForModule");
        cq.select(cb.construct(ModuleDetailSearchResultsDTO.class, root.get("moduleId"), root.get("moduleCode"),
                root.get("moduleDesc"), root.get("moduleDescOther"), root.get("active"), root.get(ORDERNO)));
    }

    /**
     * @param cq
     * @return
     */
    private Root<ModuleDetails> getRootForModule(CriteriaQuery<ModuleDetailSearchResultsDTO> cq) {
        log.info("ModuleDetailSearchResultsDTO::getRootForRole");
        return cq.from(ModuleDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ModuleDetailSearchResultsDTO> getCreateQueryForModule(CriteriaBuilder cb) {
        log.info("ModuleDetailSearchResultsDTO::getCreateQueryForRole");
        return cb.createQuery(ModuleDetailSearchResultsDTO.class);
    }

}
