package com.hypercube.evisa.common.api.customsrepoimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.customsrepo.RoleMenuCustomsRepo;
import com.hypercube.evisa.common.api.model.RoleMenuMapProj;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class RoleMenuCustomsRepoImpl implements RoleMenuCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * findallRoleMenuMap
     */
    @Override
    public List<RoleMenuMapProj> findallRoleMenuMap(Long roleId, String status) {
        log.info("RoleMenuCustomsRepoImpl::findallRoleMenuMap");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(
                "SELECT modu.moduleCode ,modu.moduleDesc, menu.menuCode, menu.menuName as menuName,(SELECT 'true'  FROM RoleMenuPrivilege roleprev  WHERE roleprev.menuId = menu.menuId AND roleprev.roleId  = '"
                        + roleId
                        + "') AS selected,menu.menuId,modu.orderNo FROM ModuleDetails modu,MenuDetails menu WHERE menu.moduleCode = modu.moduleCode ");
        queryBuilder.append("AND menu.status = '" + status + "' AND  modu.active= '" + status + "'")
                .append(" order by modu.orderNo asc");

        Query query = entityManager.createQuery(queryBuilder.toString());

        return getlist(query);

    }

    /**
     * @param query
     * @return
     */
    private List<RoleMenuMapProj> getlist(Query query) {
        @SuppressWarnings("unchecked")
        List<Object> lis = query.getResultList();
        Iterator<Object> iterator = lis.iterator();
        List<RoleMenuMapProj> listdto = new ArrayList<RoleMenuMapProj>();
        while (iterator.hasNext()) {
            Object[] row = (Object[]) iterator.next();
            if (row != null) {
                RoleMenuMapProj dto = new RoleMenuMapProj();
                dto.setModuleCode((String) row[CommonsConstants.ZERO]);
                dto.setModuleDesc((String) row[CommonsConstants.ONE]);
                dto.setMenuCode((String) row[CommonsConstants.TWO]);
                dto.setMenuName((String) row[CommonsConstants.THREE]);
                if (null != row[CommonsConstants.FOUR]) {
                    dto.setSelected(Boolean.parseBoolean((String) row[CommonsConstants.FOUR]));
                }
                dto.setMenuId((Long) row[CommonsConstants.FIVE]);
                dto.setOrderNo((int) row[CommonsConstants.SIX]);
                listdto.add(dto);

            }
        }
        return listdto;
    }

}
