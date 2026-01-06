/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
public class MailDTO {

    /**
     * 
     */
    private String mailFrom;

    /**
     * 
     */
    private String mailTo;

    /**
     * 
     */
    private String mailSubject;
    
    /**
     * 
     */
    private String mailContent;

    /**
     * 
     */
    private String contentType;

    /**
     * 
     */
    private String docType;

    /**
     * 
     */
    private Map<String, Object> model;

    /**
     * 
     */
    public MailDTO() {
        contentType = "text/plain";
    }

    /**
     * @param mailFrom
     * @param mailTo
     * @param mailSubject
     * @param docType
     * @param model
     */
    public MailDTO(String mailFrom, String mailTo, String mailSubject, String docType, Map<String, Object> model) {
        super();
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.mailSubject = mailSubject;
        this.docType = docType;
        this.model = model;
    }

}
