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
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Blog Action
 *
 * @author Erhu
 * @version 2.0 4th March, 2011
 */
public class BlogAction extends BaseAction {
    /**
     * 默认页面
     */
    @Override
    public void index(final RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * 博文列表
     *
     * @param _reqCtxt life is good :)
     */
    public void list(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        log.info("get blog list.");
        super.doPage(request, "SELECT COUNT(*) COUNT FROM BLOG", "");// 分页
        // 获取页面数据
        String sql = "SELECT * FROM BLOG ORDER BY TOP DESC, CREATE_DATE DESC";
        List<BaseBlog> blogs = QueryHelper.query_slice(BaseBlog.class, sql, page_NO, Constants.NUM_PER_PAGE);
        // -------------------------------------------------------------------------------------------------------------
        request.setAttribute("blogs", blogs);
        request.setAttribute("hotTag", super.hotTag("BLOG").subList(0, 15));// 提取热门tag
        forward(_reqCtxt, "/html/blog/blog_list.jsp");
    }

    /**
     * Just read a blog
     *
     * @param _reqCtxt request context
     * @param _id      blog's id
     * @throws ServletException u know
     * @throws IOException      u know
     */
    public void read(final RequestContext _reqCtxt, final long _id) throws ServletException, IOException {
        log.info("get blog detail, the id = " + _id);
        Blog blog = QueryHelper.read(Blog.class, "SELECT * FROM BLOG WHERE ID = ?", _id);

        if (blog != null) {
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
            // --------------------------------- hits++
            QueryHelper.update("UPDATE BLOG SET HITS = (HITS + 1) WHERE ID = ?", _id);
            forward(_reqCtxt, "/html/blog/blog_read.jsp");
        } else
            forward(_reqCtxt, "/html/error/404.jsp");
    }

    /**
     * save a new blog or update an old blog
     *
     * @param _reqCtxt life is good:-)
     */
    public void save(final RequestContext _reqCtxt) {
        String id = _reqCtxt.param("id");
        String title = _reqCtxt.param("title").trim();

        // 对于长字符串，一定要用StringBuilder，否则调试起来太费劲了！
        StringBuilder content = new StringBuilder();
        content.append(_reqCtxt.param("content"));
        String tag = Tools.checkTag(_reqCtxt.param("tag"));
        String top = _reqCtxt.param("top");
        top = top == null ? "0" : top;

        if (Strings.isNullOrEmpty(id)) {// 添加新文章
            String sql = "INSERT INTO BLOG(TITLE, CONTENT, AUTHOR, CREATE_DATE, LASTMODIFY_DATE, TAG," +
                    " HITS, TOP) VALUES(?, ?, ?, now(), now(), ?, 1, ?)";
            QueryHelper.update(sql, new String[]{title, content.toString(),
                    super.getUserFromSession(_reqCtxt).getUsername(), tag, top});

            log.info("Add blog success");
            redirect(_reqCtxt, "/blog");
        } else {// 修改文章
            String sql = "UPDATE BLOG SET TITLE = ?, CONTENT = ?, TAG = ?, LASTMODIFY_DATE = NOW(), " +
                    "TOP = ? WHERE ID = ?";
            QueryHelper.update(sql, new String[]{title, content.toString(), tag, top, id});

            log.info("Update blog success");
            redirect(_reqCtxt, "/blog/read/" + id);
        }
    }

    /**
     * redirect to the editPage to edit a blog.
     *
     * @param _reqCtxt everything is fine^-^
     * @param _id      blog's id
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        super.edit(_reqCtxt, _id, Blog.class);
    }

    /**
     * redirect to the editPage to add a new blog.
     *
     * @param _reqCtxt life is good:)
     */
    public void toAdd(final RequestContext _reqCtxt) {
        super.toAdd(_reqCtxt, "blog");
    }

    /**
     * del blog
     *
     * @param _reqCtxt (:Well, life is very good:)
     */
    public void del(final RequestContext _reqCtxt) {
        super.del(_reqCtxt, "blog");
    }

    /**
     * 为搜索结果中的关键字作标记
     *
     * @param _blog Blog
     * @param _q    question
     */
    private void dealBlogWhenQ(Blog _blog, String _q) {
        _q = _q.replaceAll("<[^>]*>", "");
        TermQuery query = new TermQuery(new Term("field", _q));

        String title = _blog.getTitle().trim();
        String f_title = Tools.highlight(query, "field", title);
        _blog.setTitle(f_title == null ? title : f_title);

        // lucene 的高亮显示有字数限制，因此不可对文章内容使用上面的方法
        String content = _blog.getContent();
        _blog.setContent(content.replace(_q, Tools.standOutStr(_q)));
    }


    /**
     * u know this.
     *
     * @param _ctxt fine day!
     */
    public void init(ServletContext _ctxt) {
        super.init(_ctxt);
        DBManager.getConnection();
        DBManager.closeConnection();
    }
}
