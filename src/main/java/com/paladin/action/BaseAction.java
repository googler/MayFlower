package com.paladin.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paladin.bean.User;
import com.paladin.mvc.RequestContext;

/**
 * base action
 * 
 * @author Erhu
 * @since Mar 9th, 2011
 */
public class BaseAction {

	public static Log log = LogFactory.getLog(BaseAction.class);

	protected User getUserFromSession(final RequestContext _reqCtxt) {
		return (User) _reqCtxt.session().getAttribute("user");
	}

	protected void redirect(final RequestContext _reqCtxt, final String _uri) {
		try {
			_reqCtxt.response().sendRedirect(_uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void forward(final RequestContext _reqCtxt, final String _uri) {
		try {
			_reqCtxt.request().getRequestDispatcher(_uri).forward(_reqCtxt.request(), _reqCtxt.response());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * init
	 * 
	 * @param _ctxt
	 */
	public void init(ServletContext _ctxt) {
		log.info(this.getClass().getName() + " init...");
	}
}
