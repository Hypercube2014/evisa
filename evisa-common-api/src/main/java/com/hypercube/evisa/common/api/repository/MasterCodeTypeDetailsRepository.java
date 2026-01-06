package com.hypercube.evisa.common.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.MasterCodeTypeDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.MasterCodeTypeDetails;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface MasterCodeTypeDetailsRepository
        extends JpaRepository<MasterCodeTypeDetails, Long>, MasterCodeTypeDetailsCustomsRepo {

    /**
     * @param active
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.MasterCodeResultDTO(m.codeType, m.description, m.descriptionOther) "
            + "FROM MasterCodeTypeDetails m WHERE m.active =:active ORDER BY m.codeType")
    List<MasterCodeResultDTO> findByActive(@Param("active") String active);

    /**
     * @param codeType
     * @return
     */
    boolean existsByCodeType(String codeType);

}
