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
import com.paladin.bean.BaseBlog;
import com.paladin.bean.Blog;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.DBManager;
import com.paladin.sys.db.QueryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Blog Action
 *
 * @author Erhu
 * @version 2.0 4th March, 2011
 */
public class BlogAction extends BaseAction {

    private static final Log log = LogFactory.getLog(BlogAction.class);

    /**
     * 默认页面
     */
    public void index(final RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * 博文列表
     */
    public void list(final RequestContext _reqCtxt) {
        log.info("get blog list.");
        // 分页
        super.doPage(_reqCtxt.request(), "SELECT COUNT(*) COUNT FROM BLOG");
        // 获取页面数据
        String sql = "SELECT * FROM BLOG ORDER BY TOP DESC, CREATE_DATE DESC";
        List<BaseBlog> blogs = QueryHelper.query_slice(BaseBlog.class, sql, page_NO, Constants.NUM_PER_PAGE, new Object[]{});

        _reqCtxt.request().setAttribute("blogs", blogs);
        _reqCtxt.request().setAttribute("hotTag", hotTag().subList(0, 15));// 提取热门tag
        forward(_reqCtxt, "/html/blog/blog_list.jsp");
    }

    /**
     * 打开文章详细内容
     */
    public void read(final RequestContext _reqCtxt, final long _id) throws ServletException, IOException {
        log.info("get blog detail, the id = " + _id);

        Blog blog = QueryHelper.read(Blog.class, "SELECT * FROM BLOG WHERE ID = ?", _id);
        final HttpServletRequest request = _reqCtxt.request();
        request.setAttribute("title", blog.getTitle());
        request.setAttribute("tags", Strings.isNullOrEmpty(blog.getTag()) ? null : blog.getTag().split(","));

        if (!Strings.isNullOrEmpty(request.getParameter("q"))) {
            String q = Tools.ISO885912UTF8(request.getParameter("q"));
            request.setAttribute("title", blog.getTitle());
            dealBlogWhenQ(blog, q);
            request.setAttribute("q", q);
        }
        request.setAttribute("blog", blog);
        // --------------------------------hits++
        QueryHelper.update("UPDATE BLOG SET HITS = (HITS + 1) WHERE ID = ?", _id);
        forward(_reqCtxt, "/html/blog/blog_read.jsp");
    }

    /**
     * 转到编辑页面
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        if (super.getUserFromSession(_reqCtxt) == null) {
            redirect(_reqCtxt, "/login?r=/blog/edit/" + _id);
            return;
        }
        log.info("get read to edit blog-" + _id);
        String sql = "SELECT * FROM BLOG WHERE ID = ?";
        Blog blog = QueryHelper.read(Blog.class, sql, new Object[]{_id});
        _reqCtxt.request().setAttribute("blog", blog);
        forward(_reqCtxt, "/html/blog/blog_edit.jsp");
    }

    /**
     * 转到添加博文页面
     */
    public void toAdd(final RequestContext _reqCtxt) {
        if (super.getUserFromSession(_reqCtxt) == null) {
            redirect(_reqCtxt, "/login?r=/blog/toAdd");
            return;
        }
        log.info("to add a new blog");
        forward(_reqCtxt, "/html/blog/blog_edit.jsp");
    }

    /**
     * 保存文章(新增或者修改)
     */
    public void save(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        String id = request.getParameter("id");
        String title = request.getParameter("title").trim();
        // 对于长字符串，一定要用StringBuilder，否则调试起来太费劲了！
        StringBuilder content = new StringBuilder();
        content.append(request.getParameter("content").trim());
        String tag = Tools.checkTag(request.getParameter("tag").trim());
        String top = (String) request.getParameter("top");
        top = top == null ? "0" : top;

        if (Strings.isNullOrEmpty(id)) {// 添加新文章
            String sql = "INSERT INTO BLOG(TITLE, CONTENT, AUTHOR, CREATE_DATE, LASTMODIFY_DATE, TAG, HITS, TOP) VALUES(?, ?, ?, now(), now(), ?, 1, ?)";
            QueryHelper.update(sql, new Object[]{title, content.toString(), "erhu", tag, top});
            log.info("Add blog success");
            redirect(_reqCtxt, "/blog");
        } else {// 修改文章
            String sql = "UPDATE BLOG SET TITLE = ?, CONTENT = ?, TAG = ?, LASTMODIFY_DATE = NOW(), TOP = ? WHERE ID = ?";
            QueryHelper.update(sql, new Object[]{title, content.toString(), tag, top, id});
            log.info("Update blog success");
            redirect(_reqCtxt, "/blog/read/" + id);
        }
    }

    /**
     * 删除博文
     */
    public void del(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        if (_reqCtxt.sessionAttr("user") == null)
            forward(_reqCtxt, "/login");

        String id = request.getParameter("id");
        if (Strings.isNullOrEmpty(id)) {
            log.info("the blog's id is null when del.");
            redirect(_reqCtxt, "/blog");
        }

        log.info("delete blog-" + id);
        String sql = "DELETE FROM BLOG WHERE ID = ?";
        QueryHelper.update(sql, new Object[]{id});
        redirect(_reqCtxt, "/blog");
    }

    /**
     * 搜索时给关键字作标记
     */
    private void dealBlogWhenQ(Blog _blog, String _q) {
        //_q = Tools.ISO885912UTF8(_q).replaceAll("<[^>]*>", "");
        _q = _q.replaceAll("<[^>]*>", "");
        String title = _blog.getTitle().trim();
        if (title.indexOf(_q) >= 0)
            title = title.replaceAll(_q, Tools.standOutStr(_q));
        _blog.setTitle(title);
        String content = _blog.getContent();
        _blog.setContent(content.replace(_q, Tools.standOutStr(_q)));
    }

    /**
     * 获取热门tag
     */
    private List<String> hotTag() {
        // 从数据库中取出所有tag
        String sql = "SELECT TAG FROM BLOG";
        List<Map<String, Object>> list = QueryHelper.queryList(sql);
        Map<String, Integer> tag_map = new HashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String tags = list.get(i).get("TAG").toString();
            if (!Strings.isNullOrEmpty(tags)) {
                for (String tag : tags.split(","))
                    tag_map.put(tag, tag_map.get(tag) == null ? 1 : tag_map.get(tag) + 1);
            }
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

    public void init(ServletContext _ctxt) {
        super.init(_ctxt);
        DBManager.getConnection();
        DBManager.closeConnection();
    }
}
