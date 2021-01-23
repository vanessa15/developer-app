package com.developer.app.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.developer.app.exception.UserServiceException;
import com.developer.app.service.AddressService;
import com.developer.app.service.UserService;
import com.developer.app.shared.dto.AddressDto;
import com.developer.app.shared.dto.UserDto;
import com.developer.app.ui.model.constants.ErrorMessages;
import com.developer.app.ui.model.constants.RequestOperationName;
import com.developer.app.ui.model.constants.RequestOperationStatus;
import com.developer.app.ui.model.request.UserDetailsRequestModel;
import com.developer.app.ui.model.response.AddressResponseModel;
import com.developer.app.ui.model.response.OperationStatusModel;
import com.developer.app.ui.model.response.UserDetailsResponseModel;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserDetailsResponseModel getUser(@PathVariable String id) {

		UserDetailsResponseModel response = new UserDetailsResponseModel();
		UserDto getUser = userService.getUserId(id);
		BeanUtils.copyProperties(getUser, response);

		return response;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel request) throws Exception {

		if (request.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(request, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		UserDetailsResponseModel response = modelMapper.map(createdUser, UserDetailsResponseModel.class);

		return response;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserDetailsResponseModel updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel request) {
		UserDetailsResponseModel response = new UserDetailsResponseModel();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(request, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, response);

		return response;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return response;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserDetailsResponseModel> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "25") int limit) {
		List<UserDetailsResponseModel> response = new ArrayList<UserDetailsResponseModel>();

		List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto user : users) {
			UserDetailsResponseModel userRes = new UserDetailsResponseModel();
			BeanUtils.copyProperties(user, userRes);
			response.add(userRes);
		}

		return response;
	}

	@GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public CollectionModel<AddressResponseModel> getListOfAddressesByUserId(@PathVariable String userId) {

		List<AddressResponseModel> response = new ArrayList<AddressResponseModel>();
		List<AddressDto> addressDto = addressService.getAddresses(userId);

		if (addressDto != null && !addressDto.isEmpty()) {

			Type listType = new TypeToken<List<AddressResponseModel>>() {
			}.getType();
			response = new ModelMapper().map(addressDto, listType);
			
			for (AddressResponseModel address: response) {
				Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
						.getAddressByAddressId(userId, address.getAddressId())).withSelfRel();
				
				address.add(selfLink);
			}
		}

		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getListOfAddressesByUserId(userId))
				.withSelfRel();
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUser(userId))
				.withRel("user");
		return CollectionModel.of(response, selfLink, userLink);
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public EntityModel<AddressResponseModel> getAddressByAddressId(@PathVariable String userId,
			@PathVariable String addressId) {

		AddressResponseModel response = new AddressResponseModel();
		AddressDto addressDto = addressService.getAddress(addressId);

		response = new ModelMapper().map(addressDto, AddressResponseModel.class);

		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getAddressByAddressId(userId, addressId)).withSelfRel();
		Link addressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getListOfAddressesByUserId(userId))
				.withRel("addresses");
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUser(userId))
				.withRel("user");

		return EntityModel.of(response, Arrays.asList(selfLink, addressesLink, userLink));
		
	}

}
