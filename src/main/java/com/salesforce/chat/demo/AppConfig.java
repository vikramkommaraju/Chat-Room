package com.salesforce.chat.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;

@Configuration
public class AppConfig {
	
	@Bean
    public EventBus eventBus() {
        return new EventBus();
    }
	
}
