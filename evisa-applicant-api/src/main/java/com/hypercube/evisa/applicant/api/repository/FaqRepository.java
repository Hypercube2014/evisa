package com.hypercube.evisa.applicant.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.applicant.api.model.FAQ;

@Repository
public interface FaqRepository extends JpaRepository<FAQ, Long> {

}
