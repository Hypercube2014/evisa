package com.hypercube.evisa.applicant.api.serviceimpl;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.applicant.api.model.ContactUsInfoDTO;
import com.hypercube.evisa.applicant.api.service.ContactUsService;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MailDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Data
@NoArgsConstructor
@Slf4j
public class ContactUsServiceImpl implements ContactUsService {

    /**
     * 
     */
    @Value("${mail.sent.from}")
    private String mailFrom;

    /**
     * 
     */
    @Value("${mail.support}")
    private String support;

    /**
     * 
     */
    @Value("${mail.renseignement}")
    private String renseignement;

    /**
     * 
     */
    @Autowired(required = true)
    private JmsTemplate jmsTemplate;

    /**
     *
     */
    @Override
    public ApiResultDTO sendContactUsFeedback(ContactUsInfoDTO contactUsInfoDTO, String locale) {
        log.info("ContactUsServiceImpl-sendContactUsFeedback");

        String subject;
        String mailToId;
        String team;
        
        if (contactUsInfoDTO.getSubject().equals("S")) {
            subject = "Support";
            mailToId = support;
            team = "Support Team";
        } else if (contactUsInfoDTO.getSubject().equals("R")) {
            subject = "Renseignement";
            mailToId = renseignement;
            team = "Renseignement Team";
        } else {
            log.info("Invalid Subject");
            return new ApiResultDTO("ERROR", "Invalid Subject");
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("team", team);
        model.put("contactname", contactUsInfoDTO.getContactName());
        model.put("contactemail", contactUsInfoDTO.getContactEmail());
        model.put("comment", contactUsInfoDTO.getContactComment());
        jmsTemplate.convertAndSend("mailbox", new MailDTO(mailFrom, mailToId, subject, "contactus", model));

        return new ApiResultDTO("SUCCESS", "Thanks For the Info/Details, Will get back to you shortly");
    }

}
