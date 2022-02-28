package com.example.demo.dto;

public class NewsDescription {
	private String title;
	private String short_description;
	private String image;
	private String link;

	public NewsDescription() {
		super();
	}

	public NewsDescription(String image, String short_description) {
		super();
		this.image = image;
		this.short_description = short_description;
	}

	public NewsDescription(String title, String short_description, String image, String link) {
		super();
		this.title = title;
		this.short_description = short_description;
		this.image = image;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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
