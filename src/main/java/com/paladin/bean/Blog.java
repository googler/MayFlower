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

import java.util.Date;

public class Blog extends BaseBlog {
    protected String tag;
    protected String content;
    protected String author;
    protected Date create_date;
    protected Date lastmodify_date;

    public String getContent() {
        return Strings.nullToEmpty(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return Strings.nullToEmpty(tag);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date createDate) {
        create_date = createDate;
    }

    public Date getLastmodify_date() {
        return lastmodify_date;
    }

    public void setLastmodify_date(Date lastmodify_date) {
        this.lastmodify_date = lastmodify_date;
    }

    @Override
    public String toString() {
        return "id: " + id + ", title: " + title;
    }

    @Override
    public int hashCode() {
        return String.valueOf(id).hashCode();
    }
}
