package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class TypingResponse {

	private String username;
	
	public TypingResponse() {}

	public TypingResponse(String username) {
		this.username = username;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
