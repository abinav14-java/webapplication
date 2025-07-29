package com.abinav.webapplication.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Users createUser(Users user) throws Exception {
		
		return userRepository.save(user);
	}

}