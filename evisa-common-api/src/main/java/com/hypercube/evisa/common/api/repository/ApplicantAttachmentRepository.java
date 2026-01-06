package com.hypercube.evisa.common.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantAttachmentDetails;
import com.hypercube.evisa.common.api.model.ApplicantAttachmentDetailsDTO;
import com.hypercube.evisa.common.api.model.GenericGroupCountDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
@Transactional
public interface ApplicantAttachmentRepository extends JpaRepository<ApplicantAttachmentDetails, Long> {

    /**
     * @param applicationNumber
     * @return
     */
    List<ApplicantAttachmentDetailsDTO> findByApplicationNumber(String applicationNumber);

    /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT v FROM ApplicantAttachmentDetails v WHERE v.applicationNumber = :applicationNumber ")
    List<ApplicantAttachmentDetails> findAllByApplicationNumber(@Param("applicationNumber") String applicationNumber);

    /**
     * @param attachmentId
     * @return
     */
    ApplicantAttachmentDetails findByAttachmentId(Long attachmentId);

    /**
     * @param applicationList
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.GenericGroupCountDTO(applicationNumber, COUNT(applicationNumber) ) "
            + "FROM ApplicantAttachmentDetails WHERE attachmentType in ('PP', 'PG', 'TK') AND applicationNumber in :applicationList "
            + "GROUP BY applicationNumber ")
    List<GenericGroupCountDTO> findApplications(@Param("applicationList") List<String> applicationList);

    /**
     * @param applicationNumber
     * @param attachmentType
     * @return
     */
    @Query(value = "SELECT v FROM ApplicantAttachmentDetails v WHERE v.applicationNumber = :applicationNumber AND attachmentType = :attachmentType ")
    List<ApplicantAttachmentDetails> findAttchDtlsByAppNoAndAttchType(@Param("applicationNumber") String applicationNumber,
            @Param("attachmentType") String attachmentType);

}