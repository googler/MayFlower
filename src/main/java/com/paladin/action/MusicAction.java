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
import com.paladin.mvc.RequestContext;
import com.sun.deploy.util.SyncFileAccess;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 为Android客户端提供音乐同步服务
 *
 * @author Erhu
 * @since July 28, 2011
 */
public class MusicAction extends BaseAction {
    /**
     * default page
     *
     * @param _reqCtxt
     */
    @Override
    protected void index(RequestContext _reqCtxt) {
        list(_reqCtxt);
    }

    /**
     * get music list
     *
     * @param _reqCtxt (:Nothing could hold a man back from his future!
     */
    public void list(final RequestContext _reqCtxt) {
        // 遍历音乐文件夹
        List<String> music_list = new ArrayList<String>();
        music_list.add("good.mp3");
        music_list.add("bad.mp3");
        String result = "";
        for (String str : music_list) {
            result += (str + "||");
        }
        if (!Strings.isNullOrEmpty(result))
            result = result.substring(0, result.length() - 2);
        //JSONObject jsonObj = new JSONObject();
        //jsonObj.element("list", result);
        try {
            _reqCtxt.response().getOutputStream().print(URLEncoder.encode(result, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(final RequestContext _reqCtxt, final String _mp3Name) {
        final HttpServletRequest request = _reqCtxt.request();
        forward(_reqCtxt, "/music_store/" + _mp3Name);
    }
}
