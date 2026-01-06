package com.hypercube.evisa.applicant.api.constants;

import org.springframework.stereotype.Component;

/**
 * @author newrta
 *
 */
@Component
public final class UtilParamPropertyConstants {

	/**
	 * 
	 */
	private static String userPwdExpiryFlag;

	/**
	 * 
	 */
	private static String userAccExpiryFlag;

	/**
	 * 
	 */
	private UtilParamPropertyConstants() {
		super();
	}

	/**
	 * @return
	 */
	public static String getUserPwdExpiryFlag() {
		return userPwdExpiryFlag;
	}

	/**
	 * @param userPwdExpiryFlag
	 */
	public static void setUserPwdExpiryFlag(String userPwdExpiryFlag) {
		UtilParamPropertyConstants.userPwdExpiryFlag = userPwdExpiryFlag;
	}

	/**
	 * @return
	 */
	public static String getUserAccExpiryFlag() {
		return userAccExpiryFlag;
	}

	/**
	 * @param userAccExpiryFlag
	 */
	public static void setUserAccExpiryFlag(String userAccExpiryFlag) {
		UtilParamPropertyConstants.userAccExpiryFlag = userAccExpiryFlag;
	}

}
