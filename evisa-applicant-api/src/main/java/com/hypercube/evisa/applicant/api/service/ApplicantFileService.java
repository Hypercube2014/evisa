package com.hypercube.evisa.applicant.api.service;

import org.springframework.http.ResponseEntity;


import com.hypercube.evisa.applicant.api.model.ApplicationFileDTO;
import com.hypercube.evisa.common.api.domain.ApplicationFile;


/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantFileService {

    /**
     * @param applicantFileDTO
     * @return
     */
    ResponseEntity<ApplicationFile> createDraftApplicationFile(ApplicationFileDTO applicantFileDTO);

    /**
     * @param fileNumber
     * @return
     */
    ResponseEntity<ApplicationFile> findApplicationsByFileNumber(String fileNumber);

    /**
     * @param fileNumber
     * @param string
     * @param count
     */
    void updateFileStatusAndCount(String fileNumber, String status, int count);

}
