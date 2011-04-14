package com.paladin.bean;

import java.util.Date;

import com.google.common.base.Strings;

/**
 * 查询列表时使用的Bean类
 */
public class BaseBlog {
    protected int id;
    protected String title;
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
