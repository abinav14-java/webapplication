package com.abinav.webapplication.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.abinav.webapplication.model.Users;

public interface UserService {

	Users createUser(Users user) throws Exception;

	boolean existsByEmail(String email) throws Exception;

	UserDetails loadUserByUsername(String email) throws Exception;

}
