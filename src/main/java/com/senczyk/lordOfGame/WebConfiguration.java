package com.senczyk.lordOfGame;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebConfiguration implements WebSocketMessageBrokerConfigurer {
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/movement", "/getGame", "/getPlayers", "/answer", "/startGame", "/askPlayer").withSockJS(); // <- filter methods via @MessageMapping("/get/Board1") annotation
				
	}
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/ws"); // <- feedback to client
		//registry.setApplicationDestinationPrefixes("/app"); 
	}
	
}
