package com.hypercube.evisa.common.api.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
@Component
public class EmailConfig {
    
    /**
     * 
     */
    @Value("${mail.host.name}")
    private String hostname;

    /**
     * 
     */
    @Value("${mail.host.portno}")
    private int hostport;

    /**
     * 
     */
    @Value("${mail.user.name}")
    private String username;

    /**
     * 
     */
    @Value("${mail.user.key}")
    private String secretkey;
    
    /**
     * 
     */
    @Value("${mail.smtp.starttls.enable}")
    private String starttls;
    
    /**
     * 
     */
    @Value("${mail.smtp.auth}")
    private String auth;
    
    /**
     * 
     */
    @Value("${mail.debug.enabled}")
    private String mailEnabled;
    
    /**
     * 
     */
    @Value("${mail.smtp.connectiontimeout}")
    private long connectionTimeOut;
    
    /**
     * 
     */
    @Value("${mail.smtp.timeout}")
    private long timeout;
    
    /**
     * 
     */
    @Value("${mail.smtp.writetimeout}")
    private long writeTimeOut;


    /**
     * @return
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(hostname);
        mailSender.setPort(hostport);
        mailSender.setUsername(username);
        mailSender.setPassword(secretkey);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", mailEnabled);
        props.put("mail.smtp.connectiontimeout", connectionTimeOut);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.writetimeout", writeTimeOut);

        return mailSender;
    }

}
