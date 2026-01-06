package com.hypercube.evisa.applicant.api.util;

import java.io.IOException;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.hypercube.evisa.common.api.model.MailDTO;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Service
public class EmailUtilService {

    /**
     * 
     */
    @Autowired(required = true)
    private JavaMailSender javaMailSender;

    /**
     * 
     */
    @Autowired
    Configuration fmConfiguration;

    /**
     * 
     */
    public EmailUtilService() {
        super();
    }

    /**
     * @param javaMailSender
     */
    public EmailUtilService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * @param mail
     * @throws TemplateException,
     *             MessagingException
     * @throws IOException
     */
    public void sendMail(MailDTO mail) throws IOException, TemplateException, MessagingException {
        log.info("EmailUtilService::sendMail {}", mail.getMailTo());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject(mail.getMailSubject());
        mimeMessageHelper.setFrom(mail.getMailFrom());
        mimeMessageHelper.setTo(mail.getMailTo());
        mail.setMailContent(geContentFromTemplate(mail.getModel(), mail.getDocType()));
        mimeMessageHelper.setText(mail.getMailContent(), true);
        //mimeMessageHelper.addInline("header3.png", new ClassPathResource("images/header3.png"));
        //mimeMessageHelper.addInline("background_2", new ClassPathResource("images/background_2.png"));
        javaMailSender.send(mimeMessageHelper.getMimeMessage());
    }

    /**
     * @param model
     * @return
     */
    public String geContentFromTemplate(Map<String, Object> model, String docType)
            throws IOException, TemplateException {
        log.info("EmailUtilService::geContentFromTemplate");

        StringBuffer content = new StringBuffer();
        content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(docType + ".ftl"),
                model));
        return content.toString();
    }

}
