package com.example.demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_tag")
public class Tag extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "slug")
	private String slug;

	@ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
	private List<Post> posts;

	public Tag(String name, String slug) {
		super();
		this.name = name;
		this.slug = slug;
	}

	public Tag() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

}
