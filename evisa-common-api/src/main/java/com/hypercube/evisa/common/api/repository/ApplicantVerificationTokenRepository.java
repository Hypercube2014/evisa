/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantVerificationToken;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantVerificationTokenRepository extends JpaRepository<ApplicantVerificationToken, Long> {
    
    /**
     * @param token
     * @return
     */
    ApplicantVerificationToken findByToken(String token);

}
