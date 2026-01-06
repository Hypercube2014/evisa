package com.hypercube.evisa.approver.api.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.repository.ApplicationHeaderDetailsRepository;
import com.hypercube.evisa.common.api.model.ApplicantionDetailsDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;

/**
 * @author Mohamed Khaireh
 *
 */

@Service
@Data
public class ApplicationHeaderDetailsService {

    private final ApplicationHeaderDetailsRepository applicationHeaderDetailsRepository;

    public List<ApplicantionDetailsDTO> getApplicationList(String period) {
        GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(period);

        /* String nonAllocated = applicationHeaderDetailsRepository.countApplicationDetails().toString();
        String nonClosed = applicationHeaderDetailsRepository.countNoneClosedApplication().toString();
        String allocated = applicationHeaderDetailsRepository.countAllocatedApplicationToday(per).toString();
        String closed = applicationHeaderDetailsRepository.countClosedApplicationToday(per).toString(); */

        List<ApplicantionDetailsDTO> applicationDetailDTO = applicationHeaderDetailsRepository.countApplicationDetails(genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());


        return applicationDetailDTO;
    }
}
