package com.hypercube.evisa.common.api.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ApplicantPaymentCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicantPaymentDetails;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ApplicantPaymentRepository
        extends JpaRepository<ApplicantPaymentDetails, String>, ApplicantPaymentCustomsRepo {

    /**
     * @param referenceNumber
     * @param sessionId
     */
    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicantPaymentDetails apd SET apd.paymentInstructions = :sessionId WHERE apd.fileNumber = :referenceNumber AND apd.status = 'PP' ")
    void updatePaymentSessionId(@Param("referenceNumber") String referenceNumber, @Param("sessionId") String sessionId);

    
    
    
    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicantPaymentDetails apd SET apd.paymentInstructions = :sessionId, apd.amountDue= :amount  WHERE apd.fileNumber = :referenceNumber")
    void updatePaymentAmount(@Param("referenceNumber") String referenceNumber, @Param("sessionId") String sessionId, @Param("amount") long amount );
    /**
     * @param status
     * @param amount
     * @param paymentMethod
     * @param sessionId
     */
//    @Modifying(flushAutomatically = true)
//    @Query(value = "UPDATE ApplicantPaymentDetails apd "
//            + "SET apd.status = :status, apd.paymentMethod = :paymentMethod, apd.amountPaid = apd.amountDue, apd.amountDue = :amount, apd.updatedDate = :updatedDate "
//            + "WHERE apd.paymentInstructions = :sessionId")
//    void updatePaymentSuccess(@Param("status") String status, @Param("amount") long amount,
//            @Param("paymentMethod") String paymentMethod, @Param("sessionId") String sessionId,
//            @Param("updatedDate") Date updatedDate);


    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicantPaymentDetails apd "
            + "SET apd.status = :status, apd.paymentMethod = :paymentMethod, apd.amountPaid = apd.amountDue, "
            + "apd.amountDue = CASE WHEN :status = 'PC' THEN 0 ELSE :amount END, "
            + "apd.updatedDate = :updatedDate "
            + "WHERE apd.paymentInstructions = :sessionId")
    void updatePaymentSuccess(@Param("status") String status, @Param("amount") long amount,
                              @Param("paymentMethod") String paymentMethod, @Param("sessionId") String sessionId,
                              @Param("updatedDate") Date updatedDate);
    
    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ApplicantPaymentDetails apd "
            + "SET apd.status = :status, apd.paymentMethod = :paymentMethod, apd.amountPaid =apd.amountPaid + apd.amountDue, apd.amountDue = :amount, apd.updatedDate = :updatedDate "
            + "WHERE apd.paymentInstructions = :sessionId")
    void updatePaymentPenalitySuccess(@Param("status") String status, @Param("amount") long amount,
            @Param("paymentMethod") String paymentMethod, @Param("sessionId") String sessionId,
            @Param("updatedDate") Date updatedDate);

    /**
     * @param sessionId
     * @return
     */
    @Query(value = "SELECT apd FROM ApplicantPaymentDetails apd WHERE apd.paymentInstructions = :sessionId ")
    ApplicantPaymentDetails getFileNumberBySessionId(@Param("sessionId") String sessionId);
    /**
     * @param filenumber
     * @return
     */
    ApplicantPaymentDetails findByFileNumber(String filenumber);

}
