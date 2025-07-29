package com.abinav.webapplication.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.service.UserService;

@Component
public class UserLogic {
	@Autowired
	private UserService userService;

	public Users createUser(Users user) throws Exception{
		return userService.createUser(user);
		
	}

}
