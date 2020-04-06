package com.salesforce.chat.demo;

import com.google.gson.annotations.SerializedName;

public class PublishStatusDetails {

	@SerializedName(value = "EventUuid")
	String eventUuid;
	
	@SerializedName(value = "Replay")
	long replay;
	
	@SerializedName(value = "FailureReason")
	String failureReason;
	
	
	public String getEventId() {
		return eventUuid;
	}


	public long getReplay() {
		return replay;
	}


	public String getReason() {
		return failureReason;
	}


	@Override
	public String toString() {
		return "PublishStatusDetails [eventId=" + eventUuid + ", replay=" + replay + ", reason=" + failureReason + "]";
	}
}
