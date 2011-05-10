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
import com.paladin.bean.Motto;
import com.paladin.bean.Word;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 我爱记单词-业务控制类
 *
 * @author: Erhu
 * @date: 11-5-9 下午4:17
 */
public class WordAction extends BaseAction {

    private static final Log log = LogFactory.getLog(WordAction.class);
    /**
     * 随机单词
     */
    private static List<Word> randomWord;
    /**
     * 上次获取单词的时间(单位秒)
     */
    private static GregorianCalendar updateTime;

    static {
        updateTime = new GregorianCalendar();
        generateRandomWord(); // 类加载时获取单词
    }

    @Override
    public void index(RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * 单词列表
     */
    public void list(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        log.info("get Word list.");
        // 分页
        super.doPage(request, "SELECT COUNT(*) COUNT FROM Word", "");
        // 获取页面数据
        String sql = "SELECT * FROM WORD ORDER BY SPELLING";
        List<Word> Words = QueryHelper.query_slice(Word.class, sql, page_NO, Constants.NUM_PER_PAGE, new Object[]{});

        request.setAttribute("words", Words);
        request.setAttribute("word", getRandomWord());// 提取一条单词
        forward(_reqCtxt, "/html/word/word_list.jsp");
    }

    /**
     * 保存单词(新增或者修改)
     */
    public void save(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        String id = request.getParameter("id");
        String spelling = request.getParameter("spelling").trim();
        String phonetic_symbol = Tools.checkTag(request.getParameter("phonetic_symbol"));
        String meaning = request.getParameter("meaning").trim();
        boolean rembered = Boolean.parseBoolean(request.getParameter("remembed"));

        if (Strings.isNullOrEmpty(id)) {
            QueryHelper.update("INSERT INTO Word(SPELLING, PHONETIC_SYMBOL, MEANING, REMEMBERED) VALUES(?, ?, ?, ?)",
                    new Object[]{spelling, phonetic_symbol, meaning, rembered});
            log.info("Add word success");
            redirect(_reqCtxt, "/word");
        } else {
            QueryHelper.update("UPDATE WORD SET SPELLING = ?, PHONETIC_SYMBOL = ?, MEANING = ?, REMEMBERED = ? WHERE ID = ?",
                    new Object[]{spelling, phonetic_symbol, meaning, rembered, id});
            log.info("Update word success");
            redirect(_reqCtxt, "/word");
        }
    }

    /**
     * 提供随机单词(公共调用)
     */
    public static Word getRandomWord() {
        // 获取当前时间（毫秒）
        GregorianCalendar time = new GregorianCalendar();
        // 若更新间隔超过指定值，则重新获取单词并更新updateTime
        if (randomWord == null || Tools.getSecondsBetweenTwoDate(time, updateTime) > Constants.MINUTE_UPDATE_MOTTO * 60) {
            updateTime = time;
            generateRandomWord();
        }
        long index = Tools.random(0, Constants.MINUTE_UPDATE_MOTTO - 1);
        if (randomWord.size() <= index)
            return null;
        return (Word) (randomWord.toArray())[(int) index];
    }

    /**
     * 从DB中提取单词
     */
    private static void generateRandomWord() {
        // 获取总记录数
        String sql = "SELECT COUNT(*) FROM WORD";
        long count = QueryHelper.stat(sql);
        // 在[0, count - 7]之间取一个数作为LIMIT的起点
        long begin = Math.abs(Tools.random(0, count - Constants.MINUTE_UPDATE_MOTTO));
        // 获取单词
        sql = "SELECT * FROM WORD LIMIT ?, ?";
        randomWord = QueryHelper.query(Word.class, sql, new Object[]{begin, Constants.MINUTE_UPDATE_MOTTO});
    }

    /**
     * 转到添加单词页面
     */
    public void toAdd(final RequestContext _reqCtxt) {
        super.toAdd(_reqCtxt, "word");
    }

    /**
     * 转到编辑页面
     */
    public void edit(final RequestContext _reqCtxt, final long _id) {
        super.edit(_reqCtxt, _id, Word.class);
    }

    /**
     * 删除单词
     */
    public void del(final RequestContext _reqCtxt) {
        super.del(_reqCtxt, "word");
    }
}