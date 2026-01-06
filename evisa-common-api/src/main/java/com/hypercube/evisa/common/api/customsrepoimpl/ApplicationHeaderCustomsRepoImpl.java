package com.hypercube.evisa.common.api.customsrepoimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.customsrepo.ApplicationHeaderCustomsRepo;
import com.hypercube.evisa.common.api.model.AgentTrackerDTO;
import com.hypercube.evisa.common.api.model.CountDTO;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import com.hypercube.evisa.common.api.model.ReportsSearchDTO;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Slf4j
public class ApplicationHeaderCustomsRepoImpl implements ApplicationHeaderCustomsRepo {

    /**
     * Entity manager for the persistence context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<AgentTrackerDTO> agentTrackerDetails(GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-agentTrackerDetails");

        int totalRecords = 0;
        List<AgentTrackerDTO> agentTrackerDTOs = new ArrayList<>();

        genericSearchDTO = getPeriodMapping(genericSearchDTO);

        if (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                && CommonsConstants.DECISION_MAKER.equals(genericSearchDTO.getOprType())) {

            /* get the decision making agents for the logged immigration officer */
            Query query = getListOfEmployeeReporting(genericSearchDTO.getLoggedUser(), genericSearchDTO.getOprType());

            /* total records of agents reporting to immigration officer */
            totalRecords = query.getResultList().size();
            query.setFirstResult((genericSearchDTO.getPageNumber() - 1) * genericSearchDTO.getPageSize())
                    .setMaxResults(genericSearchDTO.getPageSize());

            List<String> agentUsernameList = query.getResultList();

