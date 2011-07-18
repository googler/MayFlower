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
import com.paladin.bean.HFile;
import com.paladin.common.Constants;
import com.paladin.common.Tools;
import com.paladin.mvc.RequestContext;
import com.paladin.sys.db.QueryHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索服务器上的文件
 *
 * @author Erhu
 * @since May 9th, 2011
 */
public class SearchfAction extends BaseAction {

    public void index(final RequestContext _reqCtxt) {
        List<HFile> file_list = new ArrayList<HFile>();
        HttpServletRequest request = _reqCtxt.request();
        int size = 0;
        String q = request.getParameter("q");

        if (!Strings.isNullOrEmpty(q)) {
            q = Tools.ISO885912UTF8(q);
            request.setAttribute("q", q);
            // search from local file system
            String sql = "SELECT * FROM HFILE WHERE FILENAME LIKE ?";
            for (String qq : Tools.q2qArr(q)) {
                for (HFile f : QueryHelper.query(HFile.class, sql, qq))
                    if (!file_list.contains(f)) {
                        f.setFileName(f.getFileName().replace(q, Tools.standOutStr(q)));
                        file_list.add(f);
                    }
            }
        }
        size = file_list.size();
        {//分页
            super.doPage(request, file_list.size(), Constants.NUM_PER_PAGE_SEARCH, "_file");
            int begin = (page_NO - 1) * Constants.NUM_PER_PAGE_SEARCH;
            begin = begin < 0 ? 0 : begin;
            int end = page_NO * Constants.NUM_PER_PAGE_SEARCH > file_list.size() ?
                    file_list.size() : page_NO * Constants.NUM_PER_PAGE_SEARCH;
            file_list = file_list.subList(begin, end);
        }
        log.info("q = " + q);
        log.info("get file:" + file_list.size());
        request.setAttribute("file_list", file_list);
        forward(_reqCtxt, "/html/search/search_f.jsp");
    }
}
