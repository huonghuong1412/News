package com.example.demo.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.model.Post;
import com.example.demo.model.Tag;

public class PostDto extends AbstractDTO<PostDto> {

	private String title;
	private String slug;
	private String image;
	private String content;
	private String author_name;
	private String author_slug;
	private Integer display;
	private String createdDate;
	private String category_slug;
	private String category_name;
	private List<String> tag_names;
	private List<String> tag_slugs;

	public PostDto() {
		super();
	}

	public PostDto(Post entity) {
		super();
		this.setId(entity.getId());
		this.title = entity.getTitle();
		this.slug = entity.getSlug();
		this.image = entity.getImage();
		this.content = entity.getContent();
		this.author_name = entity.getAuthor().getName();
		this.author_slug = entity.getAuthor().getSlug();
		this.display = entity.getDisplay();
		this.category_slug = entity.getCategory().getSlug();
		this.category_name = entity.getCategory().getName();

		this.tag_names = new ArrayList<>();
		this.tag_slugs = new ArrayList<>();
		for (Tag tag : entity.getTags()) {
			TagDto dto = new TagDto(tag);
			this.tag_names.add(dto.getName());
			this.tag_slugs.add(dto.getSlug());
		}

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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public String getAuthor_slug() {
		return author_slug;
	}

	public void setAuthor_slug(String author_slug) {
		this.author_slug = author_slug;
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

	public List<String> getTag_names() {
		return tag_names;
	}

	public void setTag_names(List<String> tag_names) {
		this.tag_names = tag_names;
	}

	public List<String> getTag_slugs() {
		return tag_slugs;
	}

	public void setTag_slugs(List<String> tag_slugs) {
		this.tag_slugs = tag_slugs;
	}

}
