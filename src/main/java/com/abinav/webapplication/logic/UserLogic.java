package com.abinav.webapplication.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.service.UserService;

@Component
public class UserLogic  implements UserDetailsService{
	@Autowired
	private UserService userService;

	public Users createUser(Users user) throws Exception {
		return userService.createUser(user);

	}

	public boolean existsByEmail(String email) throws Exception {
		return userService.existsByEmail(email);
	}

	public UserDetails loadUserByUsername(String email) {
		try {
			return userService.loadUserByUsername(email);
		} catch (Exception e) {
			return null;		}
		
	}

}
