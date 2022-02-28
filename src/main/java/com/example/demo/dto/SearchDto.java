package com.example.demo.dto;

public class SearchDto {
	private int pageIndex;
	private int pageSize;
	private String keyword;
	private String sortBy; // name
	private String tag; // tim theo tag
	private String category;
	private String source;
	private Integer display;

	public SearchDto() {
		super();
	}

	public SearchDto(int pageIndex, int pageSize, String keyword) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.keyword = keyword;
	}

	public SearchDto(int pageIndex, int pageSize, String keyword, String category, String source) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.keyword = keyword;
		this.category = category;
		this.source = source;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

}
