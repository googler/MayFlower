package com.paladin.sys.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paladin.bean.Blog;
import com.paladin.bean.Code;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.sys.db.QueryHelper;

public class CmsServlet extends HttpServlet {

	private static final long serialVersionUID = 1803960552619136114L;
	private static final Log log = LogFactory.getLog(CmsServlet.class);

	/**
	 * do Servlet
	 */
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, SQLException {
		request.setCharacterEncoding("UTF-8");
		String type = request.getParameter("type");
		String operation = request.getParameter("operation");
		if (Tools.isNullString(type) || "atcl".equalsIgnoreCase(type)) {
			doAtcl(request, response, operation);
		} else if ("code".equalsIgnoreCase(type)) {
			doCode(request, response, operation);
		}
	}

	/**
	 * do Article
	 */
	private void doAtcl(HttpServletRequest request, HttpServletResponse response, String _operation)
			throws ServletException, IOException, SQLException {
		if (Tools.isNullString(_operation) || _operation.equalsIgnoreCase("list")) {
			this.doAtclList(request, response);
		} else if (_operation.equalsIgnoreCase("add")) {
			getServletContext().getRequestDispatcher("/blog_add.jsp").forward(request, response);
		} else if (_operation.equalsIgnoreCase("save")) {
			this.doAtclSave(request, response, "cms?operation=read&type=atcl&id=");
		} else if (_operation.equalsIgnoreCase("read")) {
			this.doAtclRead(request, response);
		} else if (_operation.equalsIgnoreCase("edit")) {
			this.doAtclEdit(request, response, "/blog_edit.jsp");
		} else if (_operation.equalsIgnoreCase("search")) {
			this.doSearch(request, response);
		} else if (_operation.equalsIgnoreCase("del")) {
			this.doAtclDel(request, response);
		}
	}

	/**
	 * do Code
	 */
	private void doCode(HttpServletRequest request, HttpServletResponse response, String _operation)
			throws SQLException, IOException, ServletException {
		if (Tools.isNullString(_operation) || "list".equalsIgnoreCase(_operation)) {
			this.doCodeList(request, response);
		} else if (_operation.equalsIgnoreCase("add")) {
			getServletContext().getRequestDispatcher("/cms_code_add.jsp").forward(request, response);
		} else if (_operation.equalsIgnoreCase("save")) {
			this.doCodeSave(request, response, "cms?operation=read&type=code&id=");
		} else if (_operation.equalsIgnoreCase("read")) {
			this.doCodeRead(request, response);
		} else if (_operation.equalsIgnoreCase("edit")) {
			this.doCodeEdit(request, response, "/cms_code_edit.jsp");
		} else if (_operation.equalsIgnoreCase("del")) {
			this.doCodeDel(request, response);
		}
	}

	/**
	 * 搜索
	 * 
	 * @param request
	 * @param response
	 * @throws java.sql.SQLException
	 * @throws ServletException
	 * @throws java.io.IOException
	 */
	private void doSearch(HttpServletRequest request, HttpServletResponse response) throws SQLException,
			ServletException, IOException {
		String q = request.getParameter("q");
		if (Tools.isNullString(q)) {
			this.doAtclList(request, response);
			return;
		}
		String t_q = q.replaceAll("<[^>]*>", "");
		q = "%" + t_q + "%";
		q = q.toUpperCase();
		String sql = "SELECT * FROM ARTICLE WHERE TITLE LIKE ? OR CONTENT LIKE ?";
		List<Blog> atcl_list = QueryHelper.query(Blog.class, sql, new Object[] { q, q });
		request.setAttribute("q", t_q);
		request.setAttribute("atcls", atcl_list);
		getServletContext().getRequestDispatcher("/blog_list.jsp").forward(request, response);
	}

