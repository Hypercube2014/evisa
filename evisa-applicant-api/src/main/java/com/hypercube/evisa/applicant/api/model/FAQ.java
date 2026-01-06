package com.hypercube.evisa.applicant.api.model;

import javax.persistence.*;

@Entity
@Table(name = "tsys_faq")
public class FAQ {
    @Id

    @Column(name = "faq_id")
    private Long faqId;

    @Column(name = "faq_type")
    private String faqType;

    @Column(name = "faq_question")
    private String faqQuestion;

    @Column(name = "faq_question_other")
    private String faqQuestionOther;

    @Column(name = "faq_answer")
    private String faqAnswer;

    @Column(name = "faq_answer_other")
    private String faqAnswerOther;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private String updatedDate;


    public FAQ() {
    }


    public FAQ(Long faqId, String faqType, String faqQuestion, String faqQuestionOther, String faqAnswer, String faqAnswerOther, String createdBy, String createdDate, String status, String updatedBy, String updatedDate) {
        this.faqId = faqId;
        this.faqType = faqType;
        this.faqQuestion = faqQuestion;
        this.faqQuestionOther = faqQuestionOther;
        this.faqAnswer = faqAnswer;
        this.faqAnswerOther = faqAnswerOther;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.status = status;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public FAQ(String faqType, String faqQuestion, String faqQuestionOther, String faqAnswer, String faqAnswerOther, String createdBy, String createdDate, String status, String updatedBy, String updatedDate) {
        this.faqType = faqType;
        this.faqQuestion = faqQuestion;
        this.faqQuestionOther = faqQuestionOther;
        this.faqAnswer = faqAnswer;
        this.faqAnswerOther = faqAnswerOther;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.status = status;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }


    public Long getFaqId() {
        return this.faqId;
    }

    public void setFaqId(Long faqId) {
        this.faqId = faqId;
    }

    public String getFaqType() {
        return this.faqType;
    }

    public void setFaqType(String faqType) {
        this.faqType = faqType;
    }

    public String getFaqQuestion() {
        return this.faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public String getFaqQuestionOther() {
        return this.faqQuestionOther;
    }

    public void setFaqQuestionOther(String faqQuestionOther) {
        this.faqQuestionOther = faqQuestionOther;
    }

    public String getFaqAnswer() {
        return this.faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }

    public String getFaqAnswerOther() {
        return this.faqAnswerOther;
    }

    public void setFaqAnswerOther(String faqAnswerOther) {
        this.faqAnswerOther = faqAnswerOther;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }


    @Override
    public String toString() {
        return "{" +
            " faqId='" + getFaqId() + "'" +
            ", faqType='" + getFaqType() + "'" +
            ", faqQuestion='" + getFaqQuestion() + "'" +
            ", faqQuestionOther='" + getFaqQuestionOther() + "'" +
            ", faqAnswer='" + getFaqAnswer() + "'" +
            ", faqAnswerOther='" + getFaqAnswerOther() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }

    
}
