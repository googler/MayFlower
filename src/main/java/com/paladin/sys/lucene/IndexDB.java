package com.paladin.sys.lucene;

import com.paladin.sys.db.QueryHelper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.out;

/**
 * 为数据库表建立索引
 *
 * @author erhu
 * @since August 1, 2011
 */
public class IndexDB {

    private static final String BASE_DIR = "D:\\myData\\luceneIdx\\";
    // 要建立索引的表
    private static String[] tables = new String[]{"blog", "code"};
    // 新建 or 更新
    private static boolean create = true;

    // 字段内容间的分隔符
    private static final String SEP = "!&%@~~@%&!";


    static {
        for (String table : tables) {
            String index_dir = BASE_DIR + table;
            File dir = new File(index_dir);
            if (!dir.exists())
                dir.mkdirs();
        }
    }

    private IndexDB() {
    }

    public static void main(String[] args) throws IOException, ParseException {
        index();
        //search("弥天大谎");
    }

    /**
     * 新建全部索引
     *
     * @throws IOException
     */
    private static void index() throws IOException {
        for (String table : tables) {
            long begin = System.currentTimeMillis();
            final String index_dir = BASE_DIR + table;
            File dir = new File(index_dir);
            Directory directory = FSDirectory.open(dir);
            Analyzer analyzer = new IKAnalyzer(false);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33, analyzer);
            if (create)
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            else
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            IndexWriter writer = new IndexWriter(directory, iwc);

            indexTable(writer, table);

            writer.close();
            out.println("索引 " + table + " 表耗时 " + (System.currentTimeMillis() - begin)
                    + " milliseconds");
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
        List<Map<String, Object>> blogs = QueryHelper.queryList(sql);
        for (Map<String, Object> blog : blogs) {
            Document doc = new Document();
            Field id_field = new Field("id", blog.get("ID").toString(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);

            // 将三个字段合并加索引，方便检索时分页
            StringBuilder builder = new StringBuilder();
            builder.append(blog.get("TITLE"));
            builder.append(SEP);
            builder.append(blog.get("CONTENT"));
            builder.append(SEP);
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

    public static void search(String _query) throws IOException, ParseException {
        out.println("检索关键字为: " + _query);

        String index_dir = "D:\\myData\\luceneIdx\\blog";
        File dir = new File(index_dir);

        IndexSearcher searcher = new IndexSearcher(FSDirectory.open(dir));
        Analyzer analyzer = new IKAnalyzer(false);

        Set<Document> doc_set = new HashSet<Document>();
        // for (String field : fields) {
        QueryParser parser = new QueryParser(Version.LUCENE_33, "title_content_tag", analyzer);
        Query query = parser.parse(_query);

        TopDocs top_result = searcher.search(query, 1000/* top 1000 */);
        int total_hits = top_result.totalHits;
        out.println("字段: title_content_tag, 匹配记录数: " + total_hits + " 条");

        ScoreDoc[] hits = top_result.scoreDocs;
        for (ScoreDoc hit : hits) {
            boolean flag = doc_set.add(searcher.doc(hit.doc));
            if (!flag)
                out.println("此记录已经存在");
        }
        //}
        Set<Integer> set_int = new HashSet<Integer>();
        for (Document doc : doc_set) {
            String res = doc.get("title_content_tag");
            out.println(res.split(SEP)[0]);
            set_int.add(Integer.parseInt(doc.get("id")));
            //out.println(doc.get("id"));
        }
        out.println("set_int.size() = " + set_int.size());
        out.println("set_doc.size() = " + doc_set.size());
    }
}
