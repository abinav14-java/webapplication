package com.abinav.webapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abinav.webapplication.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email);

	Optional<Users> findByEmail(String email);

	// Search users by username or email (case-insensitive, partial match)
	java.util.List<Users> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);

}
