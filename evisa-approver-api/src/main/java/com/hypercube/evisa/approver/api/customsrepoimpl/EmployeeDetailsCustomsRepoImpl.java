package com.hypercube.evisa.approver.api.customsrepoimpl;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hypercube.evisa.approver.api.customsrepo.EmployeeDetailsCustomsRepo;
import com.hypercube.evisa.approver.api.domain.EmployeeDetails;
import com.hypercube.evisa.approver.api.model.EmployeeDetailsSearchDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class EmployeeDetailsCustomsRepoImpl implements EmployeeDetailsCustomsRepo {

	/**
	 * Entity manager for the persistence context
	 */
	@PersistenceContext
	EntityManager entityManager;

	/**
	 * 
	 */
	@Override
	public Page<EmployeeDetails> searchEmployeeDetails(EmployeeDetailsSearchDTO employeeDetailsSearchDTO) {
		log.info("EmployeeDetailsCustomsRepoImpl-searchEmployeeDetails");

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EmployeeDetails> cq = getCreateQueryForSystem(cb);
		Root<EmployeeDetails> root = getRootForEmployeeDetails(cq);

		projectionForEmployeeDetails(cq, cb, root);

		List<Predicate> predicates = new ArrayList<>();

		TypedQuery<EmployeeDetails> q = createPredicatesForEmployeeDetails(predicates, employeeDetailsSearchDTO, root,
				cb, cq);
		int totalRecords = getEmployeeDetailsResultSize(getEmployeeDetailsResultList(q));

		Pageable sortedByIdDesc = getPageableForEmployeeDetails(employeeDetailsSearchDTO, q, Sort.by("username"));

		/* return the EmployeeDetails results DTO */
		return new PageImpl<>(getEmployeeDetailsResultList(q), sortedByIdDesc, totalRecords);
	}

	/**
	 * @param EmployeeDetailsResultList
	 * @return
	 */
	private int getEmployeeDetailsResultSize(List<EmployeeDetails> masterCodeDetailsResultList) {
		log.info("EmployeeDetailsCustomsRepoImpl::getEmployeeDetailsResultSize");
		/*
		 * return masterCodeDetailsCustomsRepoImpl:: getEmployeeDetailsResultSize
		 */
		return masterCodeDetailsResultList.size();
	}

	/**
	 * @param q
	 * @return
	 */
	private List<EmployeeDetails> getEmployeeDetailsResultList(TypedQuery<EmployeeDetails> q) {
		log.info("EmployeeDetailsCustomsRepoImpl::getEmployeeDetailsResultList");
		/*
		 * return EmployeeDetailsCustomsRepoImpl:: getEmployeeDetailsResultList
		 */
		return q.getResultList();
	}

	/**
	 * @param searchDTO
	 * @param q
	 * @param sort
	 * @return
	 */
	private Pageable getPageableForEmployeeDetails(EmployeeDetailsSearchDTO searchDTO, TypedQuery<EmployeeDetails> q,
			Sort sort) {
		log.info("EmployeeDetailsCustomsRepoImpl::getPageableForEmployeeDetails");
		if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
			q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
			q.setMaxResults(searchDTO.getPageSize());
		}
		/*
		 * return EmployeeDetailsCustomsRepoImpl:: getPageableForEmployeeDetails
		 */
		return PageRequest.of(searchDTO.getPageNumber() - 1, searchDTO.getPageSize(), sort.ascending());
	}

	/**
	 * @param predicates
	 * @param searchDTO
	 * @param root
	 * @param cb
	 * @param cq
	 * @return
	 */
	private TypedQuery<EmployeeDetails> createPredicatesForEmployeeDetails(List<Predicate> predicates,
			EmployeeDetailsSearchDTO searchDTO, Root<EmployeeDetails> root, CriteriaBuilder cb,
			CriteriaQuery<EmployeeDetails> cq) {
		log.info("EmployeeDetailsCustomsRepoImpl::createPredicatesForEmployeeDetails");

		if (checkMandatory(searchDTO.getUsername())) {
			predicates.add(cb.like(cb.lower(root.get("username")), "%" + searchDTO.getUsername().toLowerCase() + "%"));
		}

		if (checkMandatory(searchDTO.getFullName())) {
			predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + searchDTO.getFullName().toLowerCase() + "%"));
		}

		if (checkMandatory(searchDTO.getEmailId())) {
			predicates.add(cb.like(cb.lower(root.get("email")), "%" + searchDTO.getEmailId().toLowerCase() + "%"));
		}

		if (checkMandatory(searchDTO.getRole())) {
			predicates.add(cb.equal(cb.lower(root.get("role")), searchDTO.getRole().toLowerCase()));
		}

		if (checkMandatory(searchDTO.getLoggedUser())) {
			predicates.add(cb.equal(cb.lower(root.get("reportingTo")), searchDTO.getLoggedUser().toLowerCase()));
		}

		predicates.add(cb.not(root.get("username").in(Arrays.asList("sysadmin"))));

		cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("username")));
		/*
		 * return EmployeeDetailsCustomsRepoImpl:: createPredicatesForEmployeeDetails
		 */
		return entityManager.createQuery(cq);
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean checkMandatory(String value) {
		return value != null && !value.isEmpty();
	}

	/**
	 * @param cq
	 * @param cb
	 * @param root
	 */
	private void projectionForEmployeeDetails(CriteriaQuery<EmployeeDetails> cq, CriteriaBuilder cb,
			Root<EmployeeDetails> root) {
		log.info("EmployeeDetailsCustomsRepoImpl::projectionForEmployeeDetails");
		cq.select(cb.construct(EmployeeDetails.class, root.get("username"), root.get("fullName"), root.get("email"),
				root.get("role"), root.get("employementStatus")));
	}

	/**
	 * @param cq
	 * @return
	 */
	private Root<EmployeeDetails> getRootForEmployeeDetails(CriteriaQuery<EmployeeDetails> cq) {
		log.info("EmployeeDetailsCustomsRepoImpl::getRootForEmployeeDetails");
		/*
		 * return EmployeeDetailsCustomsRepoImpl::getRootForEmployeeDetails
		 */
		return cq.from(EmployeeDetails.class);
	}

	/**
	 * @param cb
	 * @return
	 */
	private CriteriaQuery<EmployeeDetails> getCreateQueryForSystem(CriteriaBuilder cb) {
		log.info("EmployeeDetailsCustomsRepoImpl::getCreateQueryForSystem");
		/* EmployeeDetailsCustomsRepoImpl::getCreateQueryForSystem */
		return cb.createQuery(EmployeeDetails.class);
	}

}
