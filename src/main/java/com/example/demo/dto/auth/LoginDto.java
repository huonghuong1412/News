package com.example.demo.dto.auth;

import com.example.demo.model.User;

public class LoginDto {

	private String email;
	private String password;

	public LoginDto() {
		super();
	}

	public LoginDto(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
