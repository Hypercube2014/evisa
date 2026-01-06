/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantUserLoginHistory;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantUserLoginHistoryRepository extends JpaRepository<ApplicantUserLoginHistory, Long> {

}
