package com.paladin.bean;

import java.util.Date;

import com.google.common.base.Strings;

public class Blog {
	protected int id;
	protected String title;
	protected String content;
	protected String author;
	protected Date create_date;
	protected String tag;
	protected String hits;

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

	public String getContent() {
		return Strings.nullToEmpty(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return Strings.nullToEmpty(author);
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

	@Override
	public boolean equals(Object obj) {
		Blog tf = (Blog) obj;
		return this.id == tf.id;
	}

	@Override
	public int hashCode() {
		return (this.id + "").hashCode();
	}

}
