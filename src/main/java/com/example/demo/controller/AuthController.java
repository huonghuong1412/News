package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.Erole;
import com.example.demo.dto.auth.JwtResponse;
import com.example.demo.dto.auth.LoginDto;
import com.example.demo.dto.auth.MessageResponse;
import com.example.demo.dto.auth.RegisterDto;
import com.example.demo.dto.auth.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserDetailsImpl;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Validated @RequestBody LoginDto dto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		if (userDetails.getRoles().contains(new String("ROLE_ADMIN"))) {
			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(),
					userDetails.getEmail(), roles));
		} else {
//			return new ResponseEntity<MessageResponse>(
//					new MessageResponse("Truy cập bị từ chối! Tài khoản hoặc mật khẩu không chính xác!"),
//					HttpStatus.BAD_REQUEST);
			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(),
					userDetails.getEmail(), roles));
		}

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Validated @RequestBody RegisterDto dto) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Email đã được đăng ký!"));
		}

		User user = new User(dto.getEmail(), encoder.encode(dto.getPassword()), dto.getFullName());
		user.setDisplay(1);

		List<String> strRoles = dto.getRole();
		List<Role> roles = new ArrayList<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findOneByName(Erole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findOneByName(Erole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(adminRole);
					break;
				default:
					Role userRole = roleRepository.findOneByName(Erole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
					break;
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Đăng ký tài khoản thành công!"));
	}

	@PutMapping("/update-password")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SELLER')")
	public ResponseEntity<?> updatePassword(@Validated @RequestBody RegisterDto dto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		User user = userRepository.findOneByEmail(username);
		String oldPassword = dto.getPassword();
//		String newPassword = dto.getPasswordNew();
		if (encoder.matches(oldPassword, user.getPassword())) {
//			user.setPassword(encoder.encode(newPassword));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Mật khẩu cũ không chính xác!"));
		}
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Cập nhật mật khẩu thành công!"));
	}

	@GetMapping("/info")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SELLER')")
	public ResponseEntity<UserDto> getNewById() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		UserDto result = userService.getCurrentUser(userDetails.getId());
		return new ResponseEntity<UserDto>(result, HttpStatus.OK);
	}
}