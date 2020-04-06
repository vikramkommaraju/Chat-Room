package com.salesforce.chat.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.streaming.demo.emp.EmpConnector;

@Component
public class SubscriptionManager {

	@Autowired
	PlatformEventListener listener;
		
	public SubscriptionManager(EventBus eventBus) {
		eventBus.register(this);
	}
	
	private Map<String, EmpConnector> userNameToConnectorMap = new HashMap<>();
	
	
	@Subscribe
	public void newUser(UserLoggedInEvent newUserEvent) throws Exception {
		userNameToConnectorMap.putIfAbsent(newUserEvent.getUsername(), listener.start(newUserEvent.getEndpoint(), newUserEvent.getUsername(), newUserEvent.getPassword()));					
		
	}
	
	@Subscribe
	public void logOut(UserLoggedOutEvent userLoggedOut) throws Exception {
		userNameToConnectorMap.get(userLoggedOut.getUsername()).stop();
		userNameToConnectorMap.remove(userLoggedOut.getUsername());		
	}
}
