package com.paladin.bean;

import java.util.Date;

import com.google.common.base.Strings;

/**
 * 查询列表时使用的Bean类
 */
public abstract class BaseBlog {
    protected int id;
    protected String title;
    protected Date create_date;
    protected Date lastmodify_date;
    protected int top;

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

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date createDate) {
        create_date = createDate;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Blog) obj).id;
    }

    @Override
    public int hashCode() {
        return (this.id + "").hashCode();
    }

    public Date getLastmodify_date() {
        return lastmodify_date;
    }

    public void setLastmodify_date(Date lastmodify_date) {
        this.lastmodify_date = lastmodify_date;
    }
}
