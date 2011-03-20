package com.paladin.action;

import com.paladin.mvc.RequestContext;

/**
 * Api Action
 * 
 * @author Erhu
 * @since Mar 8th, 2011
 */
public class ApiAction extends BaseAction {

	public void index(final RequestContext _reqCtxt) {
		forward(_reqCtxt, "/html/api/api_index.jsp");
	}
}
