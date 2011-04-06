package com.paladin.action;

import com.google.common.base.Strings;
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
     *
     * @param _reqCtxt
     * @return
     */
    public void index(final RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * 博文列表
     *
     * @param _reqCtxt
     * @return
     */
    public void list(final RequestContext _reqCtxt) {
        log.info("get blog list.");
        final HttpServletRequest request = _reqCtxt.request();
        // 当前页面
        String current_page = request.getParameter("c_page");
        if (Strings.isNullOrEmpty(current_page))
            current_page = "1";
        int page_num = Integer.parseInt(current_page);

        // 获取页面数据
        String sql = "SELECT * FROM BLOG ORDER BY TOP DESC, LASTMODIFY_DATE DESC, CREATE_DATE DESC";
        request.setAttribute("blogs",
                QueryHelper.query_slice(Blog.class, sql, page_num, Constants.NUM_PER_PAGE, new Object[]{}));
        forward(_reqCtxt, "/html/blog/blog_list.jsp");
    }

    /**
     * 打开文章详细内容
     */
    public void read(final RequestContext _reqCtxt, final long _id) throws ServletException, IOException {
        log.info("get blog detail, the id = " + _id);

        String sql = "SELECT * FROM BLOG WHERE ID = ?";
        Blog blog = QueryHelper.read(Blog.class, sql, _id);
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
        sql = "UPDATE BLOG SET HITS = (HITS + 1) WHERE ID = ?";
        QueryHelper.update(sql, _id);
        forward(_reqCtxt, "/html/blog/blog_read.jsp");
    }

    /**
     * 转到编辑页面
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        log.info("get read to edit blog-" + _id);

        String sql = "SELECT * FROM BLOG WHERE ID = ?";
        Blog blog = QueryHelper.read(Blog.class, sql, new Object[]{_id});
        _reqCtxt.request().setAttribute("blog", blog);
        forward(_reqCtxt, "/html/blog/blog_edit.jsp");
    }

    /**
     * 转到添加博文页面
     *
     * @return
     */
    public void toAdd(final RequestContext _reqCtxt) {
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
            String sql = "INSERT INTO BLOG(TITLE, CONTENT, AUTHOR, CREATE_DATE, TAG, HITS, TOP) VALUES(?, ?, ?, now(), ?, 1, ?)";
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
        log.info("delete blog-" + id);
        String sql = "DELETE FROM BLOG WHERE ID = ?";
        QueryHelper.update(sql, new Object[]{id});
        redirect(_reqCtxt, "/blog");
    }

    private void dealBlogWhenQ(Blog _blog, String _q) {
        //_q = Tools.ISO885912UTF8(_q).replaceAll("<[^>]*>", "");
        _q = _q.replaceAll("<[^>]*>", "");
        String title = _blog.getTitle().trim();
        if (title.indexOf(_q) >= 0) {
            title = title.replaceAll(_q, "<span style='background-color:#f00;'>" + _q + "</span>");
        }
        _blog.setTitle(title);
        String content = _blog.getContent();
        _blog.setContent(content.replace(_q, "<span style='background-color:#f00;'>" + _q + "</span>"));
    }

    /**
     * init
     *
     * @param _ctxt
     */
    public void init(ServletContext _ctxt) {
        super.init(_ctxt);
        try {
            DBManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBManager.closeConnection();
    }
}
