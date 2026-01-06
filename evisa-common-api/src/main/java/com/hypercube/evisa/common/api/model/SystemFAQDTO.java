/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import javax.persistence.Column;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class SystemFAQDTO {

    /**
     * 
     */
    private Long faqId;

    /**
     * 
     */
    private String faqType;

    /**
     * 
     */
    private String faqQuestion;

    /**
     * 
     */
    private String faqQuestionOther;

    /**
     * 
     */
    private String faqAnswer;

    /**
     * 
     */
    private String faqAnswerOther;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String createdBy;

    /**
     * 
     */
    @Column(name = "updated_by", length = 20, nullable = true)
    private String updatedBy;

}
