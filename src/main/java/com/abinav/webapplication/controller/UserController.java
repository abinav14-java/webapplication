package com.abinav.webapplication.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abinav.webapplication.logic.UserLogic;
import com.abinav.webapplication.model.LoginResponse;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.utility.JwtUtil;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserLogic userLogic;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<LoginResponse> createUser(@RequestBody Users user) throws Exception {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		Users savedUser = userLogic.createUser(user);
		String token = jwtUtil.generateToken(savedUser.getEmail());
//		return userLogic.createUser(user);
		
		return ResponseEntity.ok(new LoginResponse(token));
	}

	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) throws Exception {
		boolean exists = userLogic.existsByEmail(email);
		return ResponseEntity.ok(Collections.singletonMap("exists", exists));
	}

	/**
	 * Update current user's profile photo (provide imageUrl in body)
	 * PUT /api/users/profile/photo
	 */
	@PutMapping("/profile/photo")
	public ResponseEntity<?> updateProfilePhoto(@RequestBody Map<String, String> body, Authentication auth) throws Exception {
		String imageUrl = body.get("imageUrl");
		if (imageUrl == null) return ResponseEntity.badRequest().body(Collections.singletonMap("message", "imageUrl required"));
		String email = auth.getName();
		Users user = userLogic.findByEmail(email).orElse(null);
		if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "User not found"));
		user.setProfilePhotoUrl(imageUrl);
		userLogic.updateUser(user);
		return ResponseEntity.ok(Collections.singletonMap("message", "Profile photo updated"));
	}

	/**
	 * Search users by username or email
	 * GET /api/users/search?query=...
	 */
	@GetMapping("/search")
	public ResponseEntity<?> searchUsers(@RequestParam String query) throws Exception {
		java.util.List<Users> results = userLogic.searchUsers(query);
		java.util.List<com.abinav.webapplication.dto.UserDTO> dtos = new java.util.ArrayList<>();
		for (Users u : results) {
			dtos.add(com.abinav.webapplication.utility.UserMapper.toDTO(u));
		}
		return ResponseEntity.ok(Collections.singletonMap("results", dtos));
	}

}
