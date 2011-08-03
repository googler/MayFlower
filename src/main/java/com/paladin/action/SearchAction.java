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
import com.paladin.bean.Blog;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search Action
 *
 * @author Erhu
 * @since Mar 12th, 2011
 */
public class SearchAction extends BaseAction {
    /**
     * lucene 索引的 字段名
     */
    private static final String fields = "title_content_tag";

    public void index(final RequestContext _reqCtxt) {
        redirect(_reqCtxt, "/blog");
    }

    /**
     * Search blog ,code and motto
     */
    public void bcm(final RequestContext _reqCtxt) throws IOException, ParseException, InvalidTokenOffsetsException {
        HttpServletRequest request = _reqCtxt.request();
        String q = _reqCtxt.param("q");

        if (!Strings.isNullOrEmpty(q)) {
            q = Tools.ISO885912UTF8(q).trim();
            log.info("q = " + q);
            request.setAttribute("q", q);

            _b(request, q, "blog");// 查找博文
            _b(request, q, "code");// 查找代码
            _b(request, q, "motto");// 查找箴言

            // 刷新页面时应该聚集到哪个选项卡
            String type = _reqCtxt.param("t");
            // 控制搜索结果页面的样式
            request.setAttribute("class_blog", "class=\'u_tab\'");
            request.setAttribute("class_code", "class=\'u_tab\'");
            request.setAttribute("class_motto", "class=\'u_tab\'");
            if (!Strings.isNullOrEmpty(type)) {
                request.setAttribute("style_blog", "style='display:none;'");
                if ("code".equals(type)) {// t == code时，搜索结果的翻页直接定位到代码tab
                    request.setAttribute("class_code", "class=\'u_tab_hover\'");
                    request.setAttribute("style_motto", "style='display:none;'");
                } else if ("motto".equals(type)) {
                    request.setAttribute("class_motto", "class=\'u_tab_hover\'");
                    request.setAttribute("style_code", "style='display:none;'");
                }
            } else {
                request.setAttribute("class_blog", "class=\'u_tab_hover\'");
                request.setAttribute("style_code", "style='display:none;'");
                request.setAttribute("style_motto", "style='display:none;'");
            }
        }
        forward(_reqCtxt, "/html/search/search_bc.jsp");
    }

    /**
     * search using lucene
     *
     * @param request
     * @param _query
     * @throws IOException
     * @throws ParseException
     */
    private void _b(final HttpServletRequest request, final String _query, final String _table) throws IOException, ParseException, InvalidTokenOffsetsException {
        final String index_dir = Constants.LUCENE_INDEX_ROOT + _table;

        IndexSearcher searcher = new IndexSearcher(FSDirectory.open(new File(index_dir)));
        QueryParser parser = new QueryParser(Version.LUCENE_33, fields, new IKAnalyzer(false));
        Query query = parser.parse(_query);

        TopScoreDocCollector collector = TopScoreDocCollector.create(10000, true);
        searcher.search(query, collector);
        // 在 索引器 中使用 IKSimilarity 相似度 评估器
        searcher.setSimilarity(new IKSimilarity());

        // 分页
        super.doPage(request, collector.getTotalHits(), Constants.NUM_PER_PAGE_SEARCH, "_" + _table);
        log.info("get " + _table + ":" + collector.getTotalHits());

        // 查询当前页的记录
        int begin = (page_NO - 1) * Constants.NUM_PER_PAGE_SEARCH;
        begin = begin < 0 ? 0 : begin;
        ScoreDoc[] score_docs = collector.topDocs(begin, Constants.NUM_PER_PAGE_SEARCH).scoreDocs;

        List<Document> doc_list = new ArrayList<Document>();
        for (ScoreDoc score_doc : score_docs)
            doc_list.add(searcher.doc(score_doc.doc));

        request.setAttribute(_table + "_list", getBlogListFromDocList(query, doc_list));
    }

    /**
     * 从 搜索 结果集 取得 博文列表
     *
     * @param query
     * @param doc_list
     * @return
     */
    private List<Blog> getBlogListFromDocList(Query query, List<Document> doc_list) {
        List<Blog> blog_list = new ArrayList<Blog>();
        for (Document doc : doc_list) {
            Blog blog = new Blog();
            blog.setId(Integer.parseInt(doc.get("id")));

            String[] data_arr = doc.get("title_content_tag").toString().split(Constants.LUCENE_FIELD_SEP);

            final String title = Tools.highlight(query, "title_content_tag", data_arr[0]);// 高亮
            blog.setTitle(title == null ? data_arr[0] : title);

            if (data_arr.length == 3) {
                final String tag = Tools.highlight(query, "title_content_tag", data_arr[2]);
                blog.setTag(tag == null ? data_arr[2] : tag);
            }

            String content = data_arr[1];
            content = content.replaceAll("<[^>]*>", "");// 除去 HTML 标签
            // TODO:高亮显示的字数有最大限制，达不到 Constants.LENGTH_OF_SEARCH_CONTENT
            String f_content = Tools.highlight(query, "title_content_tag", content);
            if (f_content == null)
                f_content = content;

            if (f_content.length() > Constants.LENGTH_OF_SEARCH_CONTENT)
                f_content = f_content.substring(0, Constants.LENGTH_OF_SEARCH_CONTENT);
            blog.setContent(f_content);
            blog_list.add(blog);
        }
        return blog_list;
    }
}
