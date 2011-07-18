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
import com.paladin.bean.Code;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 代码业务类
 */
public class CodeAction extends BaseAction {
    /**
     * 默认页面
     */
    public void index(final RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * 代码列表
     */
    public void list(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        log.info("get code list.");
        super.doPage(request, "SELECT COUNT(*) COUNT FROM CODE", "");// 分页
        // 获取页面数据
        String sql = "SELECT * FROM CODE ORDER BY CREATE_DATE DESC";
        List<BaseBlog> codes = QueryHelper.query_slice(BaseBlog.class, sql, page_NO, Constants.NUM_PER_PAGE);
        // -------------------------------------------------------------------------------------------------------------
        request.setAttribute("codes", codes);
        request.setAttribute("hotTag", super.hotTag("CODE").subList(0, 15));// 提取热门tag
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
            if (title.indexOf(q) >= 0)
                title = title.replaceAll(q, "<span style='background-color:#f00;'>" + q + "</span>");
            code.setTitle(title);
            request.setAttribute("q", q);
        }
        request.setAttribute("code", code);
        // --------------------------------hits++
        QueryHelper.update("UPDATE CODE SET HITS = (HITS + 1) WHERE ID = ?", _id);
        forward(_reqCtxt, "/html/code/code_read.jsp");
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
     * 转到编辑页面
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        super.edit(_reqCtxt, _id, Code.class);
    }

    /**
     * 转到添加代码页面
     */
    public void toAdd(final RequestContext _reqCtxt) {
        super.toAdd(_reqCtxt, "code");
    }

    /**
     * 删除代码
     */
    public void del(final RequestContext _reqCtxt) {
        super.del(_reqCtxt, "code");
    }
}
