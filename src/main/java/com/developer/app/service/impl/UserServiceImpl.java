package com.developer.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.developer.app.io.entity.UserEntity;
import com.developer.app.repository.UserRepository;
import com.developer.app.service.UserService;
import com.developer.app.shared.Utils;
import com.developer.app.shared.dto.AddressDto;
import com.developer.app.shared.dto.UserDto;
import com.developer.app.ui.model.constants.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) throws Exception {

		if (userRepository.findByEmail(userDto.getEmail()) != null)
			throw new Exception("Record already exist");

		UserDto returnedValue = new UserDto();

		for (int i = 0; i < userDto.getAddresses().size(); i++) {
			AddressDto address = userDto.getAddresses().get(i);
			address.setUserDetails(userDto);
			address.setAddressId(utils.generatedAddressId(30));
			userDto.getAddresses().set(i, address);
		}

		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userEntity.setUserId(utils.generatedUserId(30));

		UserEntity storedValue = userRepository.save(userEntity);
		returnedValue = modelMapper.map(storedValue, UserDto.class);

		return returnedValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		UserDto returnedValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnedValue);

		return returnedValue;
	}

	@Override
	public UserDto getUserId(String id) {
		UserEntity userEntity = userRepository.findByUserId(id);

		if (userEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		UserDto returnedValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnedValue);

		return returnedValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());

		UserDto returnedValue = new UserDto();
		UserEntity updatedRecord = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedRecord, returnedValue);

		return returnedValue;
	}

	@Override
	public void deleteUser(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {

		List<UserDto> returnedValue = new ArrayList<UserDto>();

		if (page > 0)
			page -= 1;
		Pageable pageable = PageRequest.of(page, limit);
		Page<UserEntity> users = userRepository.findAll(pageable);
		List<UserEntity> userEntity = users.getContent();

		for (UserEntity user : userEntity) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			returnedValue.add(userDto);
		}

		return returnedValue;
	}

}
