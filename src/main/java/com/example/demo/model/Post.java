package com.example.demo.model;

import java.util.List;

//@Entity
//@Table(name = "tbl_post")
public class Post {

//	@Column(name = "title")
	private String title;

//	@Column(name = "slug")
	private String slug;

//	@Column(name = "image")
	private String image;

//	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

//	@Column(name = "display")
	private Integer display; // 1 : show, 0: hidden

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "category_id")
	private NewsCategory category;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "author_id")
	private Author author;

//	@ManyToMany(cascade = CascadeType.PERSIST)
//	@JoinTable(name = "tbl_post_tag", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	public Post() {
		super();
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

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public NewsCategory getCategory() {
		return category;
	}

	public void setCategory(NewsCategory category) {
		this.category = category;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

}
