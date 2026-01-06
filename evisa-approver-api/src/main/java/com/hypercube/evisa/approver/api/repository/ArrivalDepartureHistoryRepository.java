package com.hypercube.evisa.approver.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.model.ArrivalDepartureHistory;
import com.hypercube.evisa.common.api.model.SBCOTrackerDTO;

import java.util.*;


@Repository
public interface ArrivalDepartureHistoryRepository extends JpaRepository<ArrivalDepartureHistory, Long> {

    @Query(value="SELECT s FROM ArrivalDepartureHistory s WHERE approver_role =:val")
    public List<ArrivalDepartureHistory> findByApprover_role(@Param("val") String approver_role);

    @Query(value="SELECT s FROM ArrivalDepartureHistory s WHERE approver =:val")
    public List<ArrivalDepartureHistory> findByApprover(@Param("val") String approver);

    @Query(value="SELECT new com.hypercube.evisa.common.api.model.SBCOTrackerDTO((select count(d) from ArrivalDepartureHistory d where d.status = 'APR' and d.action_date >= :startDate and d.action_date <= :endDate) as approved, (select count(e) from ArrivalDepartureHistory e where e.status = 'REJ' and e.action_date >= :startDate and e.action_date <= :endDate) as rejected) FROM ArrivalDepartureHistory s WHERE s.approver = :val group by s.approver")
    List<SBCOTrackerDTO> countByStatusAndApprover(@Param("val") String approver, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
