package com.hypercube.evisa.common.api.customsrepoimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.customsrepo.ApplicationTrackerCustomsRepo;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.model.AgeStatisticsDTO;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.ApplicationSearchTrackerDTO;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ManagerDashboardRequestDTO;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
public class ApplicationTrackerCustomsRepoImpl implements ApplicationTrackerCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 
     */
    @Override
    public Page<ApplicationTracker> searchApplicationInTracker(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
        log.info("ApplicationTrackerCustomsRepoImpl-searchApplicationInTracker");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationTracker> cq = getCreateQueryForSystem(cb);
        Root<ApplicationTracker> root = getRootForApplicationTracker(cq);

        /* if needed implement the projects here */

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicationTracker> q = createPredicatesForApplicationTracker(predicates,
                applicationSearchTrackerDTO, root, cb, cq);
        int totalRecords = getApplicationTrackerResultSize(getApplicationTrackerResultList(q));

        Pageable sortedByIdDesc = getPageableForApplicationTracker(applicationSearchTrackerDTO, q,
                Sort.by("applicationNumber"));

        /* return the ApplicationTracker results DTO */
        return new PageImpl<>(getApplicationTrackerResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     *
     */
    @Override
    public Page<ApplicationTracker> searchApplicationsForArrivalDeparture(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
        log.info("ApplicationTrackerCustomsRepoImpl-searchApplicationsForArrival");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationTracker> cq = getCreateQueryForSystem(cb);
        Root<ApplicationTracker> root = getRootForApplicationTracker(cq);

        /* if needed implement the projects here */

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicationTracker> q = createPredicatesForApplicationArrivalDeparture(predicates,
                applicationSearchTrackerDTO, root, cb, cq);
        int totalRecords = getApplicationTrackerResultSize(getApplicationTrackerResultList(q));

        Pageable sortedByIdDesc = getPageableForApplicationTracker(applicationSearchTrackerDTO, q,
                Sort.by("applicationNumber"));

        /* return the ApplicationTracker results DTO */
        return new PageImpl<>(getApplicationTrackerResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param applicationTrackerResultList
     * @return
     */
    private int getApplicationTrackerResultSize(List<ApplicationTracker> applicationTrackerResultList) {
        log.info("ApplicationTrackerCustomsRepoImpl::getApplicationTrackerResultSize");
        /*
         * return ApplicationTrackerCustomsRepoImpl:: getApplicationTrackerResultSize
         */
        return applicationTrackerResultList.size();
    }

    /**
     * @param q
     * @return
     */
    private List<ApplicationTracker> getApplicationTrackerResultList(TypedQuery<ApplicationTracker> q) {
        log.info("ApplicationTrackerCustomsRepoImpl::getApplicationTrackerResultList");
        /*
         * return ApplicationTrackerCustomsRepoImpl:: getApplicationTrackerResultList
         */
        return q.getResultList();
    }

    /**
     * @param searchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForApplicationTracker(ApplicationSearchTrackerDTO searchDTO,
            TypedQuery<ApplicationTracker> q, Sort sort) {
        log.info("ApplicationTrackerCustomsRepoImpl::getPageableForApplicationTracker");
        if (searchDTO.getPageNumber() > 0 && searchDTO.getPageSize() > 0) {
            q.setFirstResult((searchDTO.getPageNumber() - 1) * searchDTO.getPageSize());
            q.setMaxResults(searchDTO.getPageSize());
        }
        /*
         * return ApplicationTrackerCustomsRepoImpl:: getPageableForApplicationTracker
         */
        return PageRequest.of(searchDTO.getPageNumber() - 1, searchDTO.getPageSize(), sort.descending());
    }

    /**
     * @param predicates
     * @param searchDTO
     * @param root
     * @param cb
     * @param cq
     * @return
     */
    private TypedQuery<ApplicationTracker> createPredicatesForApplicationTracker(List<Predicate> predicates,
            ApplicationSearchTrackerDTO searchDTO, Root<ApplicationTracker> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicationTracker> cq) {
        log.info("ApplicationTrackerCustomsRepoImpl::createPredicatesForApplicationTracker");

        if (checkMandatory(searchDTO.getApplicationNumber())) {
            predicates.add(cb.like(cb.lower(root.get("applicationNumber")),
                    "%" + searchDTO.getApplicationNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getPassportNo())) {
            predicates.add(
                    cb.like(cb.lower(root.get("passportNumber")), "%" + searchDTO.getPassportNo().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getFileNumber())) {
            predicates.add(
                    cb.like(cb.lower(root.get("fileNumber")), "%" + searchDTO.getFileNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getGivenName())) {
            predicates
                    .add(cb.like(cb.lower(root.get("givenName")), "%" + searchDTO.getGivenName().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getMiddleName())) {
            predicates
                    .add(cb.like(cb.lower(root.get("middleName")), "%" + searchDTO.getMiddleName().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getSurname())) {
            predicates
                    .add(cb.like(cb.lower(root.get("surname")), "%" + searchDTO.getSurname().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getEmailAddress())) {
            predicates.add(
                    cb.like(cb.lower(root.get("emailAddress")), "%" + searchDTO.getEmailAddress().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getLoggedUser())) {
            if (checkMandatory(searchDTO.getRole()) && "DM".equals(searchDTO.getRole())) {
                predicates.add(cb.equal(cb.lower(root.get("assignedTo")), searchDTO.getLoggedUser().toLowerCase()));
            } else {
                predicates.add(cb.equal(cb.lower(root.get("username")), searchDTO.getLoggedUser().toLowerCase()));
             }
         } else {
            if (checkMandatory(searchDTO.getDocStatus())) {
                predicates.add(cb.equal(cb.lower(root.get("documentStatus")), searchDTO.getDocStatus().toLowerCase()));
            } else {
                predicates.add(cb.not(root.get("documentStatus").in(Arrays.asList("PP"))));
            }
        }
        
        if (checkMandatory(searchDTO.getArrDepIndicator())) {
       	 predicates.add(cb.equal(cb.lower(root.get("arrDepIndicator")), searchDTO.getArrDepIndicator().toLowerCase()));
        }
 
        if (checkMandatory(searchDTO.getDocStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("documentStatus")), searchDTO.getDocStatus().toLowerCase()));
        }

        if (checkMandatory(searchDTO.getVisaStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("visaStatus")), searchDTO.getVisaStatus().toLowerCase()));
        }

        if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            predicates.add(cb.between(root.get("submittedDate"), searchDTO.getStartDate(), searchDTO.getEndDate()));
        }

        if (checkMandatory(searchDTO.getExpressVisa())) {
            predicates.add(cb.equal(root.get("isExpressVisa"), "Y".equals(searchDTO.getExpressVisa()) ? true : false));
        }

        if (checkMandatory(searchDTO.getApplicantType())) {
            predicates.add(cb.equal(root.get("applicantType"), searchDTO.getApplicantType()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));
        /*
         * return ApplicationTrackerCustomsRepoImpl:: createPredicatesForApplicationFile
         */
        return entityManager.createQuery(cq);
    }

    /**
     * @param predicates
     * @param searchDTO
     * @param root
     * @param cb
     * @param cq
     * @return
     */
    private TypedQuery<ApplicationTracker> createPredicatesForApplicationArrivalDeparture(List<Predicate> predicates,
            ApplicationSearchTrackerDTO searchDTO, Root<ApplicationTracker> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicationTracker> cq) {
        log.info("ApplicationTrackerCustomsRepoImpl::createPredicatesForApplicationTracker");

        if (checkMandatory(searchDTO.getApplicationNumber())) {
            predicates.add(cb.like(cb.lower(root.get("applicationNumber")),
                    "%" + searchDTO.getApplicationNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getPassportNo())) {
            predicates.add(
                    cb.like(cb.lower(root.get("passportNumber")), "%" + searchDTO.getPassportNo().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getFileNumber())) {
            predicates.add(
                    cb.like(cb.lower(root.get("fileNumber")), "%" + searchDTO.getFileNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getGivenName())) {
            predicates
                    .add(cb.like(cb.lower(root.get("givenName")), "%" + searchDTO.getGivenName().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getMiddleName())) {
            predicates
                    .add(cb.like(cb.lower(root.get("middleName")), "%" + searchDTO.getMiddleName().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getSurname())) {
            predicates
                    .add(cb.like(cb.lower(root.get("surname")), "%" + searchDTO.getSurname().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getExpressVisa())) {
            predicates.add(cb.equal(root.get("isExpressVisa"), "Y".equals(searchDTO.getExpressVisa()) ? true : false));
        }

        if (checkMandatory(searchDTO.getEmailAddress())) {
            predicates.add(
                    cb.like(cb.lower(root.get("emailAddress")), "%" + searchDTO.getEmailAddress().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getApplicantType())) {
            predicates.add(cb.equal(root.get("applicantType"), searchDTO.getApplicantType()));
        }

        predicates.add(root.get("visaStatus").in(Arrays.asList("APR")));

        if ("A".equals(searchDTO.getOprType())) {
            predicates.add(root.get("arrDepIndicator").in(Arrays.asList("PA", "AV")));
        } else {
            predicates.add(root.get("arrDepIndicator").in(Arrays.asList("PD", "DV")));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));
        /*
         * return ApplicationTrackerCustomsRepoImpl:: createPredicatesForApplicationFile
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
     * @return
     */
    private Root<ApplicationTracker> getRootForApplicationTracker(CriteriaQuery<ApplicationTracker> cq) {
        log.info("ApplicationTrackerCustomsRepoImpl::getRootForApplicationTracker");
        /*
         * return ApplicationTrackerCustomsRepoImpl::getRootForApplicationTracker
         */
        return cq.from(ApplicationTracker.class);
    }

    /**
     * @param cb
     * @return
     */
    private CriteriaQuery<ApplicationTracker> getCreateQueryForSystem(CriteriaBuilder cb) {
        log.info("ApplicationTrackerCustomsRepoImpl::getCreateQueryForSystem");
        /* ApplicationTrackerCustomsRepoImpl::getCreateQueryForSystem */
        return cb.createQuery(ApplicationTracker.class);
    }

    /**
     *
     */
    @Override
    public Page<ApplicationTracker> searchApplicationsForBCO(List<String> applicationNumberList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationTrackerCustomsRepoImpl-searchApplicationInTracker");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationTracker> cq = getCreateQueryForSystem(cb);
        Root<ApplicationTracker> root = getRootForApplicationTracker(cq);

        /* if needed implement the projects here */
        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicationTracker> q = createPredicatesForBCOApplications(predicates, applicationNumberList, root,
                cb, cq);
        int totalRecords = getApplicationTrackerResultSize(getApplicationTrackerResultList(q));

        Pageable sortedByIdDesc = getPageableForBCOApplications(genericSearchDTO, q, Sort.by("applicationNumber"));

        /* return the ApplicationTracker results DTO */
        return new PageImpl<>(getApplicationTrackerResultList(q), sortedByIdDesc, totalRecords);
    }

    /**
     * @param genericSearchDTO
     * @param q
     * @param sort
     * @return
     */
    private Pageable getPageableForBCOApplications(GenericSearchDTO genericSearchDTO, TypedQuery<ApplicationTracker> q,
            Sort sort) {
        log.info("ApplicationTrackerCustomsRepoImpl::getPageableForBCOApplications");
        if (genericSearchDTO.getPageNumber() > 0 && genericSearchDTO.getPageSize() > 0) {
            q.setFirstResult((genericSearchDTO.getPageNumber() - 1) * genericSearchDTO.getPageSize());
            q.setMaxResults(genericSearchDTO.getPageSize());
        }
        /*
         * return ApplicationTrackerCustomsRepoImpl:: getPageableForBCOApplications
         */
        return PageRequest.of(genericSearchDTO.getPageNumber() - 1, genericSearchDTO.getPageSize(), sort.descending());
    }

    /**
     * @param predicates
     * @param applicationNumberList
     * @param genericSearchDTO
     * @param root
     * @param cb
     * @param cq
     * @return
     */
    private TypedQuery<ApplicationTracker> createPredicatesForBCOApplications(List<Predicate> predicates,
            List<String> applicationNumberList, Root<ApplicationTracker> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicationTracker> cq) {
        log.info("ApplicationTrackerCustomsRepoImpl::createPredicatesForApplicationTracker");

        predicates.add(root.get("applicationNumber").in(applicationNumberList));

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("applicationNumber")));
        /*
         * return ApplicationTrackerCustomsRepoImpl:: createPredicatesForApplicationFile
         */
        return entityManager.createQuery(cq);
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public AgentTrackerDTO immigrationOfficerCount(ManagerDashboardRequestDTO managerDashboardDTO) {
        log.info("ApplicationTrackerCustomsRepoImpl::immigrationOfficerCount");

        /* get the total agents for logged user */
        Query query = getListOfEmployeeReporting(managerDashboardDTO.getLoggeduser(), CommonsConstants.DECISION_MAKER);

        GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(managerDashboardDTO.getPeriod());

        AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
        agentTrackerDTO.setPending(getImmigrationOfficerCount(query.getResultList(), genericSearchDTO.getStartDate(),
                genericSearchDTO.getEndDate(), CommonsConstants.PENDING, managerDashboardDTO.getCountryList()));
        agentTrackerDTO.setClosed(getImmigrationOfficerCount(query.getResultList(), genericSearchDTO.getStartDate(),
                genericSearchDTO.getEndDate(), CommonsConstants.CLOSED, managerDashboardDTO.getCountryList()));
        /* BigDecimal avgValue = getImmigrationOfficerAverage(query.getResultList(), genericSearchDTO.getStartDate(),
                genericSearchDTO.getEndDate(), managerDashboardDTO.getCountryList()); */
        String avgValue = getImmigrationOfficerAverage(query.getResultList(), genericSearchDTO.getStartDate(),
        genericSearchDTO.getEndDate(), managerDashboardDTO.getCountryList());
        Long pendingAllocationCount = getPendingAllocationApplications(genericSearchDTO.getStartDate(),
                genericSearchDTO.getEndDate(), managerDashboardDTO.getCountryList());
        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getPending() + agentTrackerDTO.getClosed());

        /* if (avgValue != null) {
            agentTrackerDTO.setAverage(avgValue.setScale(2, BigDecimal.ROUND_DOWN));
        } else {
            agentTrackerDTO.setAverage(avgValue);
        } */
        agentTrackerDTO.setTotalAverage(avgValue);
        agentTrackerDTO.setArrival(pendingAllocationCount);
        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getPending() + agentTrackerDTO.getClosed());

        return agentTrackerDTO;
    }

    /**
     * @param startDate
     * @param endDate
     * @param countryStats
     * @return
     */
    private Long getPendingAllocationApplications(Date startDate, Date endDate, List<String> countryStats) {
        log.info("ApplicationTrackerCustomsRepoImpl-getPendingAllocationApplications");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(documentStatus) FROM ApplicationTracker ");
        strb.append("WHERE ");
        strb.append("documentStatus = 'SUB' AND (allocatedDate >= :startDate AND allocatedDate <= :endDate) ");
        if (countryStats != null && !countryStats.isEmpty()) {
            strb.append("AND (nationality IN (:countryList)) ");
        }

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        if (countryStats != null && !countryStats.isEmpty()) {
            q.setParameter("countryList", countryStats);
        }

        return (Long) q.getResultList().get(0);
    }

    /**
     * @param agentUsernameList
     * @param startDate
     * @param endDate
     * @param oprType
     * @param countryStats
     * @return
     */
    private Long getImmigrationOfficerCount(List<String> agentUsernameList, Date startDate, Date endDate,
            String oprType, List<String> countryStats) {
        log.info("ApplicationTrackerCustomsRepoImpl-getImmigrationOfficerCount");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(documentStatus) FROM ApplicationTracker ");
        strb.append("WHERE ");
        strb.append("assignedTo IN (:agentUsernameList) ");
        strb.append("AND documentStatus = :docStatus ");
        if (oprType.equals(CommonsConstants.PENDING)) {
            strb.append("AND (allocatedDate >= :startDate AND allocatedDate <= :endDate) ");
        } else if (oprType.equals(CommonsConstants.CLOSED)) {
            strb.append("AND (closedDate >= :startDate AND closedDate <= :endDate) ");
        }
        if (countryStats != null && !countryStats.isEmpty()) {
            strb.append("AND (nationality IN (:countryList)) ");
        }

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);

        if (oprType.equals(CommonsConstants.PENDING)) {
            q.setParameter("docStatus", CommonsConstants.PENDING);
        } else if (oprType.equals(CommonsConstants.CLOSED)) {
            q.setParameter("docStatus", CommonsConstants.CLOSED);
        }

		q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        if (countryStats != null && !countryStats.isEmpty()) {
            q.setParameter("countryList", countryStats);
        }

        @SuppressWarnings("unchecked")
        List<Long> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty() && resultList.get(0) != null) {
            return Long.valueOf(resultList.get(0));
        }
        return 0L;
    }

    /**
     * @param agentUsernameList
     * @param startDate
     * @param endDate
     * @param countryStats
     * @return
     */
    private String getImmigrationOfficerAverage(List<String> agentUsernameList, Date startDate, Date endDate,
            List<String> countryStats) {
        log.info("ApplicationTrackerCustomsRepoImpl-getImmigrationOfficerAverage");

        /* StringBuilder strb = new StringBuilder();
        strb.append("SELECT ");
        strb.append("AVG( ");
        strb.append("(DATE_PART('day', closedDate - submittedDate) * 24 + ");
        strb.append("DATE_PART('hour', closedDate - submittedDate)");
        strb.append(")) ");
        strb.append("FROM ApplicationTracker ");
        strb.append("WHERE documentStatus = :docStatus ");
        strb.append("AND (closedDate >= :startDate AND closedDate <= :endDate) ");
        strb.append("AND assignedTo in (:agentUsernameList) "); */

        // process report db checking
        StringBuilder strbq1 = new StringBuilder();
        strbq1.append("UPDATE ");
        strbq1.append("ApplicationHeader ");
        strbq1.append("SET ");
        strbq1.append("closedDateForAvgStat = NOW() ");
        strbq1.append("WHERE closedDate is null ");

        entityManager.createQuery(strbq1.toString()).executeUpdate();

        StringBuilder strbq2 = new StringBuilder();
        strbq2.append("UPDATE ");
        strbq2.append("ApplicationHeader ");
        strbq2.append("SET ");
        strbq2.append("closedDateForAvgStat = closedDate ");
        strbq2.append("WHERE closedDate is not null ");

        entityManager.createQuery(strbq2.toString()).executeUpdate();

        /* StringBuilder strb = new StringBuilder();
        strb.append("SELECT ");
        strb.append("AVG( ");
        strb.append("(DATE_PART('day', closedDateForAvgStat - submittedDate) * 3600 + ");
        strb.append("DATE_PART('hour', closedDateForAvgStat - submittedDate) * 60 + DATE_PART('minutes', closedDateForAvgStat - submittedDate)");
        strb.append(")) ");
        strb.append("FROM ApplicationTracker ");
        strb.append("WHERE documentStatus = :docStatus ");
        strb.append("AND (closedDateForAvgStat >= :startDate AND closedDateForAvgStat <= :endDate) ");
        strb.append("AND assignedTo in (:agentUsernameList) "); */

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT ");
        strb.append("TO_CHAR(SUM( ");
        strb.append("closedDate - submittedDate");
        strb.append(" )");
        strb.append("/ ");
        strb.append("COUNT(*), 'HH24:MI:SS') ");
        strb.append("FROM ApplicationTracker ");
        strb.append("WHERE closedDate is not null ");
        strb.append("AND documentStatus = :docStatus ");
        strb.append("AND (closedDate >= :startDate AND closedDate <= :endDate) ");
        strb.append("AND assignedTo in (:agentUsernameList) ");

        if (countryStats != null && !countryStats.isEmpty()) {
            strb.append("AND (nationality IN (:countryList)) ");
        }

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("docStatus", CommonsConstants.CLOSED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        if (countryStats != null && !countryStats.isEmpty()) {
            q.setParameter("countryList", countryStats);
        }

        @SuppressWarnings("unchecked")
        List<String> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty() && resultList.get(0) != null) {
            // return BigDecimal.valueOf(resultList.get(0));
            return resultList.get(0);
        }
        return null;
    }

    /**
     * @param loggeduser
     * @param role
     * @return
     */
    public Query getListOfEmployeeReporting(String loggeduser, String role) {
        log.info("ApplicationTrackerCustomsRepoImpl-getListOfEmployeeReporting");

        StringBuilder strb = new StringBuilder();
        strb.append(
                "SELECT username FROM EmployeeDetails WHERE reportingTo =:loggeduser AND role =:role ORDER BY username ASC");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("loggeduser", loggeduser);
        q.setParameter("role", role);

        return q;
    }

    /**
     *
     */
    @Override
    public List<CountDTO> travelPurposeStatistics(List<String> countryStats, Date startDate, Date endDate) {
        log.info("ApplicationTrackerCustomsRepoImpl-travelPurposeStatistics");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT travelPurpose, Count(*) FROM ApplicationTracker ");
        strb.append("WHERE ");
        strb.append("(closedDate >= :startDate AND closedDate <= :endDate) ");
        if (countryStats != null && !countryStats.isEmpty()) {
            strb.append("AND (nationality IN (:countryList)) ");
        }
        strb.append("GROUP BY travelPurpose");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        if (countryStats != null && !countryStats.isEmpty()) {
            q.setParameter("countryList", countryStats);
        }

        List<CountDTO> countDTOs = null;

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            countDTOs = new ArrayList<CountDTO>();

            for (Object[] obj : resultList) {
                countDTOs.add(new CountDTO((String) obj[0], (Long) obj[1]));
            }
        }

        return countDTOs;
    }

    /**
     *
     */
    @Override
    public AgeStatisticsDTO ageStatistics(List<String> countryStats, Date startDate, Date endDate) {
        log.info("ApplicationTrackerCustomsRepoImpl-ageStatistics");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT sum(case when date_part('year', age(dob))<=20 then 1 end), ");
        strb.append("sum(case when date_part('year', age(dob))>=21 and date_part('year', age(dob))<40 then 1 end), ");
        strb.append("sum(case when date_part('year', age(dob))>=41 and date_part('year', age(dob))<60 then 1 end), ");
        strb.append("sum(case when date_part('year', age(dob))>60  then 1 end) FROM ApplicationTracker ");
        strb.append("WHERE ");
        strb.append("(closedDate >= :startDate AND closedDate <= :endDate) ");
        if (countryStats != null && !countryStats.isEmpty()) {
            strb.append("AND (nationality IN (:countryList)) ");
        }

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        if (countryStats != null && !countryStats.isEmpty()) {
            q.setParameter("countryList", countryStats);
        }

        List<AgeStatisticsDTO> ageStatisticsDTOs = null;

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            ageStatisticsDTOs = new ArrayList<>();

            for (Object[] obj : resultList) {
                ageStatisticsDTOs.add(new AgeStatisticsDTO((Long) obj[0], (Long) obj[1], (Long) obj[2], (Long) obj[3]));
            }
        }

        return ageStatisticsDTOs != null ? ageStatisticsDTOs.get(0) : null;
    }
    
    /**
    *
    */
    @Override
   public AgeStatisticsDTO ageStatisticsAll(Date startDate, Date endDate) {
       log.info("ApplicationTrackerCustomsRepoImpl-ageStatistics");

       StringBuilder strb = new StringBuilder();
       strb.append("SELECT sum(case when date_part('year', age(dob))<=20 then 1 end), ");
       strb.append("sum(case when date_part('year', age(dob))>=21 and date_part('year', age(dob))<40 then 1 end), ");
       strb.append("sum(case when date_part('year', age(dob))>=41 and date_part('year', age(dob))<60 then 1 end), ");
       strb.append("sum(case when date_part('year', age(dob))>60  then 1 end) FROM ApplicationTracker ");
       strb.append("WHERE ");
       strb.append("(closedDate >= :startDate AND closedDate <= :endDate) ");
      
       Query q = entityManager.createQuery(strb.toString());
       q.setParameter("startDate", startDate);
       q.setParameter("endDate", endDate);
     
       List<AgeStatisticsDTO> ageStatisticsDTOs = null;

       @SuppressWarnings("unchecked")
       List<Object[]> resultList = q.getResultList();

       if (resultList != null && !resultList.isEmpty()) {
           ageStatisticsDTOs = new ArrayList<>();

           for (Object[] obj : resultList) {
               ageStatisticsDTOs.add(new AgeStatisticsDTO((Long) obj[0], (Long) obj[1], (Long) obj[2], (Long) obj[3]));
           }
       }

       return ageStatisticsDTOs != null ? ageStatisticsDTOs.get(0) : null;
   }

    @Override
    public List<CountDTO> visaApplicationDashboard(Date subsDate, List<String> countryStats) {
        log.info("ApplicationTrackerCustomsRepoImpl-visaApplicationDashboard");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT TO_CHAR(submittedDate, 'MM'), visaStatus, COUNT(*) FROM ApplicationTracker ");
        strb.append("WHERE ");
        strb.append("visaStatus IN('APR', 'REJ', 'UP') AND (submittedDate >= :subsDate) ");
        if (countryStats != null && !countryStats.isEmpty()) {
            strb.append("AND (nationality IN (:countryList)) ");
        }
        strb.append("GROUP BY visaStatus, TO_CHAR(submittedDate, 'MM') ORDER BY TO_CHAR(submittedDate, 'MM') DESC ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("subsDate", subsDate);
        if (countryStats != null && !countryStats.isEmpty()) {
            q.setParameter("countryList", countryStats);
        }

        List<CountDTO> countDTOs = null;

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            countDTOs = new ArrayList<CountDTO>();

            for (Object[] obj : resultList) {
                countDTOs.add(new CountDTO((String) obj[0], (String) obj[1], (Long) obj[2]));
            }
        }

        return countDTOs;
    }

    /**
     *
     */
    @Override
    public Page<ApplicationTracker> pendingFileNumberForProcessing(
            ApplicationSearchTrackerDTO applicationSearchTrackerDTO) {
        log.info("ApplicationTrackerCustomsRepoImpl-pendingFileNumberForProcessing");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationTracker> cq = getCreateQueryForSystem(cb);

        Root<ApplicationTracker> root = getRootForApplicationTracker(cq);

        /* if needed implement the projects here */
        projectionForPendingApplications(cq, cb, root);

        List<Predicate> predicates = new ArrayList<>();

        TypedQuery<ApplicationTracker> q = createPredicatesForPendingApplications(predicates,
                applicationSearchTrackerDTO, root, cb, cq);
        int totalRecords = getApplicationTrackerResultSize(getApplicationTrackerResultList(q));

        Pageable sortedByIdDesc = getPageableForApplicationTracker(applicationSearchTrackerDTO, q,
                Sort.by("applicationNumber"));

        /* return the ApplicationTracker results DTO */
        return new PageImpl<>(getApplicationTrackerResultList(q), sortedByIdDesc, totalRecords);

    }

    /**
     * @param cq
     * @param cb
     * @param root
     */
    private void projectionForPendingApplications(CriteriaQuery<ApplicationTracker> cq, CriteriaBuilder cb,
            Root<ApplicationTracker> root) {
        log.info("ApplicationFileCustomsRepoImpl::projectionForApplicationPersonalDetails");
        cq.select(cb.construct(ApplicationTracker.class, root.get("fileNumber"), root.get("username"),
                root.get("isExpressVisa"), root.get("documentStatus"))).distinct(true);
    }

    /**
     * @param predicates
     * @param searchDTO
     * @param root
     * @param cb
     * @param cq
     * @return
     */
    private TypedQuery<ApplicationTracker> createPredicatesForPendingApplications(List<Predicate> predicates,
            ApplicationSearchTrackerDTO searchDTO, Root<ApplicationTracker> root, CriteriaBuilder cb,
            CriteriaQuery<ApplicationTracker> cq) {
        log.info("ApplicationTrackerCustomsRepoImpl::createPredicatesForApplicationTracker");

        if (checkMandatory(searchDTO.getFileNumber())) {
            predicates.add(
                    cb.like(cb.lower(root.get("fileNumber")), "%" + searchDTO.getFileNumber().toLowerCase() + "%"));
        }

        if (checkMandatory(searchDTO.getExpressVisa())) {
            predicates.add(cb.equal(root.get("isExpressVisa"), "Y".equals(searchDTO.getExpressVisa()) ? true : false));
        }

        if (checkMandatory(searchDTO.getApplicantType())) {
            predicates.add(cb.equal(root.get("applicantType"), searchDTO.getApplicantType()));
        }

        if (checkMandatory(searchDTO.getLoggedUser())) {
            predicates.add(cb.equal(cb.lower(root.get("assignedTo")), searchDTO.getLoggedUser().toLowerCase()));
        }

        if (checkMandatory(searchDTO.getDocStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("documentStatus")), searchDTO.getDocStatus().toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[] {})).orderBy(cb.desc(root.get("fileNumber")));
        /*
         * return ApplicationTrackerCustomsRepoImpl:: createPredicatesForApplicationFile
         */
        return entityManager.createQuery(cq);
    }

}
