package com.paladin.bean;

import java.util.Date;

import com.google.common.base.Strings;

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
}
