package com.salesforce.chat.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.google.common.eventbus.EventBus;
import com.sforce.soap.enterprise.LoginResult;

@Controller
public class LoginHandler {
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	EnterpriseService entService;
		
	@MessageMapping("/login")
    @SendTo("/topic/login")
    public LoginResponse getSessionId(LoginRequest request) throws Exception {
		LoginResult result = entService.login(request.getUsername(), request.getPassword(), request.getEndpoint());
		eventBus.post(new UserLoggedInEvent(request.getUsername(), request.getPassword(), request.getEndpoint()));
		return new LoginResponse(request.getUsername(), result.getSessionId());
	}
}
