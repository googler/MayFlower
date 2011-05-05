/**
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
package com.paladin.mvc;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 请求上下文
 *
 * @author Winter Lau (http://my.oschina.net/javayou)<br>
 * @modify Erhu
 * @date 2.0 Mar 8th, 2011
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
        String root = "";
        try {
            root = RequestContext.class.getResource("/").toURI().getPath().toString();
            root = new File(root).getParentFile().getParentFile().getCanonicalPath();
            root += File.separator;
        } catch (Exception e) {
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
}