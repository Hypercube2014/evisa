package com.hypercube.evisa.common.api.domain;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hypercube.evisa.common.api.comparator.UserLoginHistoryComparator;

import lombok.Data;

@Entity
@Table(name = "applicant_user_login")
@Data
public class ApplicantUserLogin {

    /**
     * 
     */
    @Id
    @Column(name = "username", length = 25, unique = true)
    private String userName;

    /**
     * 
     */
    @Column(name = "password", length = 60)
    private String password;

    /**
     * 
     */
    @Column(name = "account_expiry_date")
    private Date accountExpiryDate;

    /**
     * 
     */
    @Column(name = "credentials_expiry_date")
    private Date credentialsExpiryDate;

    /**
     * 
     */
    @Column(name = "account_enabled", nullable = false)
    private boolean accountEnabled;

    /**
     * 
     */
    @Column(name = "change_password_required", nullable = false)
    private boolean changePasswordRequired;

    /**
     * 
     */
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    /**
     * 
     */
    @Column(name = "roles", length = 25)
    private String roles;
    
    /**
     * 
     */
    @Transient
    private String emailId;
    
    /**
     * 
     */
    @Transient
    private String locale;

    /**
     * 
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<ApplicantUserLoginHistory> applicantUserLoginHistoryList = new ArrayList<ApplicantUserLoginHistory>();

    /**
     * @return
     */
    public String getLastLogin() {
        Collections.sort(this.applicantUserLoginHistoryList, new UserLoginHistoryComparator());
        ApplicantUserLoginHistory applicantUserLoginHistory = (applicantUserLoginHistoryList.isEmpty()) ? null
                : applicantUserLoginHistoryList.get(applicantUserLoginHistoryList.size() - 1);
        return (applicantUserLoginHistory == null) ? "" : getLastLoginTime(applicantUserLoginHistory);
    }

    /**
     * @param applicantUserLoginHistory
     * @return
     */
    private String getLastLoginTime(ApplicantUserLoginHistory applicantUserLoginHistory) {
        return applicantUserLoginHistory.getLoginTime() + " from IP " + applicantUserLoginHistory.getLoginIp();
    }

}
