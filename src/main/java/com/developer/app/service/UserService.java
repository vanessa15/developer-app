package com.developer.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.developer.app.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto) throws Exception;
	UserDto getUser(String email);
	UserDto getUserId(String id);
	UserDto updateUser(String userId, UserDto userDto);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);
}
