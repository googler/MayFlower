package com.paladin.action;
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

import com.google.common.base.Strings;
import com.paladin.bean.Motto;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 箴言功能点的业务逻辑
 *
 * @author: Erhu
 * @date: 11-4-28 上午9:53
 */
public class MottoAction extends BaseAction {
    /**
     * 随机箴言
     */
    private static List<Motto> randomMotto;
    /**
     * 上次获取箴言的时间(单位秒)
     */
    private static GregorianCalendar updateTime;

    static {
        updateTime = new GregorianCalendar();
        generateRandomMotto(); // 类加载时获取箴言
    }

    @Override
    public void index(RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * 箴言列表
     */
    public void list(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        log.info("get motto list.");
        // 分页
        super.doPage(request, "SELECT COUNT(*) COUNT FROM MOTTO", "");
        // 获取页面数据
        String sql = "SELECT * FROM MOTTO ORDER BY ID DESC";
        List<Motto> mottos = QueryHelper.query_slice(Motto.class, sql, page_NO, Constants.NUM_PER_PAGE, new Object[]{});

        request.setAttribute("mottos", mottos);
        request.setAttribute("motto", getRandomMotto());// 提取一条箴言
        forward(_reqCtxt, "/html/motto/motto_list.jsp");
    }

    /**
     * 保存箴言(新增或者修改)
     */
    public void save(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        String id = request.getParameter("id");
        String content = request.getParameter("content").trim();
        String tag = Tools.checkTag(request.getParameter("tag").trim());
        String author = request.getParameter("author").trim();

        if (Strings.isNullOrEmpty(id)) {
            QueryHelper.update("INSERT INTO MOTTO(CONTENT, AUTHOR, TAG) VALUES(?, ?, ?)",
                    new Object[]{content.toString(), author, tag});
            log.info("Add motto success");
            redirect(_reqCtxt, "/motto");
        } else {
            QueryHelper.update("UPDATE MOTTO SET CONTENT = ?, TAG = ?, AUTHOR = ? WHERE ID = ?",
                    new Object[]{content.toString(), tag, author, id});
            log.info("Update motto success");
            redirect(_reqCtxt, "/motto");
        }
    }

    /**
     * 提供随机箴言(公共调用)
     */
    public static Motto getRandomMotto() {
        // 获取当前时间（毫秒）
        GregorianCalendar time = new GregorianCalendar();
        // 若更新间隔超过指定值，则重新获取箴言并更新updateTime
        if (randomMotto == null || Tools.getSecondsBetweenTwoDate(time, updateTime) > Constants.MINUTE_UPDATE_MOTTO * 60) {
            updateTime = time;
            generateRandomMotto();
        }
        long index = Tools.random(0, Constants.NUM_RANDOM_MOTTO - 1);
        if (randomMotto.size() <= index)
            return null;
        return (Motto) (randomMotto.toArray())[(int) index];
    }

    /**
     * 从DB中提取箴言
     */
    private static void generateRandomMotto() {
        // 获取总记录数
        String sql = "SELECT COUNT(*) FROM MOTTO";
        long count = QueryHelper.stat(sql);
        // 在[0, count - 7]之间取一个数作为LIMIT的起点
        long begin = Math.abs(Tools.random(0, count - Constants.NUM_RANDOM_MOTTO));
        // 获取箴言
        sql = "SELECT * FROM MOTTO LIMIT ?, ?";
        randomMotto = QueryHelper.query(Motto.class, sql, new Object[]{begin, Constants.NUM_RANDOM_MOTTO});
    }

    /**
     * 转到添加箴言页面
     */
    public void toAdd(final RequestContext _reqCtxt) {
        super.toAdd(_reqCtxt, "motto");
    }

    /**
     * 转到编辑页面
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        super.edit(_reqCtxt, _id, Motto.class);
    }

    /**
     * 删除箴言
     */
    public void del(final RequestContext _reqCtxt) {
        super.del(_reqCtxt, "motto");
    }
}
