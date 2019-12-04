package com.senczyk.lordOfGame;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;


@Configuration
@EnableWebSocketMessageBroker
public class WebConfiguration implements WebSocketMessageBrokerConfigurer {
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		/*registry.addEndpoint("/secured/user/getPlayers").setHandshakeHandler(new DefaultHandshakeHandler() {
			 
		      public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,Map attributes) throws Exception {
		  
		            if (request instanceof ServletServerHttpRequest) {
		                ServletServerHttpRequest servletRequest
		                 = (ServletServerHttpRequest) request;
		                HttpSession session = servletRequest
		                  .getServletRequest().getSession();
		                attributes.put("sessionId", session.getId());
		            }
		            return true;
		        }}).withSockJS(); // <- filter methods via @MessageMapping("/get/Board1") annotation
		*/
		registry.addEndpoint("/getPlayers", "/movement").withSockJS();
	}
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/ws"); // <- feedback to client
		//registry.setApplicationDestinationPrefixes("/user"); 
	}
	
}
