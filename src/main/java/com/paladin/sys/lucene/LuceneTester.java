package com.paladin.sys.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;

/**
 * 测试 搜索
 *
 * @author Winter Lau
 */
public class LuceneTester {

    /**
     * 搜索 关键字 高亮 测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String text = "The quick brown fox jumps over the lazy dog";
        TermQuery query = new TermQuery(new Term("field", "fox"));
        Scorer scorer = new QueryScorer(query);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");
        Highlighter hig = new Highlighter(formatter, scorer);

        TokenStream tokens = new IKAnalyzer().tokenStream("field", new StringReader(text));
        System.out.println(hig.getBestFragment(tokens, text));
    }

}

