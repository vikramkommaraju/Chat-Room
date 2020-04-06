package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class LoginResponse {

	private String userName;
	private String sessionId;
	
	public LoginResponse() {}

	public LoginResponse(String userName, String sessionId) {
		this.userName = userName;
		this.sessionId = sessionId;
	}


	public String getUsername() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}	
	
}
