package com.developer.app.ui.model.constants;

public enum ErrorMessages {

	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required field"),
	RECORD_ALREADY_EXIST("Record already exist"), 
	INTERNAL_SERVER_ERROR("Internal server error"),
	NO_RECORD_FOUND("Record with provided id is not found"), 
	AUTHENTICATION_FIELD("Authentication field"),
	COULD_NOT_UPDATE_RECORD("Could not update record"), 
	COULD_NOT_DELETE_RECORD("Could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified");

	private String errorMessage;

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
