/**
 * 
 */
package com.hypercube.evisa.common.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TSYS_FAQ")
@NoArgsConstructor
public class SystemFAQ {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "system_faq_seq")
    @SequenceGenerator(name = "system_faq_seq", sequenceName = "system_faq_seq", allocationSize = 1)
    @Column(name = "faq_id", length = 25)
    private Long faqId;

    /**
     * 
     */
    @Column(name = "faq_type", length = 10, nullable = true)
    private String faqType;

    /**
     * 
     */
    @Column(name = "faq_question", length = 150, nullable = false)
    private String faqQuestion;

    /**
     * 
     */
    @Column(name = "faq_question_other", length = 150, nullable = false)
    private String faqQuestionOther;

    /**
     * 
     */
    @Column(name = "faq_answer", length = 1000, nullable = false)
    private String faqAnswer;

    /**
     * 
     */
    @Column(name = "faq_answer_other", length = 1000, nullable = false)
    private String faqAnswerOther;

    /**
     * 
     */
    @Column(name = "status", length = 1, nullable = false)
    private String status;

    /**
     * 
     */
    @Column(name = "created_by", length = 25, nullable = false)
    private String createdBy;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "created_date", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @Column(name = "updated_by", length = 20, nullable = true)
    private String updatedBy;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * @param id
     * @param faqType
     * @param faqQuestion
     * @param faqQuestionOther
     * @param faqAnswer
     * @param faqAnswer_other
     * @param status
     */
    public SystemFAQ(Long faqId, String faqType, String faqQuestion, String faqQuestionOther, String faqAnswer,
            String faqAnswerOther, String status) {
        super();
        this.faqId = faqId;
        this.faqType = faqType;
        this.faqQuestion = faqQuestion;
        this.faqQuestionOther = faqQuestionOther;
        this.faqAnswer = faqAnswer;
        this.faqAnswerOther = faqAnswerOther;
        this.status = status;
    }

}
