package com.hypercube.evisa.common.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.hypercube.evisa.common.api.domain.AuditDetails;
import com.hypercube.evisa.common.api.service.AuditDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Component
@Data
@Slf4j
public class AuditReceiver {

    /**
     * 
     */
    @Autowired(required = true)
    AuditDetailsService auditService;

    /**
     * @param auditDetails
     */
    @JmsListener(destination = "auditbox", containerFactory = "myFactory")
    public void receiveMessage(AuditDetails auditDetails) {
        log.info("AuditReceiver::receiveMessage::auditbox");
        auditService.saveAuditDetails(auditDetails);
    }

}
