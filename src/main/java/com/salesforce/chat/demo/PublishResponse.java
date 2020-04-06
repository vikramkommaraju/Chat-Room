package com.salesforce.chat.demo;

public class PublishResponse {
	String username;
	String message;
	String eventId;
	
	public PublishResponse() {}
	
	public PublishResponse(String username, String message, String eventId) {
		this.username = username;
		this.message = message;
		this.eventId = eventId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}	
}
