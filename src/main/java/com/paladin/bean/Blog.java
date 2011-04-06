package com.paladin.bean;

import java.util.Date;

import com.google.common.base.Strings;

public class Blog extends BaseBlog {
    protected String content;

    public String getContent() {
        return Strings.nullToEmpty(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

}
