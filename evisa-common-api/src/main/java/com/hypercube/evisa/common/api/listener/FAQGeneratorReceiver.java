package com.hypercube.evisa.common.api.listener;

import java.io.IOException;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.SystemFAQDTOList;
import com.hypercube.evisa.common.api.service.SystemFAQService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Component
@Data
@Slf4j
public class FAQGeneratorReceiver {

    /**
     * 
     */
    @Autowired(required = true)
    private SystemFAQService systemFAQService;

    /**
     * @param auditDetails
     * @throws IOException
     */
    @JmsListener(destination = "faqbox", containerFactory = "myFactory")
    public void receiveMessage(String faqDetails) throws IOException {
        log.info("FAQGeneratorReceiver::receiveMessage {}", faqDetails);

        SystemFAQDTOList systemFAQDTOList = new SystemFAQDTOList(
                systemFAQService.findAllActiveFAQ(CommonsConstants.YES));
        FileWriterWithEncoding file = null;

        try {
            log.info("FAQGeneratorReceiver-file writer");
            file = new FileWriterWithEncoding("/home/ubuntu/hypercube/evisa/applicant/rel1_0/faq/faq.json", "UTF-8");
            log.info("FAQGeneratorReceiver-convert json");
            file.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(systemFAQDTOList));

        } catch (IOException ioe) {
            log.error("FAQGeneratorReceiver-receiveMessage-IOException {}", ioe);
        } catch (Exception exe) {
            log.error("FAQGeneratorReceiver-receiveMessage-Exception {}", exe);
        } finally {
            if (file != null) {
                file.close();
            }
        }
    }

}
