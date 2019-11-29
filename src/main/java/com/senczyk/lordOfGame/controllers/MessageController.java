package com.senczyk.lordOfGame.controllers;

import java.lang.reflect.Type;
import java.util.*;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.senczyk.lordOfGame.entities.*;

@CrossOrigin
@RestController
public class MessageController {

	private List<PlayerEntity> playerList;
	private List<GameEntity> startedGames;
	
	@Autowired
	private PlayerListRepository playerListRepo;
	@Autowired
	private GamesRepository gamesRepo;
	@Autowired
	private SimpMessagingTemplate msgTemplate;

	Gson gson = new Gson();
	
	@PostConstruct
	private void initiation() {
		playerList = playerListRepo.findAll();
		startedGames = new ArrayList<>();
		// startedGames from DB
		
	}
	@MessageMapping("/startGame")
	public String startGameAtServer() {
		
		return "";
	}
	
	@MessageMapping("/askPlayer")
	@SendTo("/ws/askPlayer")
	public String askPlayerToPlay(@RequestBody String data, String choise) {

		Type listType = new TypeToken<ArrayList<PlayerEntity>>(){}.getType();
		List<PlayerEntity> temp = gson.fromJson(data, listType);
		
		if(temp.get(0).getName().isEmpty() && temp.get(1).getName().isEmpty()) {
			System.out.println("Message error: no players");
			return gson.toJson("Message error: no players");
		} else {
			msgTemplate.convertAndSend("/ws/listener", gson.toJson(temp) );
			
			//return YES/NO
			return gson.toJson("asking and Starting new game...");
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
		List<GameEntity> result = startedGames.stream().filter( game -> 
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
