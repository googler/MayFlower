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

import com.google.common.base.Strings;
import com.paladin.bean.User;
import com.paladin.common.Constants;
import com.paladin.common.LuceneHelper;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Base action
 *
 * @author Erhu
 * @since Mar 9th, 2011
 */
public abstract class BaseAction {

    protected static Log log;
    protected int total_page;// 总页数
    protected int page_NO;// 页号
    protected int p_end;// 最后页(用于 分屏)
    protected int p_start;// 最前页(用于 分屏)

    protected BaseAction() {
        log = LogFactory.getLog(this.getClass());
    }

    protected abstract void index(final RequestContext _reqCtxt);

    /**
     * 用sql分页
     *
     * @param request
     * @param _sql
     * @param _para
     */
    protected void doPage(HttpServletRequest request, final String _sql, final String _type, Object... _para) {
        total_page = (int) (QueryHelper.stat(_sql, _para) + Constants.NUM_PER_PAGE - 1) / Constants.NUM_PER_PAGE;
        _doPage(request, _type);
    }

    /**
     * 用list分页
     *
     * @param request
     * @param size
     * @param _num_per_page
     */
    protected void doPage(HttpServletRequest request, final int size, final int _num_per_page, final String _type) {
        request.setAttribute("total_count" + _type, size);
        total_page = (size + _num_per_page - 1) / _num_per_page;
        _doPage(request, _type);
    }

    protected void _doPage(HttpServletRequest request, final String _type) {
        page_NO = Integer.parseInt(getCurrentPage(request));
        page_NO = page_NO < 1 ? 1 : page_NO;
        page_NO = page_NO > total_page ? total_page : page_NO;
        // 计算显示的页码数
        p_start = page_NO - 5 > 0 ? page_NO - 5 : 1;
        p_end = p_start + 10 > total_page ? total_page : p_start + 10;
        request.setAttribute("p_start" + _type, p_start);
        request.setAttribute("p_end" + _type, p_end);
        request.setAttribute("curr_page" + _type, page_NO);
        request.setAttribute("total_page" + _type, total_page);
    }

    /**
     * 从session中获取用户
     *
     * @param _reqCtxt
     * @return
     */
    protected User getUserFromSession(final RequestContext _reqCtxt) {
        return (User) _reqCtxt.sessionAttr("user");
    }

    /**
     * redirect 跳转
     *
     * @param _reqCtxt
     * @param _uri
     */
    protected void redirect(final RequestContext _reqCtxt, final String _uri) {
        try {
            _reqCtxt.response().sendRedirect(_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * forward 跳转
     *
     * @param _reqCtxt
     * @param _uri
     */
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
     * 取得当前页码
     *
     * @param request
     * @return
     */
    protected static String getCurrentPage(final HttpServletRequest request) {
        String current_page = request.getParameter("p");
        if (Strings.isNullOrEmpty(current_page))
            current_page = "1";
        return current_page;
    }

    /**
     * 转到添加页面
     *
     * @param _reqCtxt
     * @param _table
     */
    protected void toAdd(final RequestContext _reqCtxt, String _table) {
        _table = _table.toLowerCase();
        if (getUserFromSession(_reqCtxt) == null) {
            redirect(_reqCtxt, "/login?r=/" + _table + "/toAdd");
            return;
        }
        log.info("to add a new " + _table);
        forward(_reqCtxt, "/html/" + _table + "/" + _table + "_edit.jsp");
    }

    /**
     * 转到编辑页面
     *
     * @param _reqCtxt
     * @param _id
     * @param _class
     */
    public void edit(final RequestContext _reqCtxt, final long _id, Class _class) {
        // 根据类的完整名称类除去包的名称
        String _bean_L = _class.getName().toLowerCase();
        if (_bean_L.indexOf('.') > 0)
            _bean_L = _bean_L.substring(_bean_L.lastIndexOf('.') + 1, _bean_L.length());
        final String _bean_U = _bean_L.toUpperCase();

        if (getUserFromSession(_reqCtxt) == null) {
            redirect(_reqCtxt, "/login?r=/" + _bean_L + "/edit/" + _id);
            return;
        }
        log.info("get read to edit " + _bean_L + " - " + _id);
        String sql = "SELECT * FROM " + _bean_U + " WHERE ID = ?";
        _reqCtxt.request().setAttribute(_bean_L, QueryHelper.read(_class, sql, new Object[]{_id}));
        forward(_reqCtxt, "/html/" + _bean_L + "/" + _bean_L + "_edit.jsp");
    }

    /**
     * 删除操作
     *
     * @param _reqCtxt
     * @param _table
     */
    protected void del(final RequestContext _reqCtxt, final String _table) {
        final HttpServletRequest request = _reqCtxt.request();
        if (_reqCtxt.sessionAttr("user") == null)
            forward(_reqCtxt, "/login");

        String id = request.getParameter("id");
        if (Strings.isNullOrEmpty(id)) {
            log.info("the " + _table + "'s id is null when del.");
            redirect(_reqCtxt, "/" + _table.toLowerCase());
        }

        log.info("delete " + _table + " - " + id);
        String sql = "DELETE FROM " + _table.toUpperCase() + " WHERE ID = ?";
        QueryHelper.update(sql, new Object[]{id});
        redirect(_reqCtxt, "/" + _table.toLowerCase());
    }

    /**
     * 获取热门tag
     *
     * @param _table table
     * @return hot tag
     */
    protected List<String> hotTag(String _table) {
        // 从数据库中取出所有tag
        String sql = "SELECT TAG FROM " + _table.toUpperCase();
        List<Map<String, Object>> list = QueryHelper.queryList(sql);
        Map<String, Integer> tag_map = new HashMap<String, Integer>();
        for (Map<String, Object> map : list) {
            String tags = map.get("TAG").toString();
            if (!Strings.isNullOrEmpty(tags))
                for (String tag : tags.split(","))
                    tag_map.put(tag, tag_map.get(tag) == null ? 1 : tag_map.get(tag) + 1);
        }
        // 排序并返回
        String[] key_arr = new String[tag_map.keySet().size()];
        tag_map.keySet().toArray(key_arr);
        Tools.quickSort(tag_map, key_arr, 0, key_arr.length - 1);

        List<String> list_return = new ArrayList<String>();
        for (String str : key_arr)
            list_return.add(str + ":=:" + tag_map.get(str));
        return list_return;
    }

    /**
     * init
     */
    protected void init(ServletContext _ctxt) {
        log.info(this.getClass().getName() + " init...");
    }
}
