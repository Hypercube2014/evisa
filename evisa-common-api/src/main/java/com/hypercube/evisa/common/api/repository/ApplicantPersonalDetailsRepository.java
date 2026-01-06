/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ApplicantPersonalDetailsCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantPersonalDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantPersonalDetailsRepository
        extends JpaRepository<ApplicantPersonalDetails, String>, ApplicantPersonalDetailsCustomsRepo {

    /**
     * @param applicationNumber
     * @return
     */
    ApplicantPersonalDetails findByApplicationNumber(String applicationNumber);

    /**
     * @param fileNumber
     * @return
     */
    @Query(value = "SELECT a.applicationNumber FROM ApplicantPersonalDetails a WHERE a.fileNumber =:fileNumber ")
    List<String> findApplications(@Param("fileNumber") String fileNumber);

    /**
     * @param fileNumber
     * @return
     */
    @Query(value = "SELECT a.username FROM ApplicationFile a WHERE a.fileNumber =:fileNumber ")
    String findUsername(@Param("fileNumber") String fileNumber);

    /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT a.fileNumber FROM ApplicantPersonalDetails a WHERE a.applicationNumber =:applicationNumber ")
    String findFileNumber(@Param("applicationNumber") String applicationNumber);

}
