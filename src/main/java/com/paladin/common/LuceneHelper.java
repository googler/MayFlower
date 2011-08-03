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
package com.paladin.common;

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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Lucene Helper
 *
 * @author Erhu
 * @since August 3, 2011
 */
public class LuceneHelper {

    private static Log log = LogFactory.getLog(LuceneHelper.class);

    /**
     * 新建全部索引
     *
     * @param tables
     * @throws IOException
     */
    public static void index(final String[] tables, boolean _create) {
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
            try {
                Directory directory = FSDirectory.open(dir);
                IndexWriter writer = new IndexWriter(directory, iwc);
                indexTable(writer, table);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
