package com.hypercube.evisa.approver.api.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.model.ArrivalDepartureHistory;
import com.hypercube.evisa.approver.api.repository.ArrivalDepartureHistoryRepository;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.SBCOTrackerDTO;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArrivalDepartureHistoryService {
    private final ArrivalDepartureHistoryRepository arrivalDepartureHistoryRepository;

    public ArrivalDepartureHistoryService(ArrivalDepartureHistoryRepository arrivalDepartureHistoryRepository) {
        super();
        this.arrivalDepartureHistoryRepository = arrivalDepartureHistoryRepository;
    }

    public List<ArrivalDepartureHistory> getDetailsByApproverRole(String approver_role) {
        return arrivalDepartureHistoryRepository.findByApprover_role(approver_role);
    }

    public List<ArrivalDepartureHistory> getDetailsByApprover(String approver) {
        return arrivalDepartureHistoryRepository.findByApprover(approver);
    }

    public List<SBCOTrackerDTO> getSBCOApprovedReject(String approver, String period) {

        GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(period);

        return arrivalDepartureHistoryRepository.countByStatusAndApprover(approver, genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());
    }
}
