package com.senczyk.lordOfGame.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.senczyk.lordOfGame.entities.BoardTemplate;

@RestController
public class GameMessageController {
	
	Gson gson = new Gson();
	
	@MessageMapping("/movement")
	@SendTo("/ws/updateGameboard")
	public String updateGameboard(@RequestBody String data) {
		BoardTemplate board = gson.fromJson(data, BoardTemplate.class);
	
		return data;
	}
}
