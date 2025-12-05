package com.abinav.webapplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String email;
	private String profilePhotoUrl;

	// Explicit getters/setters (for Lombok compatibility)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getProfilePhotoUrl() { return profilePhotoUrl; }
	public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }
}
