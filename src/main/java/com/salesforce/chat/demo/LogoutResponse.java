package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class LogoutResponse {

	private String username;
	
	public LogoutResponse() {}

	public LogoutResponse(String username) {
		this.username = username;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
