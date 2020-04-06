package com.salesforce.chat.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.sforce.soap.enterprise.SaveResult;

@Controller
public class PublishMessageHandler {

	@Autowired
	EnterpriseService entService;
	
	@Autowired
    private SimpMessagingTemplate template;
	
	
	private ExecutorService publishExecutor = Executors.newFixedThreadPool(5);
	
	@MessageMapping("/publish")
	@SendTo("/topic/publish")
    public PublishResponse publishMessage(PublishRequest request) throws Exception {
		String generatedUuid = java.util.UUID.randomUUID().toString();
		
		publishExecutor.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				SaveResult[] results = entService.publish(request.getUsername(), request.getMessage());
				List<String> eventIds = parseResults(results);
				String eventId = eventIds.size() > 0 ? eventIds.get(0) : null;
				template.convertAndSend("/topic/publishComplete", new PublishCompletedResponse(request.getUsername(), generatedUuid, eventId));
				return eventId;
			}
		});
		
		return new PublishResponse(request.getUsername(), request.getMessage(), generatedUuid);
				
	}
	
	private List<String> parseResults(com.sforce.soap.enterprise.SaveResult[] saveResults) {
		List<String> eventIds = new ArrayList<>();
		for (int i = 0; i < saveResults.length; i++) {
			if(!saveResults[i].isSuccess()) {
				com.sforce.soap.enterprise.Error[] errors = saveResults[i].getErrors();
				for(int j=0; j<errors.length; j++) {
					System.out.println("Error: " + errors[i].getStatusCode() + " message: " + errors[i].getMessage());
				}
			}
			com.sforce.soap.enterprise.Error[] errors = saveResults[i].getErrors();
			for(int j=0; j<errors.length; j++) {
				eventIds.add(errors[j].getMessage());
			}
		}
		return eventIds;
	}
}
