/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantPassportTravelDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantPassportTravelDetailsRepository
        extends JpaRepository<ApplicantPassportTravelDetails, String> {
    
    /**
     * @param applicationNumber
     * @return
     */
    ApplicantPassportTravelDetails findByApplicationNumber(String applicationNumber);

    /**
     * @param applicationList
     * @return
     */
    @Query(value = "SELECT a.applicationNumber FROM ApplicantPassportTravelDetails a WHERE a.applicationNumber in :applicationList ")
    List<String> findApplications(@Param("applicationList") List<String> applicationList);

}
