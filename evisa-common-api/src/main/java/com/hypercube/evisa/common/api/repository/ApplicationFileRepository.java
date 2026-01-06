/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicationFile;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicationFileRepository extends JpaRepository<ApplicationFile, Long> {

    /**
     * @param fileNumber
     * @return
     */
    ApplicationFile findByFileNumber(String fileNumber);

    /**
     * @param fileNumber
     * @param status
     */
    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicationFile a set a.status =:status, a.totalGroupApplications =:count WHERE a.fileNumber =:fileNumber ")
    void updateFileStatusAndCount(@Param("fileNumber") String fileNumber, @Param("status") String status, @Param("count") int count);

}
