/**
 * 
 */
package com.hypercube.evisa.applicant.api.serviceimpl;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.applicant.api.model.ApplicationFileDTO;
import com.hypercube.evisa.applicant.api.service.ApplicantFileService;
import com.hypercube.evisa.common.api.domain.ApplicationFile;
import com.hypercube.evisa.common.api.repository.ApplicationFileRepository;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
@Service
public class ApplicantFileServiceImpl implements ApplicantFileService {

    /**
     * 
     */
    @Autowired(required = true)
    private ApplicationFileRepository applicationFileRepository;

    /**
     * 
     */
    @Autowired(required = true)
    ModelMapper modelMapper;

    /**
     * 
     */
    @Override
    public ResponseEntity<ApplicationFile> createDraftApplicationFile(ApplicationFileDTO applicantFileDTO) {
        log.info("ApplicantFileServiceImpl-createDraftApplicationFile");

        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(applicantFileDTO.getUsername())) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(
                applicationFileRepository.save(modelMapper.map(applicantFileDTO, ApplicationFile.class)),
                HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public ResponseEntity<ApplicationFile> findApplicationsByFileNumber(String fileNumber) {
        log.info("ApplicantFileServiceImpl-findApplicationsByFileNumber");
        return new ResponseEntity<>(applicationFileRepository.findByFileNumber(fileNumber), HttpStatus.OK);
    }

    /**
     * 
     */
    @Override
    public void updateFileStatusAndCount(String fileNumber, String status, int count) {
        log.info("ApplicantFileServiceImpl-updateFileStatus");
        applicationFileRepository.updateFileStatusAndCount(fileNumber, status, count);

    }

}
