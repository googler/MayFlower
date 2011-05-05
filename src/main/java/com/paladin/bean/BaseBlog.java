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
package com.paladin.bean;

import com.google.common.base.Strings;

/**
 * 查询列表时使用的Bean类
 */
public class BaseBlog {
    protected int id;
    protected String title;
    protected int top;
    protected String hits;

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Strings.nullToEmpty(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((BaseBlog) obj).id;
    }

    @Override
    public int hashCode() {
        return (this.id + "").hashCode();
    }
}
