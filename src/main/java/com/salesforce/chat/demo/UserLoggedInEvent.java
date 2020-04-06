package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class UserLoggedInEvent {
	private String username;
	private String password;
	private String endpoint;

	public UserLoggedInEvent() {}
	
	public UserLoggedInEvent(String username, String password, String endpoint) {
		this.username = username;
		this.password = password;
		this.endpoint = endpoint;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
