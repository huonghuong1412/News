package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.auth.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDto getCurrentUser(Long id) {
		User user = userRepository.getById(id);
		UserDto dto = new UserDto(user);
		return dto;
	}
	
}
