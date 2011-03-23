package com.paladin.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Strings;
import com.paladin.bean.Blog;
import com.paladin.bean.Code;
import com.paladin.bean.HFile;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

/**
 * Serache Action
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
	 * 
	 * @param _reqCtxt
	 * @throws UnsupportedEncodingException
	 */
	public void bc(final RequestContext _reqCtxt) throws UnsupportedEncodingException {
		List<Blog> blog_list = new ArrayList<Blog>();
		List<Code> code_list = new ArrayList<Code>();
		HttpServletRequest request = _reqCtxt.request();
		String q = request.getParameter("q");
		if (!Strings.isNullOrEmpty(q)) {
			q = new String(q.getBytes("ISO-8859-1"), "utf-8");
			request.setAttribute("q", q);

			q = q.replaceAll("<[^>]*>", "");
			q = Tools.compressBlank(q);

			String[] q_arr = q.split(" ");
			for (int i = 0; i < q_arr.length; i++)
				q_arr[i] = "%".concat(q_arr[i]).concat("%");

			// search from blog
			StringBuilder sqlB = new StringBuilder();
			sqlB.append("SELECT * FROM BLOG WHERE TITLE LIKE ? OR CONTENT LIKE ? ORDER BY HITS DESC");

			for (String qq : q_arr) {
				for (Blog b : QueryHelper.query(Blog.class, sqlB.toString(), new Object[] { qq, qq })) {
					if (!blog_list.contains(b)) {
						String title = b.getTitle().trim();
						if (title.indexOf(q) >= 0)
							title = title.replaceAll(q, "<span style='background-color:#f00;'>" + q
									+ "</span>");
						b.setTitle(title);

						String content = b.getContent();
						content = content.replaceAll("<[^>]*>", "");
						int first_index = content.indexOf(q);
						int last_index = content.lastIndexOf(q);
						if (first_index >= 0 && content.length() >= last_index + q.length() + 1)
							content = content.substring(first_index, last_index + q.length() + 1);
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
				for (Code c : QueryHelper.query(Code.class, sqlB.toString(), new Object[] { qq, qq, qq })) {
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
						if (first_index >= 0 && content.length() >= last_index + q.length() + 1)
							content = content.substring(first_index, last_index + q.length() + 1);
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
	 * search file
	 * 
	 * @param _reqCtxt
	 * @throws UnsupportedEncodingException
	 */
	public void f(final RequestContext _reqCtxt) throws UnsupportedEncodingException {
		List<HFile> file_list = new ArrayList<HFile>();
		HttpServletRequest request = _reqCtxt.request();
		String q = request.getParameter("q");
		if (!Strings.isNullOrEmpty(q)) {
			q = new String(q.getBytes("ISO-8859-1"), "utf-8");
			request.setAttribute("q", q);
			q = q.replaceAll("<[^>]*>", "");
			q = Tools.compressBlank(q);

			String[] q_arr = q.split(" ");
			for (int i = 0; i < q_arr.length; i++)
				q_arr[i] = "%".concat(q_arr[i]).concat("%");
			StringBuilder sqlB = new StringBuilder();
			// search from local file system
			sqlB.append("SELECT * FROM HFILE WHERE FILENAME LIKE ? LIMIT 1000");
			for (String qq : q_arr) {
				for (HFile f : QueryHelper.query(HFile.class, sqlB.toString(), new Object[] { qq })) {
					if (!file_list.contains(f)) {
						f.setFileName(f.getFileName().replace(q,
								"<span style='background-color:#f00;'>" + q + "</span>"));
						file_list.add(f);
					}
				}
			}
		}
		log.info("q = " + q);
		log.info("get file:" + file_list.size());
		request.setAttribute("file_list", file_list);
		forward(_reqCtxt, "/html/search/search_f.jsp");
	}
}
