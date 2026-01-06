package com.hypercube.evisa.approver.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLogin;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface EmployeeUserLoginRepository extends JpaRepository<EmployeeUserLogin, String> {

	/**
	 * @param username
	 * @return
	 */
	public EmployeeUserLogin findByUsernameIgnoreCase(String username);

	/**
	 * @param username
	 * @return
	 */
	public boolean existsByUsernameIgnoreCase(String username);

	/**
	 * @param username
	 * @param role
	 */
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE EmployeeUserLogin SET roles = :role WHERE username = :username ")
	public void updateUserRole(@Param("username") String username, @Param("role") String role);

	/**
	 * @param username
	 */
	@Query(value = "SELECT eul.password FROM EmployeeUserLogin eul WHERE eul.username = :username AND eul.roles = 'DMM'")
	public String getPassword(@Param("username") String username);

	/**
	 * @param username
	 * @param encodePassword
	 */
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE EmployeeUserLogin eul SET eul.password = :encodePassword, eul.changePasswordRequired = true WHERE eul.username = :username ")
	public void forgetPasswordChange(@Param("username") String username,
			@Param("encodePassword") String encodePassword);
}
