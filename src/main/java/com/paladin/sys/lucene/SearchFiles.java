package com.paladin.sys.lucene;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.Date;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * Simple command-line based search demo.
 */
public class SearchFiles {

    private static short numPerPage = 10;

    private SearchFiles() {
    }

    /**
     * Simple command-line based search demo.
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final String usage = "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] " +
                "[-repeat n] [-queries file] [-query string] [-raw] [-paging numPerPage]\n\n" +
                "See http://lucene.apache.org/java/4_0/demo.html for details.";

        args = new String[]{"-index", "D:\\myData\\luceneIdx"};
        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            out.println(usage);
            System.exit(0);
        }

        String index = "index";
        String field = "contents";
        int repeat = 0;
        boolean raw = false;
        String queryString = null;

        for (int i = 0; i < args.length; i++) {
            if ("-index".equals(args[i])) {
                index = args[i + 1];
                i++;
            } else if ("-field".equals(args[i])) {
                field = args[i + 1];
                i++;
            } else if ("-repeat".equals(args[i])) {
                repeat = Integer.parseInt(args[i + 1]);
                i++;
            } else if ("-raw".equals(args[i])) {
                raw = true;
            } else if ("-paging".equals(args[i])) {
                numPerPage = Short.parseShort(args[i + 1]);
                if (numPerPage <= 0) {
                    err.println("每页至少显示1条记录:(");
                    System.exit(1);
                }
                i++;
            }
        }

        IndexSearcher searcher = new IndexSearcher(FSDirectory.open(new File(index)));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
        BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        QueryParser parser = new QueryParser(Version.LUCENE_31, field, analyzer);

        while (true) {
            out.print("请输入关键字: ");

            String key = queryString != null ? queryString : buffered_reader.readLine();
            if (key == null || key.trim().length() == 0)
                break;

            Query query = parser.parse(key);
            out.println("被检索字段为: " + query.toString(field));

            if (repeat > 0) {// 重复N 次
                Date start = new Date();
                for (int i = 0; i < repeat; i++) {
                    searcher.search(query, null, 100);
                }
                Date end = new Date();
                out.println("耗时: " + (end.getTime() - start.getTime()) + "ms");
            }
            // 分页 搜索
            doPagingSearch(buffered_reader, searcher, query, raw, queryString == null);

            if (queryString != null)
                break;
        }
        searcher.close();
    }

    /**
     * This demonstrates a typical paging search scenario, where the search engine presents
     * pages of size n to the user. The user can then go to the next page if interested in
     * the next hits.
     * <p/>
     * When the query is executed for the first time, then only enough results are collected
     * to fill 5 result pages. If the user wants to page beyond this limit, then the query
     * is executed another time and all hits are collected.
     */
    public static void doPagingSearch(final BufferedReader in, final IndexSearcher searcher,
                                      final Query query, boolean raw, boolean interactive) throws IOException {
        //检索前5页数据
        TopDocs top_result = searcher.search(query, 5 * numPerPage);

        // 实际查得的记录数
        int total_hits = top_result.totalHits;
        out.println("检索到 " + total_hits + " 条匹配记录");

        // 显示的记录的ID区间
        int start = 0;
        int end = Math.min(total_hits, numPerPage);// 取每页记录条数和总记录数中小的数值

        // 取得结果集
        ScoreDoc[] hits = top_result.scoreDocs;
        while (true) {
            if (end > hits.length) {
                out.print("只获取了 " + total_hits + " 条记录中的 1 - " + hits.length + " 条，查看更多结果吗(y/n) ？");
                String line = in.readLine();
                if (line.length() == 0 || line.charAt(0) == 'n')
                    break;
                hits = searcher.search(query, total_hits).scoreDocs;
            }

            end = Math.min(hits.length, start + numPerPage);
            for (int i = start; i < end; i++) {
                if (raw) {
                    out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
                    continue;
                }
                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get("path");
                if (path != null) {
                    System.out.println((i + 1) + ".  " + path);
                    String title = doc.get("title");
                    if (title != null)
                        out.println("   标题: " + doc.get("title"));
                } else
                    out.println((i + 1) + ". " + "文档没有 path 属性");
            }

            // 非交互模式
            if (!interactive || end == 0) break;

            // 还有未查看的记录
            if (total_hits >= end) {
                boolean quit = false;
                while (true) {
                    if (start - numPerPage >= 0)
                        out.print("上一页(p), ");
                    if (start + numPerPage < total_hits)
                        out.print("下一页(n), ");
                    out.println("退出(q) 或者 跳转到指定页(...)");

                    String line = in.readLine();
                    if (line.length() == 0 || line.charAt(0) == 'q') {
                        quit = true;
                        break;
                    }
                    if (line.charAt(0) == 'p') {
                        start = Math.max(0, start - numPerPage);
                        break;
                    } else if (line.charAt(0) == 'n') {
                        if (start + numPerPage < total_hits)
                            start += numPerPage;
                        break;
                    } else {
                        try {
                            int page = Integer.parseInt(line);
                            if ((page - 1) * numPerPage < total_hits) {
                                start = (page - 1) * numPerPage;
                                break;
                            } else
                                out.println("此页不存在！");
                        } catch (NumberFormatException e) {
                            out.println("你输入的页面不正确！");
                        }
                    }
                }
                if (quit)
                    break;
                end = Math.min(total_hits, start + numPerPage);
            }
        }
    }
}
