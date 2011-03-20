package com.paladin.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paladin.bean.User;
import com.paladin.common.Tools;
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
			redirect(_reqCtxt, "/login/auto");
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

		if (Tools.isNullString(id)) {// 添加新代码
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
