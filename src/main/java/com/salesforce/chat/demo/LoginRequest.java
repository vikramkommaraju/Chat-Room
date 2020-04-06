package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class LoginRequest {

	private String username;
	private String password;
	private String endpoint;
	
	public LoginRequest() {}
	
	public void setUsername(String userName) {
		this.username = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}	
}
