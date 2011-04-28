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
package com.paladin.action;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Strings;
import com.paladin.bean.User;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

/**
 * Admin Action
 * 
 * @author Erhu
 * @since Mar 19th, 2011
 */
public class AdminAction extends BaseAction {
	private static Log log = LogFactory.getLog(AdminAction.class);

	/**
	 * 编辑个人信息
	 * 
	 * @param _reqCtxt
	 */
	public void index(final RequestContext _reqCtxt) {
		User user = getUserFromSession(_reqCtxt);
		if (user != null) {
			forward(_reqCtxt, "/html/admin/user_info.jsp");
		} else {
			redirect(_reqCtxt, "/login");
		}
	}

	/**
	 * 查看其他用户信息
	 * 
	 * @param _reqCtxt
	 * @param _userid
	 */
	public void userinfo(final RequestContext _reqCtxt, final String _userid) {

	}

	/**
	 * 查看服务器信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sysinfo(final RequestContext _reqCtxt) {
		HttpServletRequest req = _reqCtxt.request();
		Map<String, Object> sysProperties = new TreeMap(System.getProperties());
		Map<String, String> env = new TreeMap<String, String>(System.getenv());

		Runtime r = Runtime.getRuntime();

		long maxMemory = r.maxMemory() / 1024000L;
		long freeMemory = r.freeMemory() / 1024000L;
		int processorNum = r.availableProcessors();

		req.setAttribute("sysProperties", sysProperties);
		req.setAttribute("env", env);

		req.setAttribute("maxMemory", Long.valueOf(maxMemory));
		req.setAttribute("freeMemory", Long.valueOf(freeMemory));
		req.setAttribute("processorNum", Integer.valueOf(processorNum));
		forward(_reqCtxt, "/html/admin/sys_info.jsp");
	}

	/**
	 * 保存用户信息
	 */
	public void save(final RequestContext _reqCtxt) {
		final HttpServletRequest request = _reqCtxt.request();
		String id = request.getParameter("id");
		String username = request.getParameter("username").trim();

		String nickname = request.getParameter("nickname").trim();
		String password = request.getParameter("password").trim();
		String email = request.getParameter("email").trim();
		String profile = request.getParameter("profile").trim();
		String ip = request.getParameter("ip");

		StringBuilder sql_builder = new StringBuilder();

		if (Strings.isNullOrEmpty(id)) {// 添加新代码
			sql_builder.append("INSERT INTO USER(USERNAME, PASSWORD, NICKNAME, EMAIL, REG_DATE, ");
			sql_builder.append("LASTLOGIN_DATE, FIRST_IP, ROLE, PROFILE) VALUES (?, ?, ?, ?, ");
			sql_builder.append("NOW(), NOW(), ?, 'visitor', ?)");
			QueryHelper.update(sql_builder.toString(), new Object[] { username, password, nickname, email,
					ip, profile });
			log.info("add new user success");
			forward(_reqCtxt, "/login");
		} else {// 修改代码
			String sql = "UPDATE USER SET USERNAME = ?, NICKNAME = ?, PASSWORD = ?, EMAIL = ?, PROFILE = ? WHERE ID = ?";
			QueryHelper.update(sql, new Object[] { username, nickname, password, email, profile, id });
			log.info("update user success");
			User user = QueryHelper.read(User.class, "SELECT * FROM USER WHERE ID = ?", id);
			_reqCtxt.session().setAttribute("user", user);
			request.setAttribute("msg", "保存成功!");
			forward(_reqCtxt, "/admin");
		}
	}
}
