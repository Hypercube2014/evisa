package com.hypercube.evisa.applicant.api.util;

import java.util.Calendar;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.applicant.api.model.ApplicantUserDetailsDTO;
import com.hypercube.evisa.common.api.domain.ApplicantUserLogin;
import com.hypercube.evisa.common.api.domain.ApplicantUserLoginAttempts;
import com.hypercube.evisa.common.api.repository.ApplicantUserLoginAttemptsRepository;
import com.hypercube.evisa.common.api.repository.ApplicantUserLoginRepository;
import com.hypercube.evisa.common.api.util.CommonsUtil;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sivasreenivas
 *
 */
@Service
@Transactional
@Slf4j
@Data
public class ApplicantUserDetailsService implements UserDetailsService {

    private static final int MILLI_SECONDS = 1200000;

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantUserLoginRepository applicantUserLoginRepository;

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantUserLoginAttemptsRepository applicantUserLoginAttemptsRepository;

    /**
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loaded -=-=-=- loadUserByUsername");

        ApplicantUserLogin user = applicantUserLoginRepository.findByUserNameIgnoreCase(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Not found: " + username);
        } else {
            return verifyUserDetails(user);
        }
    }

    private UserDetails verifyUserDetails(ApplicantUserLogin user) {
        if ((CommonsUtil.getTime(user.getCredentialsExpiryDate())
                - CommonsUtil.getTime(Calendar.getInstance().getTime())) <= 0) {
            applicantUserLoginAttemptsRepository.updateChangePasswordRequired(true, user.getUserName());
        }

        ApplicantUserLoginAttempts userAttempts = applicantUserLoginAttemptsRepository
                .findByUsername(user.getUserName());

        /* verifying whether the account is locked */
        if (!user.isAccountNonLocked()) {

            /* account is locked */

            /*
             * checking the last login attempt with wrong credentials - less
             * than 20 Minutes
             */
            if ((CommonsUtil.getTime(Calendar.getInstance().getTime())
                    - getUserLastModifiedTime(userAttempts)) <= MILLI_SECONDS) {

                /*
                 * identified its less than 20 Minutes and no change to the
                 * account is locked
                 */
                user.setAccountNonLocked(false);
            } else {

                /*
                 * its more than 20mins since the last wrong attempt and
                 * reseting fail attempts and also removing the account lock
                 */
                applicantUserLoginAttemptsRepository.resetFailAttempts(user.getUserName());
                applicantUserLoginAttemptsRepository.updateUserAccountLock(true, user.getUserName());
                user.setAccountNonLocked(true);
            }

            /* account non locked & checking no user attempts */
        } else if (userAttempts == null) {

            /* no entries in user attempts so account non locked */
            user.setAccountNonLocked(true);

            /* account non locked & checking user attempts available */
        } else if (listUserAttempts(userAttempts) > 0) {

            /* user attempts are available even the account is non locked */

            /* identifying the last user attempt */
            if ((CommonsUtil.getTime(Calendar.getInstance().getTime())
                    - getUserLastModifiedTime(userAttempts)) >= MILLI_SECONDS) {

                /*
                 * user attempting after 20 Minutes and need to reset failed
                 * attempts and update account non lock as true
                 */
                applicantUserLoginAttemptsRepository.resetFailAttempts(user.getUserName());
                applicantUserLoginAttemptsRepository.updateUserAccountLock(true, user.getUserName());
            }

            /* account non locked */
            user.setAccountNonLocked(true);
        }
        return new ApplicantUserDetailsDTO(user);

    }

    private int listUserAttempts(ApplicantUserLoginAttempts userAttempts) {
        return userAttempts.getAttempts();
    }

    /**
     * @param userAttempts
     * @return
     */
    private long getUserLastModifiedTime(ApplicantUserLoginAttempts userAttempts) {
        return CommonsUtil.getTime(userAttempts.getLastModified());
    }

    /**
     * @param username
     * @return
     */
    public ApplicantUserLogin getUserDetailsByUserName(String username) {
        return applicantUserLoginRepository.findByUserNameIgnoreCase(username);
    }

    /**
     * @param userLogin
     * @return
     */
    public ApplicantUserLogin generateUserCredentials(ApplicantUserLogin userLogin) {
        return applicantUserLoginRepository.save(userLogin);
    }

    /**
     * @param userlogin
     * @return
     */
    public ApplicantUserLogin updateUserCredentials(ApplicantUserLogin userlogin) {
        return applicantUserLoginRepository.save(userlogin);
    }
    
    /**
     * @param accountenabled
     * @param username
     */
    public void updateUserAccountEnabled(boolean accountenabled, String username){
        applicantUserLoginAttemptsRepository.updateUserAccountEnabled(accountenabled, username);
    }

    /**
     * @param userName
     * @param encodePassword
     */
    public void forgetPasswordChange(String userName, String encodePassword) {
        log.info("ApplicantUserDetailsService-forgetPasswordChange");
        applicantUserLoginRepository.forgetPasswordChange(userName, encodePassword);
    }

}
