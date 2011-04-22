package com.paladin.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;
import com.paladin.common.Constants;
import com.paladin.sys.db.QueryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paladin.bean.User;
import com.paladin.mvc.RequestContext;
import sun.misc.Request;

/**
 * base action
 *
 * @author Erhu
 * @since Mar 9th, 2011
 */
public abstract class BaseAction {

    public static Log log = LogFactory.getLog(BaseAction.class);
    protected int total_page;
    protected int page_NO;
    protected int p_end;
    protected int p_start;

    protected abstract void index(final RequestContext _reqCtxt);

    /**
     * 用sql分页
     */
    protected void doPage(HttpServletRequest request, final String _sql, Object... _para) {
        page_NO = Integer.parseInt(getCurrentPage(request));
        total_page = (int) (QueryHelper.stat(_sql, _para) + Constants.NUM_PER_PAGE - 1) / Constants.NUM_PER_PAGE;
        page_NO = page_NO < 1 ? 1 : page_NO;
        page_NO = page_NO > total_page ? total_page : page_NO;
        // 计算显示的页码数
        p_start = page_NO - 5 > 0 ? page_NO - 5 : 1;
        p_end = p_start + 10 > total_page ? total_page : p_start + 10;
        request.setAttribute("p_start", p_start);
        request.setAttribute("p_end", p_end);
        request.setAttribute("curr_page", page_NO);
        request.setAttribute("total_page", total_page);
    }

    /**
     * 用list分页
     */
    protected void doPage(HttpServletRequest request, final int size, final int _num_per_page) {
        page_NO = Integer.parseInt(getCurrentPage(request));
        total_page = (size + _num_per_page - 1) / _num_per_page;
        page_NO = page_NO < 1 ? 1 : page_NO;
        page_NO = page_NO > total_page ? total_page : page_NO;
        // 计算显示的页码数
        p_start = page_NO - 5 > 0 ? page_NO - 5 : 1;
        p_end = p_start + 10 > total_page ? total_page : p_start + 10;
        request.setAttribute("p_start", p_start);
        request.setAttribute("p_end", p_end);
        request.setAttribute("curr_page", page_NO);
        request.setAttribute("total_page", total_page);
    }

    protected User getUserFromSession(final RequestContext _reqCtxt) {
        return (User) _reqCtxt.sessionAttr("user");
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
     * 取得当前页
     */
    protected static String getCurrentPage(final HttpServletRequest request) {
        String current_page = request.getParameter("p");
        if (Strings.isNullOrEmpty(current_page))
            current_page = "1";
        return current_page;
    }

    /**
     * init
     */
    public void init(ServletContext _ctxt) {
        log.info(this.getClass().getName() + " init...");
    }
}
