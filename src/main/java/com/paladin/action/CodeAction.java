package com.paladin.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.paladin.bean.BaseBlog;
import com.paladin.bean.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Strings;
import com.paladin.bean.Code;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

public class CodeAction extends BaseAction {
    private static final Log log = LogFactory.getLog(CodeAction.class);

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
     * 代码列表
     *
     * @param _reqCtxt
     * @return
     */
    public void list(final RequestContext _reqCtxt) {
        log.info("get code list.");
        final HttpServletRequest request = _reqCtxt.request();
        // 当前页面
        String current_page = request.getParameter("p");
        if (Strings.isNullOrEmpty(current_page))
            current_page = "1";
        int page_NO = Integer.parseInt(current_page);
        // 获取博客条数
        String sql = "SELECT COUNT(*) COUNT FROM CODE";
        int total_page = (int) (QueryHelper.stat(sql) + Constants.NUM_PER_PAGE - 1) / Constants.NUM_PER_PAGE;
        page_NO = page_NO < 1 ? 1 : page_NO;
        page_NO = page_NO > total_page ? total_page : page_NO;
        // 获取页面数据
        sql = "SELECT * FROM CODE ORDER BY HITS DESC, CREATE_DATE DESC";
        List<BaseBlog> codes = QueryHelper.query_slice(BaseBlog.class, sql, page_NO, Constants.NUM_PER_PAGE, new Object[]{});
        // 计算显示的页码数
        int p_start = page_NO - 5 > 0 ? page_NO - 5 : 1;
        int p_end = p_start + 10 > total_page ? total_page : p_start + 10;

        request.setAttribute("p_start", p_start);
        request.setAttribute("p_end", p_end);
        request.setAttribute("curr_page", page_NO);
        request.setAttribute("total_page", total_page);
        request.setAttribute("codes", codes);
        forward(_reqCtxt, "/html/code/code_list.jsp");
    }

    /**
     * 打开代码详细内容
     */
    public void read(final RequestContext _reqCtxt, final long _id) throws ServletException, IOException {
        log.info("get code detail, the id = " + _id);

        String sql = "SELECT * FROM CODE WHERE ID = ?";
        Code code = QueryHelper.read(Code.class, sql, new Object[]{_id});
        final HttpServletRequest request = _reqCtxt.request();
        request.setAttribute("title", code.getTitle());
        request.setAttribute("tags", Strings.isNullOrEmpty(code.getTag()) ? null : code.getTag().split(","));

        if (!Strings.isNullOrEmpty(request.getParameter("q"))) {
            String q = request.getParameter("q");
            q = Tools.ISO885912UTF8(q).replaceAll("<[^>]*>", "");
            String title = code.getTitle().trim();
            if (title.indexOf(q) >= 0) {
                title = title.replaceAll(q, "<span style='background-color:#f00;'>" + q + "</span>");
            }
            code.setTitle(title);
            request.setAttribute("q", q);
        }
        request.setAttribute("code", code);
        // --------------------------------hits++
        sql = "UPDATE CODE SET HITS = (HITS + 1) WHERE ID = ?";
        QueryHelper.update(sql, _id);
        forward(_reqCtxt, "/html/code/code_read.jsp");
    }

    /**
     * 转到编辑页面
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        if (super.getUserFromSession(_reqCtxt) == null)
            redirect(_reqCtxt, "/login?r=/code/edit/" + _id);
        log.info("get read to edit code-" + _id);
        String sql = "SELECT * FROM CODE WHERE ID = ?";
        Code code = QueryHelper.read(Code.class, sql, new Object[]{_id});
        _reqCtxt.request().setAttribute("code", code);
        forward(_reqCtxt, "/html/code/code_edit.jsp");
    }

    /**
     * 转到添加代码页面
     *
     * @return
     */
    public void toAdd(final RequestContext _reqCtxt) {
        log.info("to add a new code");
        forward(_reqCtxt, "/html/code/code_edit.jsp");
    }

    /**
     * 保存代码(新增或者修改)
     */
    public void save(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        String id = request.getParameter("id");
        String title = request.getParameter("title").trim();
        // 对于长字符串，一定要用StringBuilder，否则调试起来太费劲了！
        StringBuilder content = new StringBuilder();
        content.append(request.getParameter("content").trim());
        String tag = Tools.checkTag(request.getParameter("tag").trim());
        String language = request.getParameter("language").trim();

        if (Strings.isNullOrEmpty(id)) {// 添加新代码
            String sql = "INSERT INTO CODE(TITLE, CONTENT, AUTHOR, CREATE_DATE, TAG, LANGUAGE, HITS) VALUES(?, ?, ?, now(), ?, ?, 1)";
            QueryHelper.update(sql, new Object[]{title, content.toString(), "erhu", tag, language});
            log.info("add new code success");
            redirect(_reqCtxt, "/code");
        } else {// 修改代码
            String sql = "UPDATE CODE SET TITLE = ?, CONTENT = ?, TAG = ?, LASTMODIFY_DATE = NOW(), LANGUAGE = ? WHERE ID = ?";
            QueryHelper.update(sql, new Object[]{title, content.toString(), tag, language, id});
            log.info("update code success");
            redirect(_reqCtxt, "/code/read/" + id);
        }
    }

    /**
     * 删除代码
     */
    public void del(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        String id = request.getParameter("id");
        if (Strings.isNullOrEmpty(id)) {
            log.info("the code's id is null when del.");
            redirect(_reqCtxt, "/code");
        }
        log.info("delete code-" + id);
        String sql = "DELETE FROM CODE WHERE ID = ?";
        QueryHelper.update(sql, new Object[]{id});
        redirect(_reqCtxt, "/code");
    }
}
