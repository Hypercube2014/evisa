package com.hypercube.evisa.common.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionFileUpload;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionFileUploadDTO;

/**
 * @author SivaSreenivas
 *
 */
@Repository
@Transactional
public interface ApplicantVisaExtensionFileUploadRepository
        extends JpaRepository<ApplicantVisaExtensionFileUpload, Long> {

    /**
     * @param fileId
     * @return
     */
   // ApplicantVisaExtensionFileUpload findByfileId(Long fileId);
    
    /**
     * @param visaExtensionId
     * @return
     */
    List<ApplicantVisaExtensionFileUploadDTO> findByVisaExtensionId(String visaExtensionId);
}