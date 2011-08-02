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
import com.paladin.bean.User;
import com.paladin.common.Constants;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Admin Action
 *
 * @author Erhu
 * @since Mar 19th, 2011
 */
public class AdminAction extends BaseAction {
    //private static Log log = LogFactory.getLog(AdminAction.class);

    /**
     * 编辑个人信息
     *
     * @param _reqCtxt
     */
    public void index(final RequestContext _reqCtxt) {
        User user = getUserFromSession(_reqCtxt);
        if (user != null)
            forward(_reqCtxt, "/html/admin/user_info.jsp");
        else
            redirect(_reqCtxt, "/login");
    }

    /**
     * 查看其他用户信息
     *
     * @param _reqCtxt
     * @param _userid
     */
    public void userinfo(final RequestContext _reqCtxt, final String _userid) {

    }

    /**
     * 查看服务器信息
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sysinfo(final RequestContext _reqCtxt) {
        HttpServletRequest req = _reqCtxt.request();
        Map<String, Object> sysProperties = new TreeMap(System.getProperties());
        Map<String, String> env = new TreeMap<String, String>(System.getenv());

        Runtime r = Runtime.getRuntime();

        long maxMemory = r.maxMemory() / 1024000L;
        long freeMemory = r.freeMemory() / 1024000L;
        int processorNum = r.availableProcessors();

        req.setAttribute("sysProperties", sysProperties);
        req.setAttribute("env", env);

        req.setAttribute("maxMemory", Long.valueOf(maxMemory));
        req.setAttribute("freeMemory", Long.valueOf(freeMemory));
        req.setAttribute("processorNum", Integer.valueOf(processorNum));
        forward(_reqCtxt, "/html/admin/sys_info.jsp");
    }

    /**
     * 保存用户信息
     */
    public void save(final RequestContext _reqCtxt) {
        final HttpServletRequest request = _reqCtxt.request();
        String id = request.getParameter("id");
        String username = request.getParameter("username").trim();

        String nickname = request.getParameter("nickname").trim();
        String password = request.getParameter("password").trim();
        String email = request.getParameter("email").trim();
        String profile = request.getParameter("profile").trim();
        String ip = request.getParameter("ip");

        StringBuilder sql_builder = new StringBuilder();

        if (Strings.isNullOrEmpty(id)) {// 新增 用户
            sql_builder.append("INSERT INTO USER(USERNAME, PASSWORD, NICKNAME, EMAIL, REG_DATE, ");
            sql_builder.append("LASTLOGIN_DATE, FIRST_IP, ROLE, PROFILE) VALUES (?, ?, ?, ?, ");
            sql_builder.append("NOW(), NOW(), ?, 'visitor', ?)");
            QueryHelper.update(sql_builder.toString(), new Object[]{username, password, nickname, email,
                    ip, profile});
            log.info("add new user success:)");
            forward(_reqCtxt, "/login");
        } else {// 更新 用户 信息
            String sql = "UPDATE USER SET USERNAME = ?, NICKNAME = ?, PASSWORD = ?, EMAIL = ?, PROFILE = ? WHERE ID = ?";
            QueryHelper.update(sql, new Object[]{username, nickname, password, email, profile, id});
            log.info("update user info success:)");
            User user = QueryHelper.read(User.class, "SELECT * FROM USER WHERE ID = ?", id);
            _reqCtxt.session().setAttribute("user", user);
            request.setAttribute("msg", "保存成功!");
            forward(_reqCtxt, "/admin");
        }
    }

    public void luceneIndex(final RequestContext _reqCtxt) {
        User user = getUserFromSession(_reqCtxt);
        if (user == null)
            forward(_reqCtxt, "/login?r=/luceneIndex");
        else
            forward(_reqCtxt, "/html/admin/lucene_index.jsp");
    }

    /**
     * 更新 索引
     *
     * @param _reqCtxt
     */
    public void updateIndex(final RequestContext _reqCtxt) throws IOException {
        final HttpServletRequest request = _reqCtxt.request();
        String operation = request.getParameter("operation");
        String[] tables = request.getParameter("table").toString().split(",");

        log.info(operation + " lucene index...");
        index(tables, operation.equals("rebuild"));

        forward(_reqCtxt, "/html/admin/lucene_index.jsp");
    }


    /**
     * 新建全部索引
     *
     * @param tables
     * @throws IOException
     */
    private static void index(final String[] tables, boolean _create) throws IOException {
        //当为 true 时，分词器迚行最大词长切分  ；当为 false 时，分词器进行最细粒度切分。
        Analyzer analyzer = new IKAnalyzer(false);

        for (String table : tables) {
            long begin = System.currentTimeMillis();

            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33, analyzer);
            if (_create)
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            else
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            final String index_dir = Constants.LUCENE_INDEX_ROOT + table;
            File dir = new File(index_dir);
            if (!dir.exists())
                dir.mkdirs();
            Directory directory = FSDirectory.open(dir);
            IndexWriter writer = new IndexWriter(directory, iwc);
            indexTable(writer, table);
            writer.close();

            log.info("索引 " + table + " 表耗时 " + (System.currentTimeMillis() - begin) + " milliseconds");
        }
    }

    /**
     * 为某表建立索引
     * //TODO:数量量过大时需要分批索引
     *
     * @param writer
     * @param table
     */
    private static void indexTable(IndexWriter writer, String table) throws IOException {
        String sql = "SELECT ID, TITLE, CONTENT, TAG, CREATE_DATE FROM " + table.toUpperCase();
        if (table.equalsIgnoreCase("motto"))
            sql = "SELECT ID, CONTENT, TAG, CREATE_DATE FROM " + table.toUpperCase();

        List<Map<String, Object>> blogs = QueryHelper.queryList(sql);
        for (Map<String, Object> blog : blogs) {
            Document doc = new Document();
            Field id_field = new Field("id", blog.get("ID").toString(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);

            // 将三个字段合并加索引，方便检索时分页
            StringBuilder builder = new StringBuilder();
            if (table.equalsIgnoreCase("motto"))
                builder.append(blog.get("CONTENT"));
            else
                builder.append(blog.get("TITLE"));
            builder.append(Constants.LUCENE_FIELD_SEP);
            builder.append(blog.get("CONTENT"));
            builder.append(Constants.LUCENE_FIELD_SEP);
            builder.append(blog.get("TAG"));

            Field t_c_t_field = new Field("title_content_tag", builder.toString(), Field.Store.YES, Field.Index.ANALYZED);

            doc.add(id_field);
            doc.add(t_c_t_field);

            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE)
                writer.addDocument(doc);
            else// 以id为依据更新索引
                writer.updateDocument(new Term("id", blog.get("ID").toString()), doc);
        }
    }
}
