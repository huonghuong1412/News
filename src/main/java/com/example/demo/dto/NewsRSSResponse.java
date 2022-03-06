package com.example.demo.dto;

public class NewsRSSResponse {
	private String title;
	private String short_description;
	private String image;
	private String url;

	public NewsRSSResponse() {
		super();
	}

	public NewsRSSResponse(String image, String short_description) {
		super();
		this.image = image;
		this.short_description = short_description;
	}

	public NewsRSSResponse(String title, String short_description, String image, String url) {
		super();
		this.title = title;
		this.short_description = short_description;
		this.image = image;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

}
