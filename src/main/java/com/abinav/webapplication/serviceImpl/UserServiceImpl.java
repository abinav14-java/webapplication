package com.abinav.webapplication.serviceImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Users createUser(Users user) throws Exception {

		return userRepository.save(user);
	}

	@Override
	public boolean existsByEmail(String email) throws Exception {
		return userRepository.existsByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws Exception {
		Users user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

}