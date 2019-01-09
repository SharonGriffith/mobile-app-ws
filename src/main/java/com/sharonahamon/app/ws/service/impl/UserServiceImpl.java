package com.sharonahamon.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharonahamon.app.ws.io.repositories.UserRepository;
import com.sharonahamon.app.ws.io.entity.UserEntity;
import com.sharonahamon.app.ws.service.UserService;
import com.sharonahamon.app.ws.shared.Utils;
import com.sharonahamon.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder; 

	@Override
	public UserDto createUser(UserDto user) {
		
		if (user != null &&
			user.getEmail() != null)
		{
			UserEntity foundUser = userRepository.findByEmail(user.getEmail());
			
			if (foundUser != null) {
				throw new RuntimeException("email already exists");
			}
		}
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		// generated
		userEntity.setUserId(utils.generateUserId(30));
		
		// generated
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserEntity = userRepository.save(userEntity);
		
		UserDto returnUser = new UserDto();
		BeanUtils.copyProperties(storedUserEntity, returnUser);
		
		return returnUser;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity foundUser = userRepository.findByEmail(email);
		
		if (foundUser == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new User(foundUser.getEmail(), foundUser.getEncryptedPassword(), new ArrayList());
	}
	
	@Override
	public UserDto getUser(String email) throws UsernameNotFoundException {
		UserEntity foundUser = userRepository.findByEmail(email);
		
		if (foundUser == null) {
			throw new UsernameNotFoundException(email);
		}
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(foundUser, returnValue);
		
		return returnValue;
	}
}