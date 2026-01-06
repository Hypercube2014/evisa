package com.hypercube.evisa.common.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtensionFileUpload;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionFileUploadDTO;
import com.hypercube.evisa.common.api.repository.ApplicantVisaExtensionFileUploadRepository;
import com.hypercube.evisa.common.api.service.ApplicantVisaExtensionFileUploadService;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@NoArgsConstructor
@Slf4j
public class ApplicantVisaExtensionFileUploadServiceImpl implements ApplicantVisaExtensionFileUploadService {
    
    /**
     * 
     */
    @Autowired(required = true)
    private ApplicantVisaExtensionFileUploadRepository ApplicantVisaExtensionFileUploadServiceRepo;

    /**
     *
     */
//    @Override
//    public ApplicantVisaExtensionFileUpload findByFileId(Long fileId) {
//    	System.out.println("ApplicantVisaExtensionFileUploadServiceImpl-findByFileId");
//        log.info("ApplicantVisaExtensionFileUploadServiceImpl-findByFileId");
//        return ApplicantVisaExtensionFileUploadServiceRepo.findByfileId(fileId);
//        		}

    /**
     *
     */
    @Override
    public List<ApplicantVisaExtensionFileUploadDTO> findByVisaExtensionId(String visaExtensionId) {
        log.info("ApplicantVisaExtensionFileUploadServiceImpl-findByVisaExtensionId");
        return ApplicantVisaExtensionFileUploadServiceRepo.findByVisaExtensionId(visaExtensionId);
    }

    /**
     *
     */
    @Override
    public ApplicantVisaExtensionFileUpload saveExtensionFileUpload(
            ApplicantVisaExtensionFileUpload applicantVisaExtensionFileUpload) {
        log.info("ApplicantVisaExtensionFileUploadServiceImpl-saveExtensionFileUpload");
        return ApplicantVisaExtensionFileUploadServiceRepo.save(applicantVisaExtensionFileUpload);
    }

}
