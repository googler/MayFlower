package com.paladin.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
		String current_page = request.getParameter("c_page");
		if (Tools.isNullString(current_page))
			current_page = "1";
		int page_num = Integer.parseInt(current_page);

		// 获取页面数据
		String sql = "SELECT * FROM CODE ORDER BY CREATE_DATE DESC";
		request.setAttribute("codes",
				QueryHelper.query_slice(Code.class, sql, page_num, Constants.NUM_PER_PAGE, new Object[] {}));
		forward(_reqCtxt, "/html/code/code_list.jsp");
	}

	/**
	 * 打开代码详细内容
	 */
	public void read(final RequestContext _reqCtxt, final long _id) throws ServletException, IOException {
		log.info("get code detail, the id = " + _id);

		String sql = "SELECT * FROM CODE WHERE ID = ?";
		Code code = QueryHelper.read(Code.class, sql, new Object[] { _id });
		final HttpServletRequest request = _reqCtxt.request();
		request.setAttribute("title", code.getTitle());
		request.setAttribute("tags", code.getTag().split(","));

		if (Strings.isNullOrEmpty(request.getParameter("q"))) {
			String q = request.getParameter("q");
			q = new String(q.getBytes("ISO-8859-1"), "UTF-8").replaceAll("<[^>]*>", "");
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
		log.info("get read to edit code-" + _id);

		String sql = "SELECT * FROM CODE WHERE ID = ?";
		Code code = QueryHelper.read(Code.class, sql, new Object[] { _id });
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

		if (Tools.isNullString(id)) {// 添加新代码
			String sql = "INSERT INTO CODE(TITLE, CONTENT, AUTHOR, CREATE_DATE, TAG, LANGUAGE, HITS) VALUES(?, ?, ?, now(), ?, ?, 1)";
			QueryHelper.update(sql, new Object[] { title, content.toString(), "erhu", tag, language });
			log.info("add new code success");
			redirect(_reqCtxt, "/code");
		} else {// 修改代码
			String sql = "UPDATE CODE SET TITLE = ?, CONTENT = ?, TAG = ?, LASTMODIFY_DATE = NOW(), LANGUAGE = ? WHERE ID = ?";
			QueryHelper.update(sql, new Object[] { title, content.toString(), tag, language, id });
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
		log.info("delete code-" + id);
		String sql = "DELETE FROM CODE WHERE ID = ?";
		QueryHelper.update(sql, new Object[] { id });
		redirect(_reqCtxt, "/code");
	}
}
