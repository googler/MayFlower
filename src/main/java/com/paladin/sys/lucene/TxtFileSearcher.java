package com.paladin.sys.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

class Indexer {
    public static void main(String[] args) throws IOException, ParseException {
        File index_dir = new File("d:\\myData\\luceneIdx");
        File data_dir = new File("D:\\BJ\\");
        long begin = System.currentTimeMillis();
        int numIndexed = index(index_dir, data_dir);
        long end = System.currentTimeMillis();
        System.out.println("Indexing " + numIndexed + "files took "
                + (end - begin) + " milliseconds");
    }

    //open and index and start file directory traversal
    public static int index(File indexDir, File dataDir) throws IOException {
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            throw new IOException(dataDir + " does not exist or is not a directory");
        }

        IndexWriter writer = new IndexWriter(FSDirectory.open(indexDir), new StandardAnalyzer(Version.LUCENE_31), true, IndexWriter.MaxFieldLength.UNLIMITED);
        writer.setUseCompoundFile(false);

        indexDirectory(writer, dataDir);

        int numIndexed = writer.numDocs();
        writer.optimize();
        writer.close();
        return numIndexed;

    }

    //recursive method that calls itself when it finds a directory，递归索引所有文件
    private static void indexDirectory(IndexWriter writer, File dir)
            throws IOException {
        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                indexDirectory(writer, f);
            } else if (f.getName().endsWith(".txt")) {
                indexFile(writer, f);
            }
        }
    }

    //method to actually index a file using Lucene，对每个文件建立索引
    private static void indexFile(IndexWriter writer, File f) throws IOException {

        if (f.isHidden() || !f.exists() || !f.canRead()) {
            return;
        }
        System.out.println("Indexing " + f.getCanonicalPath());

        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getCanonicalPath(), Field.Store.YES, Field.Index.ANALYZED));

        writer.addDocument(doc);
    }
}

class Searcher {
    public static void main(String[] args) throws Exception {
        File indexDir = new File("d:\\myData\\luceneIdx");
        String q = "contents";

        if (!indexDir.exists() || !indexDir.isDirectory()) {
            throw new Exception(indexDir + " does not exist or is not a directory.");
        }
        search(indexDir, q);
    }

    public static void search(File indexDir, String q) throws Exception {
        // TODO Auto-generated method stub
        Directory fsDir = FSDirectory.open(indexDir);
        IndexSearcher is = new IndexSearcher(fsDir, true);

        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "contents", new StandardAnalyzer(Version.LUCENE_CURRENT));
        Query query = parser.parse(q);

        TopScoreDocCollector collector = TopScoreDocCollector.create(5, false);
        long start = new Date().getTime();

        is.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        System.out.println(hits.length);
        for (int i = 0; i < hits.length; i++) {
            Document doc = is.doc(hits[i].doc);
            System.out.println(doc.getField("filename") + " " + hits[i].toString() + "");
        }
        long end = new Date().getTime();

        System.out.println("Found " + collector.getTotalHits() +
                " documents (in " + (end - start) + "milliseconds) that matched query " +
                q + ";");

    }
}
