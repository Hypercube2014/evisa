package com.hypercube.evisa.common.api.comparator;

import java.util.Comparator;


import java.util.Date;


import com.hypercube.evisa.common.api.domain.ApplicantUserLoginHistory;


/**
 * @author SivaSreenivas
 *
 */
public class UserLoginHistoryComparator implements Comparator<ApplicantUserLoginHistory> {

    /**
     *
     */
    @Override
    public int compare(ApplicantUserLoginHistory o1, ApplicantUserLoginHistory o2) {
        /* passing dates to compare */
        return compareLoginTime(o1.getLoginTime(), o2.getLoginTime());
    }

    /**
     * @param loginTime
     * @param loginTime2
     * @return
     */
    private int compareLoginTime(Date loginTime, Date loginTime2) {
        /* method to compare two dates */
        return loginTime.compareTo(loginTime2);
    }

}
