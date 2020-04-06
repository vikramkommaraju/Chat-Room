package com.salesforce.chat.demo;

import org.springframework.stereotype.Component;

@Component
public class AckResponse {
	String username;
	String eventId;
	
	public AckResponse() {}
	
	public AckResponse(String username, String eventId) {
		this.username = username;
		this.eventId = eventId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}	
}
