/**
 * 
 */
package com.hypercube.evisa.common.api.domain;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "APPLICANT_VERIFICATION_TOKEN")
public class ApplicantVerificationToken {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "APPLICANT_VERIFICATION_TOKEN_ID_SEQ")
    @SequenceGenerator(name = "APPLICANT_VERIFICATION_TOKEN_ID_SEQ", sequenceName = "APPLICANT_VERIFICATION_TOKEN_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", length = 25)
    private Long id;

    /**
     * 
     */
    @Column(name = "TOKEN", length = 100)
    private String token;

    /**
     * 
     */
    @OneToOne(targetEntity = ApplicantUserLogin.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "username")
    private ApplicantUserLogin applicantUserLogin;

    /**
     * 
     */
    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    /**
     * @param expiryTimeInMinutes
     * @return
     */
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
    
    /**
     * @param applicantUserLogin
     * @param token
     * @param expiry
     */
    public ApplicantVerificationToken(ApplicantUserLogin applicantUserLogin, String token, int expiry) {
        this.applicantUserLogin = applicantUserLogin;
        this.expiryDate = calculateExpiryDate(expiry);
        this.token = token;
    }

}
