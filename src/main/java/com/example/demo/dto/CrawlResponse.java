package com.example.demo.dto;

import java.util.List;

public class CrawlResponse {

	private String title;
	private String short_description;
	private String content;
	private String author;
	private String source;
	private String date;
	private List<String> tags;

	public CrawlResponse() {
		super();
	}

	public CrawlResponse(String title, String short_description, String content, String author, String source,
			String date, List<String> tags) {
		super();
		this.title = title;
		this.short_description = short_description;
		this.content = content;
		this.author = author;
		this.source = source;
		this.date = date;
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
