package com.salesforce.chat.demo;

public class PublishCompletedResponse {
	String username;
	String msgId;
	String eventId;
	
	public PublishCompletedResponse(String username, String msgId, String eventId) {
		this.username = username;
		this.msgId = msgId;
		this.eventId = eventId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	
}