            /* get tracker list only if agent username list is not null/not empty */
            if (agentUsernameList != null && !agentUsernameList.isEmpty()) {
                List<CountDTO> countDTOs = getDecisionMakerAgentTrackerDetailsWithPeriod(agentUsernameList,
                        genericSearchDTO);

                for (String agentUsername : agentUsernameList) {
                    AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
                    agentTrackerDTO.setUsername(agentUsername);

                    for (CountDTO countDTO : countDTOs) {
                        if (agentUsername.equals(countDTO.getAgentUsername())
                                && CommonsConstants.PENDING.equals(countDTO.getStatusCode())) {
                            agentTrackerDTO.setPending(countDTO.getCount());
                            agentTrackerDTO
                                    .setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                        } else if (agentUsername.equals(countDTO.getAgentUsername())
                                && CommonsConstants.CLOSED.equals(countDTO.getStatusCode())) {
                            agentTrackerDTO.setClosed(countDTO.getCount());
                            agentTrackerDTO
                                    .setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                        }
                    }
                    agentTrackerDTOs.add(agentTrackerDTO);
                }
            }
        } else if (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                && CommonsConstants.BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getOprType())) {
            /* get the border control officer agents for the logged immigration officer */
            Query query = getListOfBorderControlOfficersReporting(genericSearchDTO.getLoggedUser());

            /* total records of BCO agents reporting to immigration officer */
            totalRecords = query.getResultList().size();
            query.setFirstResult((genericSearchDTO.getPageNumber() - 1) * genericSearchDTO.getPageSize())
                    .setMaxResults(genericSearchDTO.getPageSize());

            agentTrackerDTOs = computeAgentTrackerForArrDepDtls(query.getResultList(), agentTrackerDTOs,
                    genericSearchDTO);

        } else if (CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getRole())
                || (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                        && CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getOprType()))) {

            /* get the SBCO agents for the logged immigration officer */
            /* get the BCO agents for the logged SBCO */
            Query query = getListOfEmployeeReporting(genericSearchDTO.getLoggedUser(), genericSearchDTO.getOprType());

            /* total records of agents reporting to immigration officer */
            totalRecords = query.getResultList().size();
            query.setFirstResult((genericSearchDTO.getPageNumber() - 1) * genericSearchDTO.getPageSize())
                    .setMaxResults(genericSearchDTO.getPageSize());

            agentTrackerDTOs = computeAgentTrackerForArrDepDtls(query.getResultList(), agentTrackerDTOs,
                    genericSearchDTO);
        }

        return new PageImpl<>(agentTrackerDTOs,
                PageRequest.of(genericSearchDTO.getPageNumber() - 1, genericSearchDTO.getPageSize()), totalRecords);
    }

    /**
     * @param agentUsernameList
     * @return
     */
    private List<CountDTO> getDecisionMakerAgentTrackerDetailsWithPeriod(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getDecisionMakerAgentTrackerDetailsWithPeriod");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT assignedTo, documentStatus, COUNT(documentStatus) FROM ApplicationHeader ");
        strb.append("WHERE allocatedDate >= :startDate AND allocatedDate <= :endDate ");
        strb.append("AND assignedTo in (:agentUsernameList) ");
        strb.append("GROUP BY ");
        strb.append("assignedTo, documentStatus ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        List<CountDTO> countDTOs = new ArrayList<>();
        for (Object[] obj : resultList) {
            countDTOs.add(new CountDTO(String.valueOf(obj[0]), String.valueOf(obj[1]),
                    Long.parseLong(String.valueOf(obj[2]))));
        }

        return countDTOs;
    }

    /**
     * @param genericSearchDTO
     * @return
     */
    private GenericSearchDTO getPeriodMapping(GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getPeriodMapping");

        if ("D".equals(genericSearchDTO.getPeriod())) {
            genericSearchDTO.setStartDate(genericSearchDTO.getStartDate());
            genericSearchDTO.setEndDate(genericSearchDTO.getEndDate());
        } else if ("T".equals(genericSearchDTO.getPeriod())) {
            genericSearchDTO
                    .setStartDate(CommonsUtil.setTimeToBeginningOfDay(CommonsUtil.getCalendarForNow()).getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("W".equals(genericSearchDTO.getPeriod())) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("M".equals(genericSearchDTO.getPeriod())) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("Y".equals(genericSearchDTO.getPeriod())) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        }
        return genericSearchDTO;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<AgentTrackerDTO> performanceReport(ReportsSearchDTO reportsSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-performanceReport");

        int totalRecords = 0;
        List<String> agentList = null;
        List<AgentTrackerDTO> agentTrackerDTOs = new ArrayList<>();
        /* get start date & end date based on period */
        GenericSearchDTO genericSearchDTO = getSearchDates(reportsSearchDTO);

        /* check the type of report (Summary or Individual) and get the list */
        if ("S".equals(reportsSearchDTO.getReportType())) {
            Query query = getAgentsListByOprTypeAndReportType(reportsSearchDTO);
            totalRecords = query.getResultList().size();
            query.setFirstResult((genericSearchDTO.getPageNumber() - 1) * genericSearchDTO.getPageSize())
                    .setMaxResults(genericSearchDTO.getPageSize());
            agentList = query.getResultList();
        } else if ("I".equals(reportsSearchDTO.getReportType())) {
            totalRecords = reportsSearchDTO.getAgentList().size();
            agentList = reportsSearchDTO.getAgentList();
        }

        agentTrackerDTOs = computePerformanceReport(agentList, genericSearchDTO, agentTrackerDTOs);

        return new PageImpl<>(agentTrackerDTOs,
                PageRequest.of(genericSearchDTO.getPageNumber() - 1, genericSearchDTO.getPageSize()), totalRecords);
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<AgentTrackerDTO> processReport(ReportsSearchDTO reportsSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-processReport");

        int totalRecords = 0;
        List<String> agentList = null;
        List<AgentTrackerDTO> agentTrackerDTOs = new ArrayList<>();
        /* get start date & end date based on period */
        GenericSearchDTO genericSearchDTO = getSearchDates(reportsSearchDTO);

        /* check the type of report (Summary or Individual) and get the list */
        if ("S".equals(reportsSearchDTO.getReportType())) {
            Query query = getAgentsListByOprTypeAndReportType(reportsSearchDTO);
            totalRecords = (query.getResultList() == null) ? 0 : query.getResultList().size();
            query.setFirstResult((genericSearchDTO.getPageNumber() - 1) * genericSearchDTO.getPageSize())
                    .setMaxResults(genericSearchDTO.getPageSize());
            agentList = query.getResultList();
        } else if ("I".equals(reportsSearchDTO.getReportType())) {
            totalRecords = reportsSearchDTO.getAgentList().size();
            agentList = reportsSearchDTO.getAgentList();
        }

        agentTrackerDTOs = computeProcessReport(agentList, genericSearchDTO, agentTrackerDTOs);

        return new PageImpl<>(agentTrackerDTOs,
                PageRequest.of(genericSearchDTO.getPageNumber() - 1, genericSearchDTO.getPageSize()), totalRecords);
    }

    /**
     * @param agentList
     * @param genericSearchDTO
     * @param agentTrackerDTOs
     * @return
     */
    private List<AgentTrackerDTO> computeProcessReport(List<String> agentList, GenericSearchDTO genericSearchDTO,
            List<AgentTrackerDTO> agentTrackerDTOs) {
        log.info("ApplicationHeaderCustomsRepoImpl-computeProcessReport");

        if (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                && CommonsConstants.DECISION_MAKER.equals(genericSearchDTO.getOprType())) {
            agentTrackerDTOs = computeDecisionMakerForProcessReport(agentList, genericSearchDTO, agentTrackerDTOs);

        }

        return agentTrackerDTOs;
    }

    /**
     * @param agentList
     * @param genericSearchDTO
     * @param agentTrackerDTOs
     * @return
     */
    private List<AgentTrackerDTO> computeDecisionMakerForProcessReport(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO, List<AgentTrackerDTO> agentTrackerDTOs) {
        log.info("ApplicationHeaderCustomsRepoImpl-computeDecisionMakerForProcessReport");

        /* get tracker list only if agent username list is not null/not empty */
        if (agentUsernameList != null && !agentUsernameList.isEmpty()) {
            agentTrackerDTOs = getDecisionMakerAgentProcessReport(agentUsernameList, genericSearchDTO);

        }
        return agentTrackerDTOs;
    }

    /**
     * @param agentUsernameList
     * @param genericSearchDTO
     * @return
     */
    private List<AgentTrackerDTO> getDecisionMakerAgentProcessReport(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getDecisionMakerAgentProcessReport");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT assignedTo, COUNT(allocatedDate), ");
        strb.append("AVG( ");
        strb.append("(DATE_PART('day', closedDate - submittedDate) * 24 + ");
        strb.append("DATE_PART('hour', closedDate - submittedDate)");
        strb.append(")) ");
        strb.append("FROM ApplicationHeader ");
        strb.append("WHERE allocatedDate >= :startDate AND allocatedDate <= :endDate ");
        strb.append("AND documentStatus = :docStatus ");
        strb.append("AND assignedTo in (:agentUsernameList) ");
        strb.append("GROUP BY ");
        strb.append("assignedTo ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());
        q.setParameter("docStatus", CommonsConstants.CLOSED);

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        List<AgentTrackerDTO> agentTrackerDTOs = new ArrayList<>();
        for (Object[] obj : resultList) {
            AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
            agentTrackerDTO.setUsername(String.valueOf(obj[0]));
            agentTrackerDTO.setTotalAllocated(Long.parseLong(String.valueOf(obj[1])));
            agentTrackerDTO.setAverage(new BigDecimal(String.valueOf(obj[2])));
            agentTrackerDTOs.add(agentTrackerDTO);
        }

        return agentTrackerDTOs;
    }

    /**
    *
    */
    @SuppressWarnings("unchecked")
    @Override
    public ReportsSearchDTO getAgentListBasedOnOprTypeAndRole(String loggeduser, String role, String oprtype) {
        log.info("ApplicationHeaderCustomsRepoImpl-getAgentListBasedOnOprTypeAndRole");

        List<String> agentsList = null;
        if (CommonsConstants.IMMIGRATION_MANAGER.equals(role) && CommonsConstants.DECISION_MAKER.equals(oprtype)) {
            agentsList = getListOfEmployeeReporting(loggeduser, oprtype).getResultList();

        } else if (CommonsConstants.IMMIGRATION_MANAGER.equals(role)
                && CommonsConstants.BORDER_CONTROL_OFFICER.equals(oprtype)) {
            agentsList = getListOfBorderControlOfficersReporting(loggeduser).getResultList();

        } else if (CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(role)
                || (CommonsConstants.IMMIGRATION_MANAGER.equals(role)
                        && CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(oprtype))) {
            agentsList = getListOfEmployeeReporting(loggeduser, oprtype).getResultList();

        }
        ReportsSearchDTO reportsSearchDTO = new ReportsSearchDTO();
        reportsSearchDTO.setAgentList(agentsList);
        return reportsSearchDTO;
    }

    /**
     * @param agentList
     * @param startDate
     * @param endDate
     * @param agentTrackerDTOs
     * @return
     */
    private List<AgentTrackerDTO> computePerformanceReport(List<String> agentList, GenericSearchDTO genericSearchDTO,
            List<AgentTrackerDTO> agentTrackerDTOs) {
        log.info("ApplicationHeaderCustomsRepoImpl-computePerformanceReport");

        if (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                && CommonsConstants.DECISION_MAKER.equals(genericSearchDTO.getOprType())) {
            agentTrackerDTOs = computeDecisionMakerForPerformanceReport(agentList, genericSearchDTO, agentTrackerDTOs);

        } else if (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                && CommonsConstants.BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getOprType())) {
            agentTrackerDTOs = computeAgentTrackerForPerformanceReport(agentList, genericSearchDTO, agentTrackerDTOs);

        } else if (CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getRole())
                || (CommonsConstants.IMMIGRATION_MANAGER.equals(genericSearchDTO.getRole())
                        && CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getOprType()))) {
            agentTrackerDTOs = computeAgentTrackerForPerformanceReport(agentList, genericSearchDTO, agentTrackerDTOs);

        }
        return agentTrackerDTOs;
    }

    /**
     * @param agentUsernameList
     * @param genericSearchDTO
     * @param agentTrackerDTOs
     * @return
     */
    private List<AgentTrackerDTO> computeDecisionMakerForPerformanceReport(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO, List<AgentTrackerDTO> agentTrackerDTOs) {
        log.info("ApplicationHeaderCustomsRepoImpl-computeDecisionMakerForSummaryReport");

        /* get tracker list only if agent username list is not null/not empty */
        if (agentUsernameList != null && !agentUsernameList.isEmpty()) {
            List<CountDTO> countDTOs = getDecisionMakerAgentForSummaryReport(agentUsernameList, genericSearchDTO);

            for (String agentUsername : agentUsernameList) {
                AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
                agentTrackerDTO.setUsername(agentUsername);

                for (CountDTO countDTO : countDTOs) {
                    if (agentUsername.equals(countDTO.getAgentUsername())
                            && CommonsConstants.PENDING.equals(countDTO.getStatusCode())) {
                        agentTrackerDTO.setPending(countDTO.getCount());
                        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                    } else if (agentUsername.equals(countDTO.getAgentUsername())
                            && CommonsConstants.CLOSED.equals(countDTO.getStatusCode())) {
                        agentTrackerDTO.setClosed(countDTO.getCount());
                        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                    }
                }
                agentTrackerDTOs.add(agentTrackerDTO);
            }
        }
        return agentTrackerDTOs;
    }

    /**
     * @param agentUsernameList
     * @param genericSearchDTO
     * @return
     */
    private List<CountDTO> getDecisionMakerAgentForSummaryReport(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getDecisionMakerAgentTrackerDetails");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT assignedTo, documentStatus, COUNT(documentStatus) FROM ApplicationHeader ");
        strb.append("WHERE allocatedDate >= :startDate AND allocatedDate <= :endDate ");
        strb.append("AND assignedTo in (:agentUsernameList)");
        strb.append("GROUP BY ");
        strb.append("assignedTo, documentStatus ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        List<CountDTO> countDTOs = new ArrayList<>();
        for (Object[] obj : resultList) {
            countDTOs.add(new CountDTO(String.valueOf(obj[0]), String.valueOf(obj[1]),
                    Long.parseLong(String.valueOf(obj[2]))));
        }

        return countDTOs;
    }

    /**
     * @param agentList
     * @param agentTrackerDTOs
     * @return
     */
    private List<AgentTrackerDTO> computeAgentTrackerForPerformanceReport(List<String> usernameList,
            GenericSearchDTO genericSearchDTO, List<AgentTrackerDTO> agentTrackerDTOs) {
        log.info("ApplicationHeaderCustomsRepoImpl-computeAgentTrackerForSummaryReport");

        /* get tracker list only if agent username list is not null/not empty */
        if (usernameList != null && !usernameList.isEmpty()) {
            List<CountDTO> countDTOs = getArrivalDepartureTrackerForSummaryReport(usernameList, genericSearchDTO);

            for (String agentUsername : usernameList) {
                AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
                agentTrackerDTO.setUsername(agentUsername);

                for (CountDTO countDTO : countDTOs) {
                    if (agentUsername.equals(countDTO.getAgentUsername())
                            && CommonsConstants.ARRIVAL.equals(countDTO.getStatusCode())) {
                        agentTrackerDTO.setArrival(countDTO.getCount());
                        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                    } else if (agentUsername.equals(countDTO.getAgentUsername())
                            && CommonsConstants.DEPARTURE.equals(countDTO.getStatusCode())) {
                        agentTrackerDTO.setDeparture(countDTO.getCount());
                        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                    }
                }
                agentTrackerDTOs.add(agentTrackerDTO);
            }
        }
        return agentTrackerDTOs;
    }

    /**
     * @param usernameList
     * @return
     */
    private List<CountDTO> getArrivalDepartureTrackerForSummaryReport(List<String> usernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getAgentTrackerByList");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT approver, oprType, COUNT(oprType) FROM ArrivalDepartureHistoryDetails ");
        strb.append("WHERE actionDate >= :startDate AND actionDate <= :endDate ");
        strb.append("AND approver in (:agentUsernameList) GROUP BY approver, oprType ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", usernameList);
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        List<CountDTO> countDTOs = new ArrayList<>();
        for (Object[] obj : resultList) {
            countDTOs.add(new CountDTO(String.valueOf(obj[0]), String.valueOf(obj[1]),
                    Long.parseLong(String.valueOf(obj[2]))));
        }

        return countDTOs;
    }

    /**
     * @param reportsSearchDTO
     * @return
     */
    private Query getAgentsListByOprTypeAndReportType(ReportsSearchDTO reportsSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getAgentsListByOprTypeAndReportType");

        if (CommonsConstants.IMMIGRATION_MANAGER.equals(reportsSearchDTO.getRole())
                && CommonsConstants.DECISION_MAKER.equals(reportsSearchDTO.getOprType())) {
            return getListOfEmployeeReporting(reportsSearchDTO.getLoggedUser(), reportsSearchDTO.getOprType());

        } else if (CommonsConstants.IMMIGRATION_MANAGER.equals(reportsSearchDTO.getRole())
                && CommonsConstants.BORDER_CONTROL_OFFICER.equals(reportsSearchDTO.getOprType())) {
            return getListOfBorderControlOfficersReporting(reportsSearchDTO.getLoggedUser());

        } else if (CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(reportsSearchDTO.getRole())
                || (CommonsConstants.IMMIGRATION_MANAGER.equals(reportsSearchDTO.getRole())
                        && CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(reportsSearchDTO.getOprType()))) {
            return getListOfEmployeeReporting(reportsSearchDTO.getLoggedUser(), reportsSearchDTO.getOprType());

        }

        return null;
    }

    /**
     * @param reportsSearchDTO
     * @return
     */
    private GenericSearchDTO getSearchDates(ReportsSearchDTO reportsSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getSearchDates");

        GenericSearchDTO genericSearchDTO = new GenericSearchDTO();
        if ("D".equals(reportsSearchDTO.getPeriod())) {
            genericSearchDTO.setStartDate(reportsSearchDTO.getStartDate());
            genericSearchDTO.setEndDate(reportsSearchDTO.getEndDate());
        } else if ("T".equals(reportsSearchDTO.getPeriod())) {
            genericSearchDTO
                    .setStartDate(CommonsUtil.setTimeToBeginningOfDay(CommonsUtil.getCalendarForNow()).getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("W".equals(reportsSearchDTO.getPeriod())) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("M".equals(reportsSearchDTO.getPeriod())) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("Y".equals(reportsSearchDTO.getPeriod())) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        }

        genericSearchDTO.setLoggedUser(reportsSearchDTO.getLoggedUser());
        genericSearchDTO.setOprType(reportsSearchDTO.getOprType());
        genericSearchDTO.setRole(reportsSearchDTO.getRole());
        genericSearchDTO.setPageNumber(reportsSearchDTO.getPageNumber());
        genericSearchDTO.setPageSize(reportsSearchDTO.getPageSize());

        return genericSearchDTO;
    }

    /**
     * @param usernameList
     * @param agentTrackerDTOs
     * @return
     */
    List<AgentTrackerDTO> computeAgentTrackerForArrDepDtls(List<String> usernameList,
            List<AgentTrackerDTO> agentTrackerDTOs, GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-computeAgentTrackerForArrDepDtls");

        /* get tracker list only if agent username list is not null/not empty */
        if (usernameList != null && !usernameList.isEmpty()) {
            List<CountDTO> countDTOs = getArrivalDepartureTrackerDetailsWithPeriod(usernameList, genericSearchDTO);

            for (String agentUsername : usernameList) {
                AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
                agentTrackerDTO.setUsername(agentUsername);

                for (CountDTO countDTO : countDTOs) {
                    if (agentUsername.equals(countDTO.getAgentUsername())
                            && CommonsConstants.ARRIVAL.equals(countDTO.getStatusCode())) {
                        agentTrackerDTO.setArrival(countDTO.getCount());
                        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                    } else if (agentUsername.equals(countDTO.getAgentUsername())
                            && CommonsConstants.DEPARTURE.equals(countDTO.getStatusCode())) {
                        agentTrackerDTO.setDeparture(countDTO.getCount());
                        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                    }
                }
                agentTrackerDTOs.add(agentTrackerDTO);
            }
        }
        return agentTrackerDTOs;
    }

    /**
     * @param agentUsernameList
     * @return
     */
    private List<CountDTO> getArrivalDepartureTrackerDetailsWithPeriod(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ApplicationHeaderCustomsRepoImpl-getAgentTrackerByList");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT approver, oprType, COUNT(oprType) FROM ArrivalDepartureHistoryDetails ");
        strb.append("WHERE actionDate >= :startDate AND actionDate <= :endDate ");
        strb.append("AND approver in (:agentUsernameList)");
        strb.append("GROUP BY ");
        strb.append("approver, oprType ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        List<CountDTO> countDTOs = new ArrayList<>();
        for (Object[] obj : resultList) {
            countDTOs.add(new CountDTO(String.valueOf(obj[0]), String.valueOf(obj[1]),
                    Long.parseLong(String.valueOf(obj[2]))));
        }

        return countDTOs;
    }

    /**
     * @param loggedUser
     * @return
     */
    private Query getListOfBorderControlOfficersReporting(String loggedUser) {
        log.info("ApplicationHeaderCustomsRepoImpl-getListOfBorderControlOfficersReporting");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT e1.username FROM EmployeeDetails e1 WHERE e1.role =:bco AND e1.reportingTo in ");
        strb.append("(");
        strb.append("SELECT e2.username FROM EmployeeDetails e2 WHERE reportingTo =:loggeduser AND e2.role =:sbco");
        strb.append(") ");
        strb.append("ORDER BY e1.username ASC");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("loggeduser", loggedUser);
        q.setParameter("sbco", CommonsConstants.SR_BORDER_CONTROL_OFFICER);
        q.setParameter("bco", CommonsConstants.BORDER_CONTROL_OFFICER);

        return q;
    }

    /**
     * @param loggeduser
     * @param role
     * @return
     */
    @Override
    public Query getListOfEmployeeReporting(String loggeduser, String role) {
        log.info("ApplicationHeaderCustomsRepoImpl-getListOfEmployeeReporting");

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
    @SuppressWarnings("unchecked")
    @Override
    public AgentTrackerDTO immigrationOfficerDashboard(String loggeduser, String period) {
        log.info("ApplicationHeaderCustomsRepoImpl-immigrationOfficerDashboard");

        /* get the total agents for loggeduser */
        Query query = getListOfEmployeeReporting(loggeduser, CommonsConstants.DECISION_MAKER);

        GenericSearchDTO genericSearchDTO = CommonsUtil.getSearchDates(period);

        AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();
        Map<String, Long> mapResultList = getImmigrationOfficerCount(query.getResultList(),
                genericSearchDTO.getStartDate(), genericSearchDTO.getEndDate());

        BigDecimal avgValue = getImmigrationOfficerAverage(query.getResultList(), genericSearchDTO.getStartDate(),
                genericSearchDTO.getEndDate());
        Long pendingAllocationCount = getPendingAllocationApplications(genericSearchDTO.getStartDate(),
                genericSearchDTO.getEndDate());

        agentTrackerDTO.setPending(mapResultList.get(CommonsConstants.PENDING));
        agentTrackerDTO.setClosed(mapResultList.get(CommonsConstants.CLOSED));
        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getPending() + agentTrackerDTO.getClosed());

        if (avgValue != null) {
            agentTrackerDTO.setAverage(avgValue.setScale(2, BigDecimal.ROUND_DOWN));
        } else {
            agentTrackerDTO.setAverage(avgValue);
        }
        agentTrackerDTO.setArrival(pendingAllocationCount);
        agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + pendingAllocationCount);
        return agentTrackerDTO;
    }

    /**
     * @return
     */
    private Long getPendingAllocationApplications(Date startDate, Date endDate) {
        log.info("ApplicationHeaderCustomsRepoImpl-getPendingAllocationApplications");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(documentStatus) FROM ApplicationHeader ");
        strb.append("WHERE ");
        strb.append("documentStatus = 'SUB' AND (allocatedDate >= :startDate AND allocatedDate <= :endDate) ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        return (Long) q.getResultList().get(0);
    }

    /**
     * @param resultList
     */
    private Map<String, Long> getImmigrationOfficerCount(List<String> agentUsernameList, Date startDate, Date endDate) {
        log.info("ApplicationHeaderCustomsRepoImpl-getImmigrationOfficerCount");

        Map<String, Long> mapResult = new HashMap<>();

        /* immigration officer pending count */
        Long pendingCount = getImmigrationOfficerPendingCount(agentUsernameList, startDate, endDate);
        mapResult.put("PEN", pendingCount);

        Long closedCount = getImmigrationOfficerClosedCount(agentUsernameList, startDate, endDate);
        mapResult.put("CLS", closedCount);

        return mapResult;
    }

    /**
     * @param agentUsernameList
     * @param startDate
     * @param endDate
     * @return
     */
    private Long getImmigrationOfficerClosedCount(List<String> agentUsernameList, Date startDate, Date endDate) {
        log.info("ApplicationHeaderCustomsRepoImpl-getImmigrationOfficerClosedCount");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(documentStatus) FROM ApplicationHeader ");
        strb.append("WHERE ");
        strb.append("assignedTo in (:agentUsernameList) ");
        strb.append("AND documentStatus = :docStatus ");
        strb.append("AND (closedDate >= :startDate AND closedDate <= :endDate) ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("docStatus", CommonsConstants.CLOSED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

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
     * @return
     */
    private Long getImmigrationOfficerPendingCount(List<String> agentUsernameList, Date startDate, Date endDate) {
        log.info("ApplicationHeaderCustomsRepoImpl-getImmigrationOfficerPendingCount");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(documentStatus) FROM ApplicationHeader ");
        strb.append("WHERE ");
        strb.append("assignedTo in (:agentUsernameList) ");
        strb.append("AND documentStatus = :docStatus ");
        strb.append("AND (allocatedDate >= :startDate AND allocatedDate <= :endDate) ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("docStatus", CommonsConstants.PENDING);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        @SuppressWarnings("unchecked")
        List<Long> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty() && resultList.get(0) != null) {
            return Long.valueOf(resultList.get(0));
        }
        return 0L;
    }

    /**
     * @param agentUsernameList
     * @return
     * @return
     */
    private BigDecimal getImmigrationOfficerAverage(List<String> agentUsernameList, Date startDate, Date endDate) {
        log.info("ApplicationHeaderCustomsRepoImpl-getDecisionMakerAgentProcessReport");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT ");
        strb.append("AVG( ");
        strb.append("(DATE_PART('day', closedDateForAvgStat - submittedDate) * 3600 + ");
        strb.append("DATE_PART('hour', closedDateForAvgStat - submittedDate) * 60 + DATE_PART('minutes', closedDateForAvgStat - submittedDate)");
        strb.append(")) ");
        strb.append("FROM ApplicationHeader ");
        strb.append("WHERE documentStatus = :docStatus ");
        strb.append("AND (closedDateForAvgStat >= :startDate AND closedDateForAvgStat <= :endDate) ");
        strb.append("AND assignedTo in (:agentUsernameList) ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("docStatus", CommonsConstants.CLOSED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        @SuppressWarnings("unchecked")
        List<Double> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty() && resultList.get(0) != null) {
            return BigDecimal.valueOf(resultList.get(0));
        }
        return null;
    }

    /**
    *
    */
    @SuppressWarnings("unchecked")
    @Override
    public AgentTrackerDTO borderControlDashboardStatistics(GenericSearchDTO genericSearchDTO) {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-borderControlDashboardStatistics");

        List<String> userList = new ArrayList<>();
        genericSearchDTO = getPeriodMapping(genericSearchDTO);

        if (CommonsConstants.SR_BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getRole())) {

            /* get the BCO agents for the logged SBCO */
            Query query = getListOfEmployeeReporting(genericSearchDTO.getLoggedUser(),
                    CommonsConstants.BORDER_CONTROL_OFFICER);
            userList = query.getResultList();

        } else if (CommonsConstants.BORDER_CONTROL_OFFICER.equals(genericSearchDTO.getRole())) {
            userList.add(genericSearchDTO.getLoggedUser());

        }

        return computeDashboardForArrDepDtls(userList, genericSearchDTO);
    }
    
    /**
    *
    */
   
    @Override
    public long overstayStatistics() {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-borderControlDashboardStatistics");

        return getOverStayCount();
    }

    /**
     *
     */
    public List<String> agentTrackerForSBCO(GenericSearchDTO genericSearchDTO) {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-agentTrackerForSBCO");

        genericSearchDTO = getPeriodMapping(genericSearchDTO);

        return searchAgentTrackerForSBCO(genericSearchDTO);
    }

    /**
     * @param genericSearchDTO
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<String> searchAgentTrackerForSBCO(GenericSearchDTO genericSearchDTO) {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-searchAgentTrackerForSBCO");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT applicationNumber FROM ArrivalDepartureHistoryDetails ");
        strb.append("WHERE actionDate >= :startDate AND actionDate <= :endDate ");
        strb.append("AND approver = :bcoUsername ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("bcoUsername", genericSearchDTO.getLoggedUser());
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());

        return q.getResultList();
    }

    /**
     * @param userList
     * @param genericSearchDTO
     * @return
     */
    private AgentTrackerDTO computeDashboardForArrDepDtls(List<String> usernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-computeDashboardForArrDepDtls");

        AgentTrackerDTO agentTrackerDTO = new AgentTrackerDTO();

        /* get tracker list only if agent username list is not null/not empty */
        if (usernameList != null && !usernameList.isEmpty()) {
            List<CountDTO> countDTOs = getArrivalDepartureDashboardDetailsWithPeriod(usernameList, genericSearchDTO);

            for (CountDTO countDTO : countDTOs) {
                if (CommonsConstants.ARRIVAL.equals(countDTO.getStatusCode())) {
                    agentTrackerDTO.setArrival(countDTO.getCount());
                    agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                } else if (CommonsConstants.DEPARTURE.equals(countDTO.getStatusCode())) {
                    agentTrackerDTO.setDeparture(countDTO.getCount());
                    agentTrackerDTO.setTotalAllocated(agentTrackerDTO.getTotalAllocated() + countDTO.getCount());
                }
            }

            /* check any over stay applications */
            agentTrackerDTO.setPending(getApplicationOverStayCount(usernameList));
        }
        return agentTrackerDTO;
    }

    /**
     * @param usernameList
     * @return
     */
    private long getApplicationOverStayCount(List<String> usernameList) {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-getApplicationOverStayCount");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(applicationNumber) FROM ApplicationOverStayDetails ");
        strb.append("WHERE ");
        strb.append("approver in (:usernameList) ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("usernameList", usernameList);

        @SuppressWarnings("unchecked")
        List<Long> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty() && resultList.get(0) != null) {
            return Long.valueOf(resultList.get(0));
        }
        return 0L;
    }


    /**
     *
     * @return
     */
    private long getOverStayCount() {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-getOverStayCount");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT COUNT(applicationNumber) FROM ApplicationOverStayDetails ");

        Query q = entityManager.createQuery(strb.toString());

        @SuppressWarnings("unchecked")
        List<Long> resultList = q.getResultList();

        if (resultList != null && !resultList.isEmpty() && resultList.get(0) != null) {
            return Long.valueOf(resultList.get(0));
        }
        return 0L;
    }

    /**
     * @param usernameList
     * @param genericSearchDTO
     * @return
     */
    private List<CountDTO> getArrivalDepartureDashboardDetailsWithPeriod(List<String> agentUsernameList,
            GenericSearchDTO genericSearchDTO) {
        log.info("ArrivalDepartureHistoryDetailsCustomsRepoImpl-getArrivalDepartureDashboardDetailsWithPeriod");

        StringBuilder strb = new StringBuilder();
        strb.append("SELECT oprType, COUNT(oprType) FROM ArrivalDepartureHistoryDetails ");
        strb.append("WHERE actionDate >= :startDate AND actionDate <= :endDate ");
        strb.append("AND approver in (:agentUsernameList)");
        strb.append("GROUP BY ");
        strb.append("oprType ");

        Query q = entityManager.createQuery(strb.toString());
        q.setParameter("agentUsernameList", agentUsernameList);
        q.setParameter("startDate", genericSearchDTO.getStartDate());
        q.setParameter("endDate", genericSearchDTO.getEndDate());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = q.getResultList();

        List<CountDTO> countDTOs = new ArrayList<>();
        for (Object[] obj : resultList) {
            countDTOs.add(new CountDTO(String.valueOf(obj[0]), Long.parseLong(String.valueOf(obj[1]))));
        }

        return countDTOs;
    }

}
