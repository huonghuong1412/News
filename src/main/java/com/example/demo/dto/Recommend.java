package com.example.demo.dto;

public class Recommend {
	private Double value;
	private Integer index;

	public Recommend() {
		super();
	}

	public Recommend(Double value, Integer index) {
		super();
		this.value = value;
		this.index = index;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