	/**
	 * 转到编辑页面
	 */
	private void doAtclEdit(HttpServletRequest request, HttpServletResponse response, String _url)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String sql = "SELECT * FROM ARTICLE WHERE ID = ?";
		Blog atcl = QueryHelper.read(Blog.class, sql, new Object[] { id });
		request.setAttribute("atcl", atcl);
		getServletContext().getRequestDispatcher(_url).forward(request, response);
	}

	/**
	 * 转到编辑页面
	 */
	private void doCodeEdit(HttpServletRequest request, HttpServletResponse response, String _url)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String sql = "SELECT * FROM CODE WHERE ID = ?";
		Code code = QueryHelper.read(Code.class, sql, new Object[] { id });
		request.setAttribute("code", code);
		getServletContext().getRequestDispatcher(_url).forward(request, response);
	}

	/**
	 * 打开文章详细内容
	 */
	private void doAtclRead(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String id = request.getParameter("id");
		String q = request.getParameter("q");
		if (Tools.isNullString(id))
			id = "1";
		if (!Tools.isNullString(q)) {
			q = new String(q.getBytes("ISO-8859-1"), "UTF-8").replaceAll("<[^>]*>", "");
			request.setAttribute("q", q);
		}
		String sql = "SELECT * FROM ARTICLE WHERE ID = ?";
		Blog atcl = QueryHelper.read(Blog.class, sql, new Object[] { id });
		request.setAttribute("atcl", atcl);
		getServletContext().getRequestDispatcher("/blog_read.jsp").forward(request, response);
	}

	/**
	 * 打开代码详细内容
	 */
	private void doCodeRead(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String id = request.getParameter("id");
		String q = request.getParameter("q");
		if (Tools.isNullString(id))
			id = "1";
		if (!Tools.isNullString(q)) {
			q = new String(q.getBytes("ISO-8859-1"), "UTF-8").replaceAll("<[^>]*>", "");
			request.setAttribute("q", q);
		}
		String sql = "SELECT * FROM CODE WHERE ID = ?";
		request.setAttribute("code", QueryHelper.read(Code.class, sql, new Object[] { id }));
		getServletContext().getRequestDispatcher("/cms_code_read.jsp").forward(request, response);
	}

	/**
	 * 转到文章列表页面
	 */
	private void doAtclList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String current_page = request.getParameter("c_page");
		if (Tools.isNullString(current_page))
			current_page = "1";
		int page_num = Integer.parseInt(current_page);
		String sql = "SELECT * FROM ARTICLE ORDER BY CREATE_DATE DESC";
		request.setAttribute("atcls", QueryHelper.query_slice(Blog.class, sql, page_num, Constants.NUM_PER_PAGE,
				new Object[] {}));
		getServletContext().getRequestDispatcher("/blog_list.jsp").forward(request, response);
	}

	/**
	 * 转到代码列表页面
	 */
	private void doCodeList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// 当前第几页
		String page = request.getParameter("page");
		if (Tools.isNullString(page))
			page = "1";
		int page_num = Integer.parseInt(page);
		String sql = "SELECT * FROM CODE ORDER BY CREATE_DATE DESC";
		request.setAttribute("codes", QueryHelper.query_slice(Code.class, sql, page_num, Constants.NUM_PER_PAGE,
				new Object[] {}));
		getServletContext().getRequestDispatcher("/cms_code_list.jsp").forward(request, response);
	}

	/**
	 * 保存文章(新增或者修改)
	 */
	private void doAtclSave(HttpServletRequest request, HttpServletResponse response, String _url) throws IOException {
		String id = request.getParameter("id");
		String title = request.getParameter("title").trim();
		String content = request.getParameter("content").trim();
		String tag = checkTag(request.getParameter("tag").trim(), "博文");
		if (Tools.isNullString(id)) {// 添加新文章
			String sql = "INSERT INTO ARTICLE(TITLE, CONTENT, AUTHOR, CREATE_DATE, TAG) VALUES(?, ?, ?, now(), ?)";
			QueryHelper.update(sql, new Object[] { title, content, "erhu", tag });
			response.sendRedirect("cms?operation=list&type=atcl");
		} else {// 修改文章
			String sql = "UPDATE ARTICLE SET TITLE = ?, CONTENT = ?, TAG = ?, LASTMODIFY_DATE = NOW() WHERE ID = ?";
			QueryHelper.update(sql, new Object[] { title, content, tag, id });
			response.sendRedirect(_url + id);
		}
	}

	/**
	 * 保存代码
	 */
	private void doCodeSave(HttpServletRequest request, HttpServletResponse response, String _url) throws SQLException,
			IOException {
		String id = request.getParameter("id");
		String title = request.getParameter("title").trim();
		String content = request.getParameter("content").trim();
		String tag = checkTag(request.getParameter("tag").trim(), "代码");
		String language = request.getParameter("language").trim();

		if (Tools.isNullString(id)) {// 发布新代码
			String sql = "INSERT INTO CODE(TITLE, CONTENT, AUTHOR, CREATE_DATE, TAG, LANGUAGE) VALUES(?, ?, ?, now(), ?, ?)";
			QueryHelper.update(sql, new Object[] { title, content, "erhu", tag, language });
			response.sendRedirect("cms?operation=list&type=code");
		} else {// 修改代码
			String sql = "UPDATE CODE SET TITLE = ?, CONTENT = ?, TAG = ?, LANGUAGE = ?, LASTMODIFY_DATE = NOW() WHERE ID = ?";
			QueryHelper.update(sql, new Object[] { title, content, tag, language, id });
			response.sendRedirect(_url + id);
		}
	}

	/** check tag */
	private String checkTag(String _tags, final String _type) {
		// 替换全角逗号分隔符
		_tags = _tags.replace("，", ",");
		// 删除重复的tag
		List<String> tag_list = new ArrayList<String>();
		for (String tag : _tags.split(",")) {
			String _tag = Tools.compressBlank(tag);// 压缩标签中的空格
			if (!Tools.isNullString(_tag) && !tag_list.contains(_tag))
				tag_list.add(_tag);
		}
		if (!tag_list.contains(_type))
			tag_list.add(_type);
		String tag = Arrays.toString((Object[]) tag_list.toArray());
		return tag.substring(1, tag.length() - 1);
	}

	/**
	 * 删除文章
	 */
	private void doAtclDel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		if (!Tools.isNullString(id)) {
			String sql = "DELETE FROM ARTICLE WHERE ID = ?";
			QueryHelper.update(sql, new Object[] { id });
		}
		response.sendRedirect("cms?operation=list&type=atcl");
	}

	/**
	 * 删除代码
	 */
	private void doCodeDel(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String id = request.getParameter("id");
		if (!Tools.isNullString(id)) {
			String sql = "DELETE FROM CODE WHERE ID = ?";
			QueryHelper.update(sql, new Object[] { id });
		}
		response.sendRedirect("cms?operation=list&type=code");
	}

	// -----------------------------------------------------
	public void init() throws ServletException {
		log.info("cms servlet init.");
	}

	public void destroy() {
		super.destroy();
		log.info("cms servlet destroy");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
