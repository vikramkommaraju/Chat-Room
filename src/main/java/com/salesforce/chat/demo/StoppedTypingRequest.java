package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class StoppedTypingRequest {

	private String username;
	
	public StoppedTypingRequest() {}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
