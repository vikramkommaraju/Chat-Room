package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class LogoutRequest {

	private String username;
	
	public LogoutRequest() {}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
