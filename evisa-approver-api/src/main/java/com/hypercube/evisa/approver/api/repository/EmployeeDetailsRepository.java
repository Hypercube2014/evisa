package com.hypercube.evisa.approver.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.customsrepo.EmployeeDetailsCustomsRepo;
import com.hypercube.evisa.approver.api.domain.EmployeeDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, String>, EmployeeDetailsCustomsRepo {

	/**
	 * @param username
	 * @return
	 */
	boolean existsByUsername(String username);

	/**
	 * @param username
	 * @return
	 */
	EmployeeDetails findByUsername(String username);

	/**
	 * @param roleCode
	 * @return
	 */
	@Query(value = "SELECT new com.hypercube.evisa.approver.api.domain.EmployeeDetails(e.username, e.fullName, e.email) FROM EmployeeDetails e WHERE e.role = :roleCode")
	List<EmployeeDetails> getEmployeesByRole(@Param("roleCode") String roleCode);

	/**
	 * @param username
	 * @param status
	 */
	@Modifying
	@Query(value = "UPDATE EmployeeDetails e SET e.employementStatus = :status WHERE e.username = :username ")
	void updateEmployeeStatus(@Param("username") String username, @Param("status") String status);

	/**
	 * @param loggeduser
	 * @param role
	 * @return
	 */
	@Query(value = "SELECT e.username FROM EmployeeDetails e WHERE e.reportingTo = :loggeduser AND e.role = :role ")
	List<String> employeesReportToManager(@Param("loggeduser") String loggeduser, @Param("role") String role);

	/**
	 * @param reportingManager
	 * @param agentList
	 */
	@Modifying
	@Query(value = "UPDATE EmployeeDetails e SET e.reportingTo = :reportingManager WHERE e.username in :agentList ")
	void transferAgents(@Param("reportingManager") String reportingManager, @Param("agentList") List<String> agentList);

	/**
	 * @param emailid
	 * @return
	 */
	EmployeeDetails findByEmailIgnoreCase(String emailid);

	/**
	 * @param emailid
	 * @return
	 */
	boolean existsByEmailIgnoreCase(String emailid);

}
