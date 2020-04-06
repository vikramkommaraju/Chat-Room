package com.salesforce.chat.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.google.common.eventbus.EventBus;

@Controller
public class LogoutHandler {
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	EnterpriseService entService;
		
	@MessageMapping("/logout")
    @SendTo("/topic/logout")
    public LogoutResponse getSessionId(LogoutRequest request) throws Exception {
		System.out.println("Received log out request for : " + request.getUsername());
		eventBus.post(new UserLoggedOutEvent(request.getUsername()));
		return new LogoutResponse(request.getUsername());
	}
}
