package com.salesforce.chat.demo;

import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

public class PublishStatusEvent {

	@SerializedName(value = "Status")
	String status;
	
	@SerializedName(value = "Topic")
	String topic;
	
	@SerializedName(value = "PublishStatusDetails")
	PublishStatusDetails[] publishStatusDetails;
	
	
	public String getStatus() {
		return status;
	}


	public String getTopic() {
		return topic;
	}


	public PublishStatusDetails[] getPublishResults() {
		return publishStatusDetails;
	}


	@Override
	public String toString() {
		return "PublishStatusEvent [status=" + status + ", topic=" + topic + ", results=" + Arrays.toString(publishStatusDetails)
				+ "]";
	}
}
