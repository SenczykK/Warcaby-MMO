package com.senczyk.lordOfGame.controllers;


import java.time.LocalDate;
import java.util.*;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.senczyk.lordOfGame.entities.*;

@CrossOrigin
@RestController
public class MessageController {
	
	@Autowired
	private PlayerListRepository playerListRepo;
	@Autowired
	private GamesRepository gamesRepo;
	@Autowired
	private SimpMessagingTemplate msgTemplate;

	Gson gson = new Gson();
	
	/**
	 * Method that is a proxy between two player 
	 * to send answer about new game
	 * 
	 * @author ksenczyk
	 * @param data - message in JSON format using MessageTemplate
	 */
	@MessageMapping("/answer")
	@SendTo("/ws/listener")
	public void sendAnswer(@RequestBody String data) {
		MessageTemplate message = gson.fromJson(data, MessageTemplate.class);
		message.printMessage();
		
		if( message.getOption().contentEquals("reject") ) {
			System.out.println("Usuwam zapytanie graczy ");message.printMessage();
		} else {
			//gamesRepo.save(new GameEntity( new PlayerEntity(message.getAuthor()), new PlayerEntity(message.getReciver())));
			msgTemplate.convertAndSend("/ws/listener", data);
		}
	}
	
	/**
	 * Method that ask opponent to play a new game
	 * 
	 * @author ksenczyk
	 * @param data - message in JSON format using MessageTemplate
	 */
	@MessageMapping("/askPlayer")
	public void askPlayerToPlay(@RequestBody String data) {
		MessageTemplate message = gson.fromJson(data, MessageTemplate.class);
		message.printMessage();
		
		if(message.getAuthor().isEmpty() && message.getReciver().isEmpty()) {
			System.out.println("Message error: no players");
			//msgTemplate.convertAndSend("/ws/listener", gson.toJson(temp) );
		} else {
			//wysyłam pytanie do 2go gracza
			System.out.println("Gracz "+message.getAuthor()+" wyzywa gracza "+message.getReciver() );
			msgTemplate.convertAndSend("/ws/listener", data);
		}
	}
	
	/**
	 * Mehod that get a list of players who login in last day
	 * 
	 * @return String JSON list of player names 
	 */
	@MessageMapping("/getPlayers")
	@SendTo("/ws/getPlayers")
	public String sendPlayersList() {
		List<PlayerEntity> toDeletePlayerList = playerListRepo.findAll().stream().filter( p -> {
			if(p.getLastLogin().isBefore(LocalDate.now()))
				return true;
			else return false;
					}).collect(Collectors.toList());
		playerListRepo.deleteAll(toDeletePlayerList);
		return gson.toJson(playerListRepo.findAll().stream()
				.map( p -> {return p.getName();})
				.collect(Collectors.toList()));
	}
}
