package com.example.demo.dto;

public class ResponseMessage {

	private String status;
	private String message;

	public ResponseMessage(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public ResponseMessage() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
