package com.developer.app.shared.dto;

public class AddressDto {

	private long id;
	private String addressId;
	private String street;
	private String city;
	private String country;
	private String postal;
	private String type;
	private UserDto userDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public UserDto getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDto userDetails) {
		this.userDetails = userDetails;
	}

}
