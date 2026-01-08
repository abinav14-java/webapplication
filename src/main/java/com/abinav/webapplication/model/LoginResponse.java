package com.abinav.webapplication.model;

public class LoginResponse {
	private String token;
	private Long userId;
	private String username;

	public LoginResponse(String token, Long userId, String username) {
		this.token = token;
		this.userId = userId;
		this.username = username;
	}

	public LoginResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
