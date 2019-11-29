package com.senczyk.lordOfGame.controllers;

import java.util.*;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
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
	
	Gson gson = new Gson();
	
	@PostConstruct
	private void initiation() {
		playerList = playerListRepo.findAll();
		startedGames = new ArrayList<>();
		// startedGames from DB
		
	}
	
	@MessageMapping("/askPlayer/{player1}/{player2}")
	@SendTo("/ws/askPlayer")
	public String askPlayerToPlay(@PathVariable("player1") String player1Name, @PathVariable("player1") String player2Name) {
		System.out.println("Get message:"+player1Name+" "+player2Name);
		if(player1Name.isEmpty() && player2Name.isEmpty()) {
			System.out.println("Message error: no players");
			return "Message error: no players";
		} else {
			return "Starting new game...";
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
		playerList = playerListRepo.findAll();
		return gson.toJson(playerList);
	}
}
