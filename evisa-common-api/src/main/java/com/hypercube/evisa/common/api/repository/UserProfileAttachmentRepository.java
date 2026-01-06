package com.hypercube.evisa.common.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.UserProfileAttachment;

/**
 * @author SivaSreenivas
 *
 */
@Repository
@Transactional
public interface UserProfileAttachmentRepository extends JpaRepository<UserProfileAttachment, String> {

    /**
     * @param username
     * @return
     */
    UserProfileAttachment findByUsername(String username);

    /**
     * @param username
     */
    void deleteByUsername(String username);

}
