package com.paladin.mvc;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 请求上下文
 * 
 * @author Erhu
 * @since 2.0 Mar 8th, 2011
 */
public class RequestContext {

	private final static Log log = LogFactory.getLog(RequestContext.class);

	private final static String UTF_8 = "UTF-8";

	private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();
	// private final static boolean isResin;
	private final static String upload_tmp_path;
	private final static String TEMP_UPLOAD_PATH_ATTR_NAME = "$OSCHINA_TEMP_UPLOAD_PATH$";

	private static String webroot = null;

	private ServletContext context;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Cookie> cookies;

	static {
		webroot = getWebrootPath();
		// isResin = _CheckResinVersion();
		// 上传的临时目录
		upload_tmp_path = webroot + "attached" + File.separator;
		try {
			FileUtils.forceMkdir(new File(upload_tmp_path));
		} catch (IOException excp) {
		}
	}

	private final static String getWebrootPath() {
		String root = RequestContext.class.getResource("/").getFile();
		try {
			root = new File(root).getParentFile().getParentFile().getCanonicalPath();
			root += File.separator;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return root;
	}

	/**
	 * 初始化请求上下文
	 * 
	 * @param ctx
	 * @param req
	 * @param res
	 */
	public static RequestContext begin(ServletContext ctx, HttpServletRequest req, HttpServletResponse res) {
		RequestContext rc = new RequestContext();
		rc.context = ctx;
		rc.request = req;// _AutoUploadRequest(_AutoEncodingRequest(req));
		rc.response = res;
		rc.response.setCharacterEncoding(UTF_8);
		rc.session = req.getSession(false);
		rc.cookies = new HashMap<String, Cookie>();
		Cookie[] cookies = req.getCookies();
		if (cookies != null)
			for (Cookie ck : cookies) {
				rc.cookies.put(ck.getName(), ck);
			}
		contexts.set(rc);
		return rc;
	}

	/**
	 * 返回Web应用的路径
	 * 
	 * @return
	 */
	public static String root() {
		return webroot;
	}

	/**
	 * 获取当前请求的上下文
	 * 
	 * @return
	 */
	public static RequestContext get() {
		return contexts.get();
	}

	public void end() {
		String tmpPath = (String) request.getAttribute(TEMP_UPLOAD_PATH_ATTR_NAME);
		if (tmpPath != null) {
			try {
				FileUtils.deleteDirectory(new File(tmpPath));
			} catch (IOException e) {
				log.fatal("Failed to cleanup upload directory: " + tmpPath, e);
			}
		}
		this.context = null;
		this.request = null;
		this.response = null;
		this.session = null;
		this.cookies = null;
		contexts.remove();
	}

	public Locale locale() {
		return request.getLocale();
	}

	public void closeCache() {
		header("Pragma", "No-cache");
		header("Cache-Control", "no-cache");
		header("Expires", 0L);
	}

	public long id() {
		return param("id", 0L);
	}

	/*
	 * public String ip(){ return RequestUtils.getRemoteAddr(request); }
	 */

	@SuppressWarnings("unchecked")
	public Enumeration<String> params() {
		return request.getParameterNames();
	}

	public String param(String name, String... def_value) {
		String v = request.getParameter(name);
		return (v != null) ? v : ((def_value.length > 0) ? def_value[0] : null);
	}

	public long param(String name, long def_value) {
		return NumberUtils.toLong(param(name), def_value);
	}

	public int param(String name, int def_value) {
		return NumberUtils.toInt(param(name), def_value);
	}

	public byte param(String name, byte def_value) {
		return (byte) NumberUtils.toInt(param(name), def_value);
	}

	public String[] params(String name) {
		return request.getParameterValues(name);
	}

	public String uri() {
		return request.getRequestURI();
	}

	public String contextPath() {
		return request.getContextPath();
	}

	public void redirect(String uri) {
		try {
			response.sendRedirect(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void forward(String uri) {
		RequestDispatcher rd = context.getRequestDispatcher(uri);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void include(String uri) throws ServletException, IOException {
	 * RequestDispatcher rd = context.getRequestDispatcher(uri);
	 * rd.include(request, response); }
	 */

	/**
	 * 输出信息到浏览器
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public void print(Object msg) throws IOException {
		if (!UTF_8.equalsIgnoreCase(response.getCharacterEncoding()))
			response.setCharacterEncoding(UTF_8);
		response.getWriter().print(msg);
	}

	public void error(int code, String... msg) throws IOException {
		if (msg.length > 0)
			response.sendError(code, msg[0]);
		else
			response.sendError(code);
	}

	public void forbidden() throws IOException {
		error(HttpServletResponse.SC_FORBIDDEN);
	}

	public void not_found() throws IOException {
		error(HttpServletResponse.SC_NOT_FOUND);
	}

	public ServletContext context() {
		return context;
	}

	public HttpSession session() {
		return session;
	}

	public HttpSession session(boolean create) {
		return (session == null && create) ? (session = request.getSession()) : session;
	}

	public Object sessionAttr(String attr) {
		HttpSession ssn = session();
		return (ssn != null) ? ssn.getAttribute(attr) : null;
	}

	public HttpServletRequest request() {
		return request;
	}

	public HttpServletResponse response() {
		return response;
	}

	public Cookie cookie(String name) {
		return cookies.get(name);
	}

	public String header(String name) {
		return request.getHeader(name);
	}

	public void header(String name, String value) {
		response.setHeader(name, value);
	}

	public void header(String name, int value) {
		response.setIntHeader(name, value);
	}

	public void header(String name, long value) {
		response.setDateHeader(name, value);
	}

	/**
	 * 返回当前登录的用户资料
	 * 
	 * @return
	 */
	/*
	 * public IUser user() { return User.GetLoginUser(request); }
	 */

	/**
	 * 保存登录信息
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param save
	 * 
	 *            public void saveUserInCookie(IUser user, boolean save) {
	 *            String new_value = _GenLoginKey(user, ip(),
	 *            header("user-agent")); int max_age = save ? MAX_AGE : -1;
	 *            deleteCookie(COOKIE_LOGIN, true);
	 *            cookie(COOKIE_LOGIN,new_value,max_age,true); }
	 * 
	 *            public void deleteUserInCookie() { deleteCookie(COOKIE_LOGIN,
	 *            true); }
	 */

	/**
	 * 自动解码
	 * 
	 * @author liudong
	 */
	private static class RequestProxy extends HttpServletRequestWrapper {
		private String uri_encoding;

		RequestProxy(HttpServletRequest request, String encoding) {
			super(request);
			this.uri_encoding = encoding;
		}

		/**
		 * 重载getParameter
		 */
		public String getParameter(String paramName) {
			String value = super.getParameter(paramName);
			return _DecodeParamValue(value);
		}

		/**
		 * 重载getParameterMap
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map<String, Object> getParameterMap() {
			Map params = super.getParameterMap();
			HashMap<String, Object> new_params = new HashMap<String, Object>();
			Iterator<String> iter = params.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object oValue = params.get(key);
				if (oValue.getClass().isArray()) {
					String[] values = (String[]) params.get(key);
					String[] new_values = new String[values.length];
					for (int i = 0; i < values.length; i++)
						new_values[i] = _DecodeParamValue(values[i]);

					new_params.put(key, new_values);
				} else {
					String value = (String) params.get(key);
					String new_value = _DecodeParamValue(value);
					if (new_value != null)
						new_params.put(key, new_value);
				}
			}
			return new_params;
		}

		/**
		 * 重载getParameterValues
		 */
		public String[] getParameterValues(String arg0) {
			String[] values = super.getParameterValues(arg0);
			for (int i = 0; values != null && i < values.length; i++)
				values[i] = _DecodeParamValue(values[i]);
			return values;
		}

		/**
		 * 参数转码
		 * 
		 * @param value
		 * @return
		 */
		private String _DecodeParamValue(String value) {
			if (StringUtils.isBlank(value) || StringUtils.isBlank(uri_encoding) || StringUtils.isNumeric(value))
				return value;
			try {
				return new String(value.getBytes("8859_1"), uri_encoding);
			} catch (Exception e) {
			}
			return value;
		}

	}

	public final static String COOKIE_LOGIN = "oscid";
	// private final static int MAX_AGE = 86400 * 365;
	// private final static byte[] E_KEY = new byte[] { '1', '2', '3', '4', '5',
	// '6', '7', '8' };
}