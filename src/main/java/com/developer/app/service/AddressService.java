package com.developer.app.service;

import java.util.List;

import com.developer.app.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);
	AddressDto getAddress(String addressId);
}
