package com.hypercube.evisa.common.api.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.PayementCash;

@Repository
public interface PayementCashRepository extends JpaRepository<PayementCash, Long>{

	@Query("SELECT new com.hypercube.evisa.common.api.domain.PayementCash(applicationNumber, SUM(amount) AS amount) FROM PayementCash WHERE  applicationNumber= :application GROUP BY applicationNumber")
	PayementCash selectPaymentCash(@Param("application") String  applicationNumber);
	
	@Query("SELECT new com.hypercube.evisa.common.api.domain.PayementCash (COUNT(idPayementcash) AS idPayementcash)"+
              "FROM PayementCash WHERE approver =:approver AND Date(createdDate) BETWEEN :date1 AND :date2")
	PayementCash statistiqueCount(@Param("approver") String approver,@Param("date1") Date date1, @Param("date2") Date date2);
	
	@Query("SELECT new com.hypercube.evisa.common.api.domain.PayementCash (SUM(amount) AS amount)"+
            "FROM PayementCash WHERE approver =:approver AND Date(createdDate) BETWEEN :date1 AND :date2")
	PayementCash StatistiqueSum(@Param("approver") String approver,@Param("date1") Date date1, @Param("date2") Date date2);
}
