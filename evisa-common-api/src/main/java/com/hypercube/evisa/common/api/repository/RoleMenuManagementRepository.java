package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.RoleMenuCustomsRepo;
import com.hypercube.evisa.common.api.domain.RoleMenuPrivilege;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface RoleMenuManagementRepository extends JpaRepository<RoleMenuPrivilege, Long>, RoleMenuCustomsRepo {
   
    
    /**
     * @param roleId
     */
    @Modifying
    @Query("delete from RoleMenuPrivilege u where u.roleId=?1")
    void deleteRoleMenuWithMenuIds(Long roleId );
}
