package com.example.demo.dto;

import com.example.demo.model.Source;

public class SourceDto extends AbstractDTO<SourceDto> {

	private String name;
	private String url_logo;

	public SourceDto() {
		super();
	}

	public SourceDto(Source entity) {
		super();
		this.setId(entity.getId());
		this.name = entity.getName();
		this.url_logo = entity.getUrl_logo();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl_logo() {
		return url_logo;
	}

	public void setUrl_logo(String url_logo) {
		this.url_logo = url_logo;
	}

}
