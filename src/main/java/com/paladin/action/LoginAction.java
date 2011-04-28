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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Strings;
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
        String r = request.getParameter("r");
        log.info("Hi, someone from " + ip + " get ready to login!");
        _reqCtxt.session().removeAttribute("user");
        request.setAttribute("r", r);
        forward(_reqCtxt, "/html/login.jsp");
    }

    /**
     * get/generate user by ip
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
            QueryHelper.update(sql_builder.toString(), new Object[]{ip, password, ip, ip, ip});
            user = getUserByIp(ip);
        }
        session.setAttribute("user", user);
        request.setAttribute("msg", "当前用户名和密码系自动生成，请登录后修改！");
    }

    /**
     * Login
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
        String r = request.getParameter("r");
        if (Strings.isNullOrEmpty(r))
            redirect(_reqCtxt, "/");
        else
            redirect(_reqCtxt, r);
    }

    public void exit(final RequestContext _reqCtxt) {
        final HttpSession session = _reqCtxt.session();
        session.removeAttribute("user");
        String r = _reqCtxt.request().getParameter("r");
        if (Strings.isNullOrEmpty(r))
            redirect(_reqCtxt, "/login");
        else
            redirect(_reqCtxt, r);
    }

    private User getUserByIp(final String _ip) {
        return QueryHelper.read(User.class, "SELECT * FROM USER WHERE FIRST_IP = ?", _ip);
    }

    private User getUser(final String _email, final String _pwd) {
        return QueryHelper.read(User.class, "SELECT * FROM USER WHERE EMAIL = ? AND PASSWORD = ?",
                new Object[]{_email, _pwd});
    }
}