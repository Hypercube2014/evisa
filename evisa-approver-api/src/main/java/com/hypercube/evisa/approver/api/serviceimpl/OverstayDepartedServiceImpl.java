package com.hypercube.evisa.approver.api.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.approver.api.domain.OverstayDeparted;
import com.hypercube.evisa.approver.api.repository.OverstayDepartedRepository;
import com.hypercube.evisa.approver.api.service.OverstayDepartedService;
import com.hypercube.evisa.common.api.service.ApplicationHeaderService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class OverstayDepartedServiceImpl implements OverstayDepartedService {

	@Autowired
	private OverstayDepartedRepository repository;
	
	@Override
	public List<OverstayDeparted> findAllOverstayDeparted() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

}
