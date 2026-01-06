package com.hypercube.evisa.common.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.customsrepoimpl.ApplicantTravelHistoryCustomsRepoImpl;
import com.hypercube.evisa.common.api.domain.AuditDetails;

import lombok.Data;
import lombok.extern.slf4j.*;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@Slf4j
public class CommonQueueUtilService {

	private static final Logger log = LoggerFactory.getLogger(CommonQueueUtilService.class);
    /**
     * 
     */
    @Autowired(required = true)
    private JmsTemplate jmsTemplate;

    /**
     * @param audit
     */
    public void sendAuditDetailsToQueue(AuditDetails auditDetails) {
        log.info("CommonQueueUtilService::sendAuditDetailsToQueue");
        jmsTemplate.convertAndSend("auditbox", auditDetails);
    }
    
    /**
     * @param faqChange
     */
    public void notifyFAQToQueue() {
        log.info("CommonQueueUtilService::notifyFAQToQueue");
        jmsTemplate.convertAndSend("faqbox", "NotifyFAQ");
    }

}
