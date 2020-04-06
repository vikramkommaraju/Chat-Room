package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class UserLoggedOutEvent {
	private String username;

	public UserLoggedOutEvent() {}
	
	public UserLoggedOutEvent(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
