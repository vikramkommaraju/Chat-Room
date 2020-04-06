package com.salesforce.chat.demo;

import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

public class MessageEvent {

	@SerializedName(value = "PE_TEXT2__c")
	String pe_text2__c;
	
	@SerializedName(value = "CreatedById")
	String createdById;

	public String getPe_text2__c() {
		return pe_text2__c;
	}

	public void setPe_text2__c(String pe_text2__c) {
		this.pe_text2__c = pe_text2__c;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	@Override
	public String toString() {
		return "MessageEvent [pe_text2__c=" + pe_text2__c + ", createdById=" + createdById + "]";
	}
}
