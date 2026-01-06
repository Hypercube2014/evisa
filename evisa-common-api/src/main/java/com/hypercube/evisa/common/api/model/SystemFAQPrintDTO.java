package com.hypercube.evisa.common.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Getter
@Setter
public class SystemFAQPrintDTO {

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
     * @param faqType
     * @param faqQuestion
     * @param faqQuestionOther
     * @param faqAnswer
     * @param faqAnswerOther
     */
    public SystemFAQPrintDTO(String faqType, String faqQuestion, String faqQuestionOther, String faqAnswer,
            String faqAnswerOther) {
        super();
        this.faqType = faqType;
        this.faqQuestion = faqQuestion;
        this.faqQuestionOther = faqQuestionOther;
        this.faqAnswer = faqAnswer;
        this.faqAnswerOther = faqAnswerOther;
    }

}
