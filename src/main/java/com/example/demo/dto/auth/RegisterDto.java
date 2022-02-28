package com.example.demo.dto.auth;

import java.util.List;

import com.example.demo.dto.AbstractDTO;
import com.example.demo.model.User;

public class RegisterDto extends AbstractDTO<RegisterDto> {
	private String password;
	private String email;
	private String fullName;
	private List<String> role;

	public RegisterDto() {
		super();
	}

	public RegisterDto(User entity) {
		super();
		this.setId(entity.getId());
		this.password = entity.getPassword();
		this.email = entity.getEmail();
		this.fullName = entity.getFullname();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

}
