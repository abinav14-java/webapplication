package com.abinav.webapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abinav.webapplication.logic.UserLogic;
import com.abinav.webapplication.model.Users;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserLogic userLogic;
	
	 @PostMapping
	 @ResponseStatus(code = HttpStatus.CREATED)
	    public Users createUser(@RequestBody Users user) throws Exception {
	        return userLogic.createUser(user);
	    }

}
