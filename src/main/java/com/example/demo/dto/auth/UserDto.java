package com.example.demo.dto.auth;

import com.example.demo.dto.AbstractDTO;
import com.example.demo.model.User;

public class UserDto extends AbstractDTO<UserDto> {
	private String email;
	private String fullName;
	private Integer display;

	public UserDto() {
		super();
	}

	public UserDto(User user) {
		super();
		this.setId(user.getId());
		this.email = user.getEmail();
		this.fullName = user.getFullname();
		this.display = user.getDisplay();
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

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

}
