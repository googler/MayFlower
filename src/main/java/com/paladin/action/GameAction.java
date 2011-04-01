package com.paladin.action;

import com.paladin.mvc.RequestContext;

/**
 * Game Action
 * 
 * @author Erhu
 * @since Mar 27th, 2011
 */
public class GameAction extends BaseAction {

	public void index(final RequestContext _reqCtxt) {
		if (_reqCtxt.sessionAttr("user") != null) {
			forward(_reqCtxt, "/html/game/index.jsp");
		} else {
			forward(_reqCtxt, "/login?r=/game");
		}
	}
}
