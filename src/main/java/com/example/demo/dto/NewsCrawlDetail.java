package com.example.demo.dto;

import java.util.List;

public class NewsCrawlDetail {

	private String title;
	private String content;
	private String short_description;
	private String author;
	private List<String> tags;

	public NewsCrawlDetail() {
		super();
	}

	public NewsCrawlDetail(String title, String content, String short_description, String author, List<String> tags) {
		super();
		this.title = title;
		this.content = content;
		this.short_description = short_description;
		this.author = author;
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "NewsDetail [title=" + title + ", content=" + content + ", short_description=" + short_description
				+ ", author=" + author + ", tags=" + tags + "]";
	}

	
	
}
