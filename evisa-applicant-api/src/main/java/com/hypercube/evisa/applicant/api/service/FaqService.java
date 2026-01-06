package com.hypercube.evisa.applicant.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.applicant.api.repository.FaqRepository;

import com.hypercube.evisa.applicant.api.model.FAQ;

@Service
public class FaqService {
    private final FaqRepository faqRepository;


    @Autowired
    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQ> getFaqDetails() {
        return faqRepository.findAll();
    }

}
