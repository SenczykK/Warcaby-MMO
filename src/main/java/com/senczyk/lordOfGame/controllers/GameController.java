package com.senczyk.lordOfGame.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import com.senczyk.lordOfGame.entities.*;

@CrossOrigin
@RestController
public class GameController {
	
	@Autowired
	private PlayerListRepository playerListRepo;
	
	Gson gson = new Gson();

	
	@PostMapping("/destroy")
	public void cancelGame(@RequestBody String data) {
		
		MessageTemplate message = gson.fromJson(data, MessageTemplate.class);
		
		message.printMessage();
	}

}
