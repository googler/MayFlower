package com.paladin.common;

import javax.servlet.http.HttpServletRequest;

/**
 * request utils
 * 
 * @author Erhu
 * @since Mar 10th, 2011
 */
public class RequestUtils {
	/**
	 * get remote address
	 * 
	 * @param _req
	 * @return
	 */
	public static String getRemoteAddress(final HttpServletRequest _req) {
		return _req.getRemoteAddr();
	}
}
