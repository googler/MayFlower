package com.paladin.bean;

import java.util.Date;

import com.google.common.base.Strings;

public class Blog extends BaseBlog {
    protected String tag;
    protected String content;
    protected String hits;
    protected String author;

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

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
