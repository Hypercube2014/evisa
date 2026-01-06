package com.hypercube.evisa.common.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ApplicantVisaExtensionCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.model.CountDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantVisaExtensionRepository
        extends JpaRepository<ApplicantVisaExtension, Long>, ApplicantVisaExtensionCustomsRepo {
	
    /**
     * @param referenceNumber
     */
    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicantVisaExtension a SET a.documentStatus = 'SUB', a.extensionStatus = 'UP' WHERE a.visaExtensionId = :referenceNumber ")
    void updatePaymentDetails(@Param("referenceNumber") String referenceNumber);

    /**
     * @param extensionId
     * @return
     */
    ApplicantVisaExtension findByVisaExtensionId(String extensionId);

    /**
     * @param loggeduser
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM ApplicantVisaExtension WHERE assignedTo = :loggeduser AND documentStatus = 'PEN' ")
    int checkExtensionPendingAllocatedList(@Param("loggeduser") String loggeduser);

    /**
     * @param pageable
     * @return
     */
    @Query(value = "SELECT a.visaExtensionId FROM ApplicantVisaExtension a WHERE a.documentStatus = 'SUB' "
            + "ORDER BY a.visaExtensionId ASC ")
    List<String> pendingExtensionVisaProcessList(Pageable pageable);

    /**
     * @param loggeduser
     * @param extensionVisaAppList
     */
    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicantVisaExtension a SET a.assignedTo= :loggeduser, a.allocatedDate=CURRENT_TIMESTAMP, a.documentStatus = 'PEN' "
            + "WHERE a.visaExtensionId in :extensionVisaAppList ")
    void allocateSubmittedExtensionApplications(@Param("loggeduser") String loggeduser,
            @Param("extensionVisaAppList") List<String> extensionVisaAppList);

    /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT a.newExpiryDate FROM ApplicantVisaExtension a WHERE a.applicationNumber = :applicationNumber AND a.extensionStatus = 'APR'")
    Date getNewExipredDate(@Param("applicationNumber") String applicationNumber);

    /**
     * @param loggeduser
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "SELECT COUNT(h.documentStatus) FROM ApplicantVisaExtension h "
            + "WHERE h.assignedTo = :loggeduser AND h.documentStatus = 'PEN' AND (h.allocatedDate >= :startDate AND h.allocatedDate <= :endDate) ")
    Long decisionMakerExtensionPendingCount(String loggeduser, Date startDate, Date endDate);

    /**
     * @param loggeduser
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "SELECT COUNT(h.documentStatus) FROM ApplicantVisaExtension h "
            + "WHERE h.assignedTo = :loggeduser AND h.documentStatus = 'CLS' AND (h.closedDate >= :startDate AND h.closedDate <= :endDate) ")
    Long decisionMakerExtensionClosedCount(String loggeduser, Date startDate, Date endDate);

    /**
     * @param loggeduser
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "SELECT new com.hypercube.evisa.common.api.model.CountDTO(h.extensionStatus, COUNT(h.extensionStatus)) FROM ApplicantVisaExtension h "
            + "WHERE h.assignedTo = :loggeduser AND (h.closedDate >= :startDate AND h.closedDate <= :endDate) GROUP BY h.extensionStatus")
    List<CountDTO> decisionMakerCount(String loggeduser, Date startDate, Date endDate);

    /**
     * @param applicationNumber
     * @return
     */
    @Query(value = "SELECT v.visaDescription FROM ManagementVisaDetails v WHERE v.visaType = "
            + "(SELECT e.visaType FROM ApplicantVisaExtension e WHERE e.applicationNumber = :applicationNumber AND e.extensionStatus = 'APR')")
    String getExtensionTypeByApplicationNumber(@Param("applicationNumber") String applicationNumber);

}
