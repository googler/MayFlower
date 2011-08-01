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

public class Constants {
    /**
     * 每页显示文章条数
     */
    public static final int NUM_PER_PAGE = 46;
    /**
     * 每页显示的搜索结果条数
     */
    public static final int NUM_PER_PAGE_SEARCH = 10;

    /**
     * 搜索结果中的文章内容只显示600字
     */
    public static final int LENGTH_OF_SEARCH_CONTENT = 500;
    /**
     * 服务器启动时随机搜索的箴言的条数
     */
    public static final int NUM_RANDOM_MOTTO = 7;
    /**
     * 更新箴言列表的时间间隔(分钟)
     */
    public static final int MINUTE_UPDATE_MOTTO = 1;
    /**
     * 字段内容间的分隔符
     */
    public static final String LUCENE_FIELD_SEP = "!&%@~~@%&!";
}
