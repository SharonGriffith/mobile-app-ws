package com.sharonahamon.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharonahamon.app.ws.UserRepository;
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
		
		// will be generated
		userEntity.setUserId(utils.generateUserId(30));
		
		// will be generated
		userEntity.setEncryptedPassword("test");
		
		UserEntity storedUserEntity = userRepository.save(userEntity);
		
		UserDto returnUser = new UserDto();
		BeanUtils.copyProperties(storedUserEntity, returnUser);
		
		return returnUser;
	}
}