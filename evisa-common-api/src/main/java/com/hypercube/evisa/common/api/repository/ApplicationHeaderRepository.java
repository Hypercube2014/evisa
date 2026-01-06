/**
 * 
 */
package com.hypercube.evisa.common.api.repository;

import java.util.Date;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ApplicationHeaderCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicationHeader;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.DMDashboardDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicationHeaderRepository
                extends JpaRepository<ApplicationHeader, String>, ApplicationHeaderCustomsRepo {

        /**
         * @param loggeduser
         * @return
         */
        @Query(value = "SELECT COUNT(*) FROM ApplicationHeader WHERE assignedTo = :loggeduser AND documentStatus = 'PEN' ")
        int checkPendingAllocatedList(@Param("loggeduser") String loggeduser);

        /**
         * @param loggeduser
         * @param applicationList
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.assignedTo= :loggeduser, a.allocatedDate=CURRENT_TIMESTAMP, a.documentStatus = 'PEN' "
                        + "WHERE a.fileNumber in :applicationList ")
        void allocateSubmittedApplications(@Param("loggeduser") String loggeduser,
                        @Param("applicationList") List<String> applicationList);

        /**
         * @param username
         * @param role
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.assignedTo = null, a.allocatedDate = null, a.documentStatus = 'SUB' "
                        + "WHERE a.assignedTo = :username AND a.documentStatus = 'PEN' ")
        void deallocateApplications(@Param("username") String username);

        /**
         * @param applicationlist
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.assignedTo = null, a.allocatedDate = null, a.documentStatus = 'SUB' "
                        + "WHERE a.applicationNumber in :applicationlist AND a.documentStatus = 'PEN' ")
        void deallocateSelectedApplications(@Param("applicationlist") List<String> applicationlist);

        /**
         * @param agentusername
         * @return
         */
        @Query(value = "SELECT applicationNumber FROM ApplicationHeader WHERE assignedTo = :agentusername AND documentStatus = 'PEN' ")
        List<String> deallocateSelectedApplications(@Param("agentusername") String agentusername);

        /**
         * @param applicationNumber
         * @param oprType
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.arrDepIndicator = :oprType, a.arrDepId = :arrDepId WHERE a.applicationNumber = :applicationNumber ")
        void updateArrivalDepartureIndicator(@Param("applicationNumber") String applicationNumber,
                        @Param("oprType") String oprType, @Param("arrDepId") Long arrDepId);

        /**
         * @param applicationNumber
         * @param oprType
         * @param arrDepId
         * @param visaExpiryDate
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.arrDepIndicator = :oprType, a.arrDepId = :arrDepId, a.visaExpiry = :visaExpiryDate "
                        + "WHERE a.applicationNumber = :applicationNumber ")
        void updateVisaExpiryAlongWithArrivalDtls(@Param("applicationNumber") String applicationNumber,
                        @Param("oprType") String oprType, @Param("arrDepId") Long arrDepId,
                        @Param("visaExpiryDate") Date visaExpiryDate);

        /**
         * @param referenceNumber
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.documentStatus = 'SUB', a.visaStatus = 'UP' WHERE a.fileNumber = :referenceNumber ")
        void updatePaymentDetails(@Param("referenceNumber") String referenceNumber);

        /**
         * @param applicationNumber
         * @return
         */
        @Query(value = "SELECT vd.entryType FROM ManagementVisaDetails vd WHERE vd.visaType = ("
                        + "SELECT at.visaType FROM ApplicationTracker at WHERE at.applicationNumber = :applicationNumber )")
        String getEntryTypeByApplicationNumber(@Param("applicationNumber") String applicationNumber);

        /**
         * @param documentStatus
         * @return
         */
        @Query(value = "SELECT COUNT(h.documentStatus) FROM ApplicationHeader h "
                        + "WHERE h.documentStatus =:documentStatus")
        Long decisionMakerPendingCount(@Param("documentStatus") String documentStatus);

        /**
         * @param loggeduser
         * @param startDate
         * @param endDate
         * @return
         */
        @Query(value = "SELECT COUNT(h.documentStatus) FROM ApplicationHeader h "
                        + "WHERE h.assignedTo = :loggeduser AND h.documentStatus = 'CLS' AND (h.closedDate >= :startDate AND h.closedDate <= :endDate) ")
        Long decisionMakerClosedCount(@Param("loggeduser") String loggeduser, @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        /**
         * @param loggeduser
         * @param startDate
         * @param endDate
         * @return
         */

        /*
         * @Query(value =
         * "SELECT new com.hypercube.evisa.common.api.model.CountDTO(h.visaStatus, COUNT(h.visaStatus)) FROM ApplicationHeader h "
         * +
         * "WHERE h.assignedTo = :loggeduser AND (h.allocatedDate >= :startDate AND h.allocatedDate <= :endDate) GROUP BY h.visaStatus "
         * )
         * List<CountDTO> decisionMakerCount(@Param("loggeduser") String
         * loggeduser, @Param("startDate") Date startDate,
         * 
         * @Param("endDate") Date endDate);
         */
        @Query(value = "SELECT "
                        + "new com.hypercube.evisa.common.api.model.DMDashboardDTO("+

                        "(select count(f) from ApplicationHeader f where f.assignedTo = :loggeduser and f.allocatedDate >= :startDate and f.allocatedDate <= :endDate) as allocated,"+ 

                       "(select count(g) from ApplicationHeader g where g.assignedTo = :loggeduser and g.documentStatus = 'PEN' and g.visaStatus = 'UP' and g.allocatedDate >= :startDate and g.allocatedDate <= :endDate) as pending, "+ 

                       "(select count(i) from ApplicationHeader i where i.assignedTo = :loggeduser and i.documentStatus = 'CLS' and i.visaStatus = 'APR' and i.allocatedDate >= :startDate and i.allocatedDate <= :endDate) as approved, " +

                       "(select count(j) from ApplicationHeader j where j.assignedTo = :loggeduser and j.documentStatus = 'CLS' and j.visaStatus  = 'REJ' and j.allocatedDate >= :startDate and j.allocatedDate <= :endDate) as rejected, " +

                       "(select count(k) from ApplicationHeader k where k.assignedTo = :loggeduser and k.documentStatus = 'VAL' and k.visaStatus = 'UP' and k.allocatedDate >= :startDate and k.allocatedDate <= :endDate) as validation) "

                        + "FROM ApplicationHeader h "
                        + "WHERE h.assignedTo = :loggeduser ")
        List<DMDashboardDTO> decisionMakerCount(@Param("loggeduser") String loggeduser, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

        /**
         * @param extensionNumber
         * @param flagStatus
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.extensionApplied = :flagStatus WHERE a.applicationNumber = "
                        + "(SELECT e.applicationNumber FROM ApplicantVisaExtension e WHERE e.visaExtensionId = :extensionNumber) ")
        void updateExtensionAppliedFlag(@Param("extensionNumber") String extensionNumber,
                        @Param("flagStatus") String flagStatus);

        /**
         * @param expiryAfterExtension
         * @param applicationNumber
         */
        @Modifying(flushAutomatically = true)
        @Query(value = "UPDATE ApplicationHeader a SET a.visaExpiry = :expiryAfterExtension WHERE a.applicationNumber = :applicationNumber")
        void updateVisaExpiryAfterExtension(@Param("expiryAfterExtension") Date expiryAfterExtension,
                        @Param("applicationNumber") String applicationNumber);

}
