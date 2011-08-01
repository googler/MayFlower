package com.paladin.sys.lucene;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;

/**
 * 在内存中建立索引，然后检索之
 *
 * @author Erhu
 * @since July 31th, 2011
 */
public class RAMSearchTester {
    private static RAMDirectory dir;
    private static SimpleAnalyzer analyzer;
    private static IndexSearcher searcher;

    public static void main(String[] args) throws Exception {
        buildIndex();
        testSearch();
    }

    private static void testSearch() throws Exception {
        Query query = new TermQuery(new Term("tags", "array".toLowerCase()));
        TopDocs docs = searcher.search(query, null, 100000);
        System.out.println("hits: " + docs.totalHits);
        for (int i = 0; i < docs.totalHits; i++) {
            ScoreDoc s_doc = docs.scoreDocs[i];
            Document doc = searcher.doc(s_doc.doc);
            System.out.println(doc);
        }
    }

    private static void buildIndex() throws Exception {
        dir = new RAMDirectory();
        analyzer = new SimpleAnalyzer();
        IndexWriter write = new IndexWriter(dir, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
        Document doc = new Document();
        doc.add(new Field("id", "1", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("tags", "java, array", Field.Store.NO, Field.Index.ANALYZED));
        System.out.println(doc);
        write.addDocument(doc);
        write.close();
        searcher = new IndexSearcher(dir);
    }
}
