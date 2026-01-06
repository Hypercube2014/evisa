package com.hypercube.evisa.common.api.repository;

import java.time.LocalDate;
import java.util.Date;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ApplicationTrackerCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.CountDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicationTrackerRepository
        extends JpaRepository<ApplicationTracker, String>, ApplicationTrackerCustomsRepo {

    /**
     * @param limit
     * @return
     */
    @Query(value = "SELECT DISTINCT(a.fileNumber) FROM ApplicationTracker a WHERE a.documentStatus = 'SUB' AND a.isExpressVisa = :isExpressVisa "
            + "ORDER BY a.fileNumber ASC ")
    List<String> pendingVisaProcessList(@Param("isExpressVisa") boolean isExpressVisa, Pageable pageable);

    /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT t.visaDuration FROM ApplicationTracker t WHERE t.applicationNumber = :applicationNumber ")
    int getVisaDuration(@Param("applicationNumber") String applicationNumber);

        /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT t.arrivalDate FROM ApplicationTracker t WHERE t.applicationNumber = :applicationNumber ")
    LocalDate getArrivalDate(@Param("applicationNumber") String applicationNumber);


    /**
     * @param applicationNumber
     * @return
     */
    ApplicationTracker findByApplicationNumber(String applicationNumber);

    /**
     * @param countries
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.CountDTO(vat.nationality, Count(*)) from ApplicationTracker vat "
            + "WHERE vat.nationality IN(:countries) AND (closedDate >= :startDate AND closedDate <= :endDate) GROUP BY vat.nationality")
    List<CountDTO> countryStatistics(@Param("countries") List<String> countries, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT t.remarks FROM ApproverHistoryDetails t WHERE t.applicationNumber = :applicationNumber AND t.status IN('APR', 'REJ')")
    String getApproverRemarks(@Param("applicationNumber") String applicationNumber);
    
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.CountDTO(vat.nationality, Count(*)) from ApplicationTracker vat  WHERE closedDate >= :startDate AND closedDate <= :endDate GROUP BY vat.nationality ORDER BY Count(*) DESC ")
    List<CountDTO> top5countryStatistics(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

}
