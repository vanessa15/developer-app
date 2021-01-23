package com.developer.app.security;

import com.developer.app.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME =  864000000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADERSTRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
	
}
