package com.developer.app.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET= "0123456789abcdefghijklmnopqrstuvwxy";
	
	public String generatedUserId(int len) {
		return generatedRandomId(len);
	}
	
	public String generatedAddressId(int len) {
		return generatedRandomId(len);
	}
	
	private String generatedRandomId(int len) {
		
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(sb);
	}
}
