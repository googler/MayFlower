package com.paladin.action;

import com.paladin.mvc.RequestContext;

/**
 * Admin Action
 * 
 * @author Erhu
 * @since Mar 19th, 2011
 */
public class AdminAction extends BaseAction {

	public void index(final RequestContext _reqCtxt) {

		if (checkUser(_reqCtxt)) {// 登录用户
			forward(_reqCtxt, "/html/admin/admin_index.jsp");
		} else {
			redirect(_reqCtxt, "/login/auto");
		}
	}
}
