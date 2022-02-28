package com.example.demo.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo.model.News;

public class NewsDto extends AbstractDTO<NewsDto> {

	private String title;
	private String short_description;
	private String image;
	private String url;
	private Integer display;
	private String createdDate;
	private String category_slug;
	private String category_name;
	private String source_slug;
	private String source_name;

	public NewsDto() {
		super();
	}

	public NewsDto(News entity) {
		super();
		this.setId(entity.getId());
		this.title = entity.getTitle();
		this.short_description = entity.getShort_description();
		this.image = entity.getImage();
		this.url = entity.getUrl();
		this.display = entity.getDisplay();
		this.category_slug = entity.getCategory().getSlug();
		this.category_name = entity.getCategory().getName();

		this.source_slug = entity.getSource().getSlug();
		this.source_name = entity.getSource().getName();

		try {
			this.createdDate = new SimpleDateFormat("dd/MM/yyyy").format(
					new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(entity.getCreatedDate()).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCategory_slug() {
		return category_slug;
	}

	public void setCategory_slug(String category_slug) {
		this.category_slug = category_slug;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getSource_slug() {
		return source_slug;
	}

	public void setSource_slug(String source_slug) {
		this.source_slug = source_slug;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	@Override
	public String toString() {
		return "NewsDto [title=" + title + ", short_description=" + short_description + ", image=" + image + ", url="
				+ url + ", display=" + display + ", createdDate=" + createdDate + ", category_slug=" + category_slug
				+ ", category_name=" + category_name + ", source_slug=" + source_slug + ", source_name=" + source_name
				+ "]";
	}
	
	

}
