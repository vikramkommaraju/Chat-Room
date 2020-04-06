package com.salesforce.chat.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TypingHandler {

	@MessageMapping("/typing")
    @SendTo("/topic/typing")
    public TypingResponse handleIsTyping(IsTypingRequest request) throws Exception {
		return new TypingResponse(request.getUsername());
	}
	
	@MessageMapping("/stoppedtyping")
    @SendTo("/topic/stoppedtyping")
    public TypingResponse handleStoppedTyping(StoppedTypingRequest request) throws Exception {
		return new TypingResponse(request.getUsername());
	}
}
