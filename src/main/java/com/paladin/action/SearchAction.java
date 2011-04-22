package com.paladin.action;

import com.google.common.base.Strings;
import com.paladin.bean.Blog;
import com.paladin.bean.Code;
import com.paladin.bean.HFile;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search Action
 *
 * @author Erhu
 * @since Mar 12th, 2011
 */
public class SearchAction extends BaseAction {

    private static Log log = LogFactory.getLog(SearchAction.class);

    public void index(final RequestContext _reqCtxt) {
        redirect(_reqCtxt, "/blog");
    }

    /**
     * Search blog and code
     */
    public void bc(final RequestContext _reqCtxt) throws UnsupportedEncodingException {
        List<Blog> blog_list = new ArrayList<Blog>();
        List<Code> code_list = new ArrayList<Code>();
        HttpServletRequest request = _reqCtxt.request();
        String q = request.getParameter("q");
        if (!Strings.isNullOrEmpty(q)) {
            q = Tools.ISO885912UTF8(q).trim();
            request.setAttribute("q", q);

            q = q.replaceAll("<[^>]*>", "");
            q = Tools.compressBlank(q);

            String[] q_arr = q.split(" ");
            for (int i = 0; i < q_arr.length; i++)
                q_arr[i] = "%".concat(q_arr[i]).concat("%");

            // search from blog
            StringBuilder sqlB = new StringBuilder();
            sqlB.append("SELECT * FROM BLOG WHERE TITLE LIKE ? OR CONTENT LIKE ? OR TAG LIKE ? ORDER BY HITS DESC");

            for (String qq : q_arr) {
                for (Blog b : QueryHelper.query(Blog.class, sqlB.toString(), new Object[]{qq, qq, qq})) {
                    if (!blog_list.contains(b)) {
                        String title = b.getTitle().trim();
                        if (title.indexOf(q) >= 0) {
                            title = title.replaceAll(q, "<span style='background-color:#f00;'>" + q
                                    + "</span>");
                            b.setTitle(title);
                        }
                        String tag = b.getTag().trim();
                        if (tag.indexOf(q) >= 0) {
                            tag = tag.replaceAll(q, "<span style='background-color:#f00;'>" + q
                                    + "</span>");
                            b.setTag(tag);
                        }

                        String content = b.getContent();
                        content = content.replaceAll("<[^>]*>", "");
                        int first_index = content.indexOf(q);
                        int last_index = content.lastIndexOf(q);
                        if (first_index >= 0 && content.length() >= last_index + q.length() + 20)
                            content = content.substring(first_index, last_index + q.length() + 20);
                        if (content.length() > 1200)
                            content = content.substring(0, 1200);
                        b.setContent(content.replace(q, "<span style='background-color:#f00;'>" + q
                                + "</span>"));
                        blog_list.add(b);
                    }
                }
            }
            sqlB.setLength(0);
            // search form code
            sqlB.append("SELECT * FROM CODE WHERE TITLE LIKE ? OR CONTENT LIKE ? OR TAG LIKE ? ORDER BY HITS DESC");
            for (String qq : q_arr) {
                for (Code c : QueryHelper.query(Code.class, sqlB.toString(), new Object[]{qq, qq, qq})) {
                    if (!code_list.contains(c)) {
                        String title = c.getTitle().trim();
                        if (title.indexOf(q) >= 0)
                            title = title.replaceAll(q, "<span style='background-color:#f00;'>" + q
                                    + "</span>");
                        c.setTitle(title);

                        String content = c.getContent();
                        content = content.replaceAll("<[^>]*>", "");
                        int first_index = content.indexOf(q);
                        int last_index = content.lastIndexOf(q);
                        if (first_index >= 0 && content.length() >= last_index + q.length() + 20)
                            content = content.substring(first_index, last_index + q.length() + 20);
                        if (content.length() > 1200)
                            content = content.substring(0, 1200);
                        c.setContent(content.replace(q, "<span style='background-color:#f00;'>" + q
                                + "</span>"));
                        code_list.add(c);
                    }
                }
            }
        }
        log.info("q = " + q);
        log.info("get blog:" + blog_list.size());
        log.info("get code:" + code_list.size());

        request.setAttribute("blog_list", blog_list);
        request.setAttribute("code_list", code_list);
        forward(_reqCtxt, "/html/search/search_bc.jsp");
    }

    /**
     * Search blog
     */
    public void b(final RequestContext _reqCtxt) throws UnsupportedEncodingException {
        List<Blog> blog_list = new ArrayList<Blog>();
        HttpServletRequest request = _reqCtxt.request();
        String q = request.getParameter("q");
        if (!Strings.isNullOrEmpty(q)) {
            q = Tools.ISO885912UTF8(q).trim();
            request.setAttribute("q", q);
            // search from blog

            for (String qq : Tools.q2qArr(q)) {
                String sql = "SELECT * FROM BLOG WHERE TITLE LIKE ? OR CONTENT LIKE ? OR TAG LIKE ? ORDER BY HITS DESC";
                for (Blog b : QueryHelper.query(Blog.class, sql, new Object[]{qq, qq, qq})) {
                    if (!blog_list.contains(b)) {
                        String title = b.getTitle().trim();
                        String tag = b.getTag().trim();
                        String content = b.getContent().trim();
                        if (title.indexOf(q) >= 0)
                            b.setTitle(title.replaceAll(q, Tools.standOutStr(q)));
                        if (tag.indexOf(q) >= 0)
                            b.setTag(tag.replaceAll(q, Tools.standOutStr(q)));

                        content = content.replaceAll("<[^>]*>", "");
                        int first_index = content.indexOf(q);
                        int last_index = content.lastIndexOf(q);
                        if (first_index >= 0 && content.length() >= last_index + q.length() + 20)
                            content = content.substring(first_index, last_index + q.length() + 20);
                        if (content.length() > 1200)
                            content = content.substring(0, 1200);
                        b.setContent(content.replace(q, Tools.standOutStr(q)));
                        blog_list.add(b);
                    }
                }
            }
            {//分页
                int num_per_page = 5;// 每页显示5条搜索结果
                super.doPage(request, blog_list.size(), num_per_page);
                int begin = (page_NO - 1) * 5;
                int end = page_NO * num_per_page > blog_list.size() ? blog_list.size() : page_NO * num_per_page;
                blog_list = blog_list.subList(begin, end);
            }
        }
        log.info("q = " + q);
        log.info("get blog:" + blog_list.size());
        request.setAttribute("blog_list", blog_list);
        forward(_reqCtxt, "/html/search/search_b.jsp");
    }

    /**
     * search file
     */
    public void f(final RequestContext _reqCtxt) throws UnsupportedEncodingException {
        List<HFile> file_list = new ArrayList<HFile>();
        HttpServletRequest request = _reqCtxt.request();
        String q = request.getParameter("q");
        if (!Strings.isNullOrEmpty(q)) {
            q = Tools.ISO885912UTF8(q);
            request.setAttribute("q", q);
            String[] q_arr = Tools.q2qArr(q);
            // search from local file system
            String sql = "SELECT * FROM HFILE WHERE FILENAME LIKE ? LIMIT 1000";
            for (String qq : q_arr) {
                for (HFile f : QueryHelper.query(HFile.class, sql, new Object[]{qq}))
                    if (!file_list.contains(f)) {
                        f.setFileName(f.getFileName().replace(q, Tools.standOutStr(q)));
                        file_list.add(f);
                    }
            }
        }
        log.info("q = " + q);
        log.info("get file:" + file_list.size());
        request.setAttribute("file_list", file_list);
        forward(_reqCtxt, "/html/search/search_f.jsp");
    }
}
