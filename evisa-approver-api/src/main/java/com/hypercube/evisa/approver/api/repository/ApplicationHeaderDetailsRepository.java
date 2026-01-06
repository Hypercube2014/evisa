package com.hypercube.evisa.approver.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.hypercube.evisa.approver.api.model.ApplicationHeaderDetails;
import com.hypercube.evisa.common.api.model.ApplicantionDetailsDTO;

/**
 * @author Mohamed Khaireh
 *
 */

@Repository
public interface ApplicationHeaderDetailsRepository extends JpaRepository<ApplicationHeaderDetails, Long> {

    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.ApplicantionDetailsDTO((SELECT count(b) FROM ApplicationHeaderDetails b WHERE b.allocatedDate is null AND b.documentStatus != 'PP') as nonAllocated, (SELECT count(c) FROM ApplicationHeaderDetails c WHERE c.allocatedDate is not null AND c.documentStatus = 'PEN') as nonClosed, (SELECT count(d) FROM ApplicationHeaderDetails d WHERE d.allocatedDate is not null AND d.allocatedDate >= :startDate AND d.allocatedDate <= :endDate) as allocated, (SELECT count(e) FROM ApplicationHeaderDetails e WHERE e.allocatedDate is not null AND e.documentStatus = 'CLS' AND closedDate is not null AND e.closedDate >= :startDate AND e.closedDate <= :endDate) as closed, (SELECT count(f) FROM ApplicationHeaderDetails f WHERE f.allocatedDate is not null AND f.documentStatus = 'VAL') as validation) FROM ApplicationHeaderDetails a")
    List<ApplicantionDetailsDTO> countApplicationDetails(@Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
