package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantUserLoginAttempts;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantUserLoginAttemptsRepository extends JpaRepository<ApplicantUserLoginAttempts, Long> {

    /**
     * @param username
     * @return
     */
    public boolean existsByUsername(String username);

    /**
     * @param username
     * @return
     */
    public ApplicantUserLoginAttempts findByUsername(String username);

    /**
     * @param accountlocked
     * @param username
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update {h-schema}applicant_user_login set account_non_locked =:accountlocked WHERE username =:username", nativeQuery = true)
    public void updateUserAccountLock(@Param("accountlocked") boolean accountlocked,
            @Param("username") String username);

    /**
     * @param username
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update {h-schema}applicant_user_login_attempts set attempts = 0, last_modified = null WHERE username =:username", nativeQuery = true)
    public void resetFailAttempts(@Param("username") String username);

    /**
     * @param changepassword
     * @param username
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update {h-schema}applicant_user_login set change_password_required =:changepassword WHERE username =:username", nativeQuery = true)
    public void updateChangePasswordRequired(@Param("changepassword") boolean changepassword,
            @Param("username") String username);

    /**
     * @param result
     * @param userName
     * @param role
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update {h-schema}applicant_user_login set account_enabled =:accountenabled, roles =:role  WHERE username =:username", nativeQuery = true)
    public void updateUserAccountEnableAndRole(@Param("accountenabled") boolean accountenabled,
            @Param("username") String username, @Param("role") String role);

    /**
     * @param accountenabled
     * @param username
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update {h-schema}applicant_user_login set account_enabled =:accountenabled where username =:username", nativeQuery = true)
    public void updateUserAccountEnabled(@Param("accountenabled") boolean accountenabled,
            @Param("username") String username);

}
