package com.hypercube.evisa.approver.api.comparator;

import java.util.Comparator;
import java.util.Date;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLoginHistory;

/**
 * @author SivaSreenivas
 *
 */
public class EmployeeLoginHistoryComparator implements Comparator<EmployeeUserLoginHistory> {

	/**
	 *
	 */
	@Override
	public int compare(EmployeeUserLoginHistory o1, EmployeeUserLoginHistory o2) {
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
