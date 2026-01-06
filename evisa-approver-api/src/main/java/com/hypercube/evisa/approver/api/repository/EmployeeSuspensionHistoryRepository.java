package com.hypercube.evisa.approver.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.domain.EmployeeSuspensionHistory;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface EmployeeSuspensionHistoryRepository extends JpaRepository<EmployeeSuspensionHistory, Long> {

}
