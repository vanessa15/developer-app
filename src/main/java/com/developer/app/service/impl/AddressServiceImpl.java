package com.developer.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developer.app.io.entity.AddressEntity;
import com.developer.app.io.entity.UserEntity;
import com.developer.app.repository.AddressRepository;
import com.developer.app.repository.UserRepository;
import com.developer.app.service.AddressService;
import com.developer.app.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userReposity;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDto> getAddresses(String id) {
		List<AddressDto> returnValue = new ArrayList<AddressDto>();
		ModelMapper modelMapper = new ModelMapper();

		UserEntity userEntity = userReposity.findByUserId(id);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

		for (AddressEntity address : addresses) {
			returnValue.add(modelMapper.map(address, AddressDto.class));
		}
		//sdda

		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto returnValue = new AddressDto();
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if (addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}

}
