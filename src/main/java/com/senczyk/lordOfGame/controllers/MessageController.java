package com.senczyk.lordOfGame.controllers;

import java.lang.reflect.Type;
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
import com.google.gson.reflect.TypeToken;
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
	
	@MessageMapping("/startGame")
	public String startGameAtServer() {
		//if get player2 player 1 pair start
		// if get "reject" return 0;
		return "";
	}
	
	@MessageMapping("/answer")
	@SendTo("/ws/listener")
	public void sendAnswer(@RequestBody String data) {
		Type listType = new TypeToken<ArrayList<PlayerEntity>>(){}.getType();
		List<PlayerEntity> temp = gson.fromJson(data, listType);
		System.out.println("/answer "+data);
		// check opponent decision
		if( temp.get(1).getName().contentEquals("reject") ) {
			msgTemplate.convertAndSend("/ws/listener", data );
			System.out.println("Usuwam zapytanie");
		} else {
			System.out.println("Rozpoczynam gre");
			// start new game
		}
	}
	
	@MessageMapping("/askPlayer")
	public void askPlayerToPlay(@RequestBody String data) {

		Type listType = new TypeToken<ArrayList<PlayerEntity>>(){}.getType();
		List<PlayerEntity> temp = gson.fromJson(data, listType);
		
		if(temp.get(0).getName().isEmpty() && temp.get(1).getName().isEmpty()) {
			System.out.println("Message error: no players");
			//msgTemplate.convertAndSend("/ws/listener", gson.toJson(temp) );
		} else {
			//wysyłam pytanie do 2go gracza
			System.out.println("Wysylam do gracza "+temp.get(1).getName());
			msgTemplate.convertAndSend("/ws/listener", gson.toJson(temp) );
		}
	}
	
	@MessageMapping("/getGame")
	@SendTo("/ws/getGame")
	public String sendGame(String m) {
		PlayerEntity players = new PlayerEntity();
		players = gson.fromJson(m, PlayerEntity.class);
		final String name = new String(players.getName());
		// get in message a names of players
		System.out.println("Looking a game for "+name+" player...");
		List<GameEntity> result = gamesRepo.findAll().stream().filter( game -> 
									game.getPlayer1().getName().contentEquals(name) ||
									game.getPlayer2().getName().contentEquals(name) )
								.collect(Collectors.toList());
		if(result.isEmpty()) {
			return "No game found.";
		}
		System.out.println("Found game with id:"+result.get(0));
		List<PawEntity> resultPaws = new LinkedList<>();
		resultPaws.addAll(result.get(0).getPlayer1().getPaws());
		resultPaws.addAll(result.get(0).getPlayer2().getPaws());
		return gson.toJson(resultPaws);
	}
	
	@MessageMapping("/getPlayers")
	@SendTo("/ws/getPlayers")
	public String sendPlayersList() {

		return gson.toJson(playerListRepo.findAll().stream()
				.map( p -> {return p.getName();})
				.collect(Collectors.toList()));
	}
}
