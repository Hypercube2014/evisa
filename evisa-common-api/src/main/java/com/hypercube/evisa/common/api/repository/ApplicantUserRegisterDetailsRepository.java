package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hypercube.evisa.common.api.domain.ApplicantUserRegisterDetails;

/**
 * @author sivasreenivas
 *
 */
public interface ApplicantUserRegisterDetailsRepository
        extends JpaRepository<ApplicantUserRegisterDetails, String> {

    /**
     * @param username
     * @return
     */
    ApplicantUserRegisterDetails findByUserNameIgnoreCase(String username);

    /**
     * @param username
     * @return
     */
    boolean existsByUserNameIgnoreCase(String username);

    /**
     * @param active
     * @param username
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE {h-schema}tsys_applicant_user SET active =:active  WHERE username =:username", nativeQuery = true)
    int updateAccountStatus(@Param("active") String active, @Param("username") String username);

    /**
     * @param emailId
     * @return
     */
    boolean existsByEmailIdIgnoreCase(String emailId);

    /**
     * @param emailid
     * @return
     */
    ApplicantUserRegisterDetails findByEmailIdIgnoreCase(String emailid);

}
