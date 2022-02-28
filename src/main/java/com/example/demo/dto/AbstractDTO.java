package com.example.demo.dto;

import java.sql.Timestamp;
import java.util.Date;

public class AbstractDTO<T> {
	private Long id;
	private String createdDate = new Timestamp(new Date().getTime()).toString();
	private String updatedDate;

//	public AbstractDTO(Long id, String createdDate, String updatedDate) {
//		super();
//		this.id = id;
//		this.createdDate = createdDate;
//		this.updatedDate = updatedDate;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = new Timestamp(new Date().getTime()).toString();
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

}
