package com.abinav.webapplication.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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

}
