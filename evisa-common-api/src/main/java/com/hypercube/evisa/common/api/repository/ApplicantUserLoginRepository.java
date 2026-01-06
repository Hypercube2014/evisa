package com.hypercube.evisa.common.api.repository;

import javax.websocket.server.PathParam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantUserLogin;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantUserLoginRepository extends JpaRepository<ApplicantUserLogin, String> {

    /**
     * @param username
     * @return
     */
    public ApplicantUserLogin findByUserNameIgnoreCase(String username);

    /**
     * @param username
     * @return
     */
    public boolean existsByUserNameIgnoreCase(String username);

    /**
     * @param userName
     * @param encodePassword
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ApplicantUserLogin aul SET aul.password = :encodePassword, aul.changePasswordRequired = true WHERE aul.userName = :userName ")
    public void forgetPasswordChange(@PathParam("userName") String userName,
            @PathParam("encodePassword") String encodePassword);

}
