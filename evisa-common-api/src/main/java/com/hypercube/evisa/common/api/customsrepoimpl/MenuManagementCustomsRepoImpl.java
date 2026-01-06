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

import com.hypercube.evisa.common.api.customsrepo.MenuManagementCustomsRepo;
import com.hypercube.evisa.common.api.domain.MenuDetails;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchResultsDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@NoArgsConstructor
@Slf4j
public class MenuManagementCustomsRepoImpl implements MenuManagementCustomsRepo {

    /**
     * static declarations for the role id
     */
    private static final String MENUID = "menuId";

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<MenuDetailsSearchResultsDTO> searchMenuDetails(MenuDetailsSearchDTO menuDetailsSearchDTO) {
        log.info("::MenuDetailsSearchResultsDTO::searchMenuDetails");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<MenuDetailsSearchResultsDTO> cq = getCreateQueryForMenu(cb);
        Root<MenuDetails> root = getRootForMenu(cq);

        projectionForMenu(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<MenuDetailsSearchResultsDTO> q = createPredicatesForMenu(predicates, menuDetailsSearchDTO, cb, cq,
                root);
        int count = getMenuResultSize(getMenuResults(q));
        Pageable sortedByIdName = getPageableForMenu(menuDetailsSearchDTO, q, Sort.by(MENUID));

        /* return the routing search results dto */
        return new PageImpl<MenuDetailsSearchResultsDTO>(getMenuResults(q), sortedByIdName, count);
    }

    /**
     * @param menuResults
     * @return
     */
    private int getMenuResultSize(List<MenuDetailsSearchResultsDTO> menuResults) {
        log.info("MenuDetailsSearchResultsDTO::menuResults");
        /* return the list size */
        return menuResults.size();
    }

    /**
     * @param results
     * @return
     */
    private List<MenuDetailsSearchResultsDTO> getMenuResults(TypedQuery<MenuDetailsSearchResultsDTO> results) {
        log.info("createPredicatesForMenu::getMenuResults");
        /* return the result list */
        return results.getResultList();
    }

    /**
     * @param menuDetailsSearchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForMenu(MenuDetailsSearchDTO menuDetailsSearchDTO,
            TypedQuery<MenuDetailsSearchResultsDTO> q, Sort sort) {
        log.info("MenuDetailsSearchResultsDTO::getPageableForMenu");
        if (menuDetailsSearchDTO.getPageNumber() > 0 && menuDetailsSearchDTO.getPageSize() > 0) {
            q.setFirstResult((menuDetailsSearchDTO.getPageNumber() - 1) * menuDetailsSearchDTO.getPageSize());
            q.setMaxResults(menuDetailsSearchDTO.getPageSize());
        }
        /* return the page request of routing search dto */
        return PageRequest.of(menuDetailsSearchDTO.getPageNumber() - 1, menuDetailsSearchDTO.getPageSize(),
                sort.descending());
    }

    /**
     * @param predicates
     * @param menuDetailsSearchDTO
     * @param cb
     * @param cq
     * @param rootRole
     * @return
     */
    private TypedQuery<MenuDetailsSearchResultsDTO> createPredicatesForMenu(List<Predicate> predicates,
            MenuDetailsSearchDTO menuDetailsSearchDTO, CriteriaBuilder cb,
            CriteriaQuery<MenuDetailsSearchResultsDTO> cq, Root<MenuDetails> rootRole) {
        log.info("RoleDetailsSearchResultsDTO::createPredicatesForRole");

        if (checkMandatory(menuDetailsSearchDTO.getMenuCode())) {
            predicates.add(cb.like(cb.lower(rootRole.get("menuCode")),
                    "%" + menuDetailsSearchDTO.getMenuCode().toLowerCase() + "%"));
        }

        if (checkMandatory(menuDetailsSearchDTO.getMenuName())) {
            predicates.add(cb.or(
                    cb.like(cb.lower(rootRole.get("menuName")),
                            "%" + menuDetailsSearchDTO.getMenuName().toLowerCase() + "%"),
                    cb.like(cb.lower(rootRole.get("menuNameOther")),
                            "%" + menuDetailsSearchDTO.getMenuName().toLowerCase() + "%")));
        }

        if (checkMandatory(menuDetailsSearchDTO.getStatus())) {
            predicates.add(cb.equal(rootRole.get("status"), menuDetailsSearchDTO.getStatus()));
        }
        if (checkMandatory(menuDetailsSearchDTO.getModuleCode())) {
            predicates.add(
                    cb.equal(cb.lower(rootRole.get("moduleCode")), menuDetailsSearchDTO.getModuleCode().toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(rootRole.get(MENUID)));

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
    private void projectionForMenu(CriteriaQuery<MenuDetailsSearchResultsDTO> cq, CriteriaBuilder cb,
            Root<MenuDetails> root) {
        log.info("MenuDetailsSearchResultsDTO::projectionForMenu");
        cq.select(cb.construct(MenuDetailsSearchResultsDTO.class, root.get(MENUID), root.get("menuCode"),
                root.get("menuName"), root.get("menuNameOther"), root.get("status"), root.get("moduleCode")));
    }

    /**
     * @param cq
     * @return
     */
    private Root<MenuDetails> getRootForMenu(CriteriaQuery<MenuDetailsSearchResultsDTO> cq) {
        log.info("MenuDetailsSearchResultsDTO::getRootForMenu");
        return cq.from(MenuDetails.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<MenuDetailsSearchResultsDTO> getCreateQueryForMenu(CriteriaBuilder cb) {
        log.info("MenuDetailsSearchResultsDTO::getCreateQueryForMenu");
        return cb.createQuery(MenuDetailsSearchResultsDTO.class);
    }

}
