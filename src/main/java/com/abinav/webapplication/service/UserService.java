package com.abinav.webapplication.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.abinav.webapplication.model.Users;

public interface UserService {

	Users createUser(Users user);

	boolean existsByEmail(String email);

	UserDetails loadUserByUsername(String email) throws Exception;

	java.util.Optional<Users> findByEmail(String email);

	java.util.Optional<Users> findById(Long id);

	Users updateUser(Users user);

	java.util.List<Users> searchUsers(String query);

}
