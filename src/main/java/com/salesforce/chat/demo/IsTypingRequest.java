package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class IsTypingRequest {

	private String username;
	
	public IsTypingRequest() {}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
