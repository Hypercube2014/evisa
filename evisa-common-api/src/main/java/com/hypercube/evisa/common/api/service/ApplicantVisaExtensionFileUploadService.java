package com.hypercube.evisa.common.api.service;

import java.util.List;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionFileUpload;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionFileUploadDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantVisaExtensionFileUploadService {
    
    /**
     * @param applicantVisaExtensionFileUpload
     * @return
     */
    ApplicantVisaExtensionFileUpload saveExtensionFileUpload(ApplicantVisaExtensionFileUpload applicantVisaExtensionFileUpload);
    
    /**
     * @param fileId
     * @return
     */
   // ApplicantVisaExtensionFileUpload findByFileId(Long fileId);
    
    /**
     * @param visaExtensionId
     * @return
     */
    List<ApplicantVisaExtensionFileUploadDTO> findByVisaExtensionId(String visaExtensionId);

}
