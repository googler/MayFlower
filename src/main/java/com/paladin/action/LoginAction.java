package com.paladin.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paladin.bean.User;
import com.paladin.common.RequestUtils;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

/**
 * LoginAction
 * 
 * @author Erhu
 * @since 2.0 Mar 8th, 2011
 */
public class LoginAction extends BaseAction {
	private static Log log = LogFactory.getLog(LoginAction.class);

	/**
	 * forward to login page
	 * 
	 * @param ctx
	 * @throws IOException
	 */
	public void auto(final RequestContext _reqCtxt) {
		final HttpServletRequest request = _reqCtxt.request();
		String ip = RequestUtils.getRemoteAddress(request);
		log.info("Hi, someone from " + ip + " get ready to login!");
		genUserByIp(_reqCtxt);
		forward(_reqCtxt, "/html/login.jsp");
	}

	public void index(final RequestContext _reqCtxt) {
		final HttpServletRequest request = _reqCtxt.request();
		String ip = RequestUtils.getRemoteAddress(request);
		log.info("Hi, someone from " + ip + " get ready to login!");
		_reqCtxt.session().removeAttribute("user");
		forward(_reqCtxt, "/html/login.jsp");
	}

	/**
	 * get/generate user by ip
	 * 
	 * @param _ip
	 */
	private void genUserByIp(final RequestContext _reqCtxt) {
		final HttpServletRequest request = _reqCtxt.request();
		final HttpSession session = _reqCtxt.session();
		String ip = RequestUtils.getRemoteAddress(request);
		// check the ip if it was recorded before
		StringBuilder sql_builder = new StringBuilder();
		User user = getUserByIp(ip);
		if (user == null) {// if not recorded, generate new user
			String password = "12345";
			sql_builder.append("INSERT INTO USER(USERNAME, PASSWORD, NICKNAME, EMAIL, REG_DATE, ");
			sql_builder
					.append("LASTLOGIN_DATE, FIRST_IP, ROLE) VALUES (?, ?, ?, ?, NOW(), NOW(), ?, 'visitor')");
			QueryHelper.update(sql_builder.toString(), new Object[] { ip, password, ip, ip, ip });
			user = getUserByIp(ip);
		}
		session.setAttribute("user", user);
		request.setAttribute("msg", "当前用户名和密码系自动生成，请登录后修改！");
	}

	/**
	 * Login
	 * 
	 * @param _reqCtxt
	 * @return
	 */
	public void doLogin(final RequestContext _reqCtxt) {
		final HttpServletRequest request = _reqCtxt.request();
		final HttpSession session = _reqCtxt.session();
		session.removeAttribute("user");
		String email = request.getParameter("email").trim();
		String pwd = request.getParameter("pwd").trim();
		User user = getUser(email, pwd);
		if (null == user) {
			request.setAttribute("msg", "Oops! 你输入的帐户信息有误:(");
			forward(_reqCtxt, "/html/login.jsp");
			return;
		}
		session.setAttribute("user", user);
		redirect(_reqCtxt, "/");
	}

	public void exit(final RequestContext _reqCtxt) {
		final HttpSession session = _reqCtxt.session();
		session.removeAttribute("user");
		redirect(_reqCtxt, "/login");
	}

	private User getUserByIp(final String _ip) {
		return QueryHelper.read(User.class, "SELECT * FROM USER WHERE FIRST_IP = ?", new Object[] { _ip });
	}

	private User getUser(final String _email, final String _pwd) {
		return QueryHelper.read(User.class, "SELECT * FROM USER WHERE EMAIL = ? AND PASSWORD = ?",
				new Object[] { _email, _pwd });
	}
}