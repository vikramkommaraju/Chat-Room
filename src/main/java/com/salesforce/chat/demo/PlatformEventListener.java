package com.salesforce.chat.demo;

import static org.cometd.bayeux.Channel.META_CONNECT;
import static org.cometd.bayeux.Channel.META_DISCONNECT;
import static org.cometd.bayeux.Channel.META_HANDSHAKE;
import static org.cometd.bayeux.Channel.META_SUBSCRIBE;
import static org.cometd.bayeux.Channel.META_UNSUBSCRIBE;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jetty.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.streaming.demo.emp.BayeuxParameters;
import com.streaming.demo.emp.BearerTokenProvider;
import com.streaming.demo.emp.EmpConnector;
import com.streaming.demo.emp.LoggingListener;
import com.streaming.demo.emp.LoginHelper;

@Component
public class PlatformEventListener {
	
	@Autowired
    private SimpMessagingTemplate template;
	
	private final AtomicInteger numStatusEvents = new AtomicInteger(0);
	private final Map<String, Long> topicToReplayMap = Maps.newHashMap();
	private final Map<String, Integer> acksCountPerTopic = Maps.newHashMap();
	private final Gson gson = new Gson();
	
	public EmpConnector start(String endPoint, String userName, String password) throws Exception {
		BearerTokenProvider tokenProvider = new BearerTokenProvider(getTokenSupplier(endPoint, userName, password));
		BayeuxParameters params = tokenProvider.login();
		
		EmpConnector connector = new EmpConnector(params);
        LoggingListener loggingListener = new LoggingListener(true, true);

        connector.addListener(META_HANDSHAKE, loggingListener)
                .addListener(META_CONNECT, loggingListener)
                .addListener(META_DISCONNECT, loggingListener)
                .addListener(META_SUBSCRIBE, loggingListener)
                .addListener(META_UNSUBSCRIBE, loggingListener);

        connector.setBearerTokenProvider(tokenProvider);
        connector.start().get(5, TimeUnit.SECONDS);	
		connector.subscribe("/event/PublishStatusEvent", -1L, getStatusEventConsumer());
		connector.subscribe("/event/PE2_HV__e", -1L, getEventConsumer());

		return connector;
	}
	
	private Consumer<Map<String, Object>> getEventConsumer() { 
		return (event) -> {
			MessageEvent msg = gson.fromJson(JSON.toString(event.get("payload")), MessageEvent.class);
			System.out.println("Message = " + msg);
			String sender = null;
			if(msg.getCreatedById().equals("005xx000001X88PAAS")) {
				sender = "test@pub1.org";
			} else {
				sender = "test@pub2.org";
			}
			String message = msg.getPe_text2__c();
			newEvent(sender, message);
			
		};
	}
	
	private Consumer<Map<String, Object>> getStatusEventConsumer() {
		return (event) -> {
			System.out.println(event);
			numStatusEvents.addAndGet(1);
			PublishStatusEvent statusEvent = gson.fromJson(JSON.toString(event.get("payload")), PublishStatusEvent.class);
			System.out.println("--------------------------------------");
			PublishStatusDetails[] publishResults = statusEvent.getPublishResults();
			for(int i=0; i<publishResults.length; i++) {
				if(statusEvent.getStatus().equalsIgnoreCase("Success")) {
					ack(publishResults[i].getEventId());					
				} else {
					nack(publishResults[i].getEventId());
				}
			}
			Integer count = acksCountPerTopic.get(statusEvent.getTopic());
			if(count == null) {
				count = publishResults.length;
			} else {
				count += publishResults.length;
			}
			acksCountPerTopic.put(statusEvent.getTopic(), count);
			topicToReplayMap.putIfAbsent(statusEvent.getTopic(), publishResults[0].getReplay());
			printMap();
			System.out.println("numStatusEvents=" + numStatusEvents);
			System.out.println("TopicReplayMap=" + topicToReplayMap);
			System.out.println("--------------------------------------");
		};
	}
	
	public void ack(String eventUuid) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		this.template.convertAndSend("/topic/acks", eventUuid);
	}
	
	public void nack(String eventUuid) {
		this.template.convertAndSend("/topic/nacks", eventUuid);
	}
	
	public void newEvent(String sender, String message) {
		this.template.convertAndSend("/topic/newMessage", new NewMessageResponse(sender, message, java.util.UUID.randomUUID().toString()));
	}
	
	private void printMap() {
		for(Entry<String, Integer> entry : acksCountPerTopic.entrySet()) {
			System.out.println(entry);
		}
		
	}
	
	private Supplier<BayeuxParameters> getTokenSupplier(String endPoint, String userName, String password) {
		return () -> {
            try {
                return LoginHelper.login(new URL(endPoint), userName, password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
	}
}
