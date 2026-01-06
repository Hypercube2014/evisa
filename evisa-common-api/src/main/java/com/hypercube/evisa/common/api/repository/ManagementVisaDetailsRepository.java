/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ManagementVisaDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ManagementVisaDetailsRepository extends JpaRepository<ManagementVisaDetails, Long>, ManagementVisaDetailsCustomsRepo {

    /**
     * @param visaId
     * @return
     */
    ManagementVisaDetails findByVisaId(Long visaId);

    /**
     * @param visaType
     * @return
     */
    boolean existsByVisaType(String visaType);

    /**
     * @param status
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.MasterCodeResultDTO(m.visaType, m.visaDescription, m.visaDescriptionOther) "
            + "FROM ManagementVisaDetails m WHERE m.status =:status AND m.entryType NOT IN('E') ORDER BY m.visaType ")
    List<MasterCodeResultDTO> findByStatus(@Param("status") String status);

    /**
     * @param status
     * @param expressVisa
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.MasterCodeResultDTO(m.visaType, m.visaDescription, m.visaDescriptionOther) "
            + "FROM ManagementVisaDetails m WHERE m.status =:status AND m.isExpressVisaAllowed =:expressVisa ORDER BY m.visaType ")
    List<MasterCodeResultDTO> findByExpressVisaAndStatus(@Param("expressVisa") boolean expressVisa,
            @Param("status") String status);

    /**
     * @param visaType
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.MasterCodeResultDTO(m.visaType, m.visaDescription, m.visaDescriptionOther) "
            + "FROM ManagementVisaDetails m WHERE m.visaType =:visaType ")
    MasterCodeResultDTO getVisaDetailsByVisaType(@Param("visaType") String visaType);

    /**
     * @param visaType
     * @return
     */
    ManagementVisaDetails findByVisaType(String visaType);

    /**
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.MasterCodeResultDTO(m.visaType, m.visaDescription, m.visaDescriptionOther, m.visaDuration, m.visaFee) "
            + "FROM ManagementVisaDetails m WHERE m.entryType = 'E' AND m.status = 'Y' ORDER BY m.visaType ")
    List<MasterCodeResultDTO> findActiveExtensionVisaTypes();
}
