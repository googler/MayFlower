/**
 * Copyright (C) 2011 Erhu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
