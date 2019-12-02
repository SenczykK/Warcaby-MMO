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
	
	// Checking an answer to play a game. If accept, save to db new game.
	@MessageMapping("/answer")
	public void sendAnswer(@RequestBody String data) {
		Type listType = new TypeToken<ArrayList<PlayerEntity>>(){}.getType();
		List<PlayerEntity> temp = gson.fromJson(data, listType);
		
		// check opponent decision
		if( temp.get(2).getName().contentEquals("reject") ) {
			msgTemplate.convertAndSend("/ws/listener", data );
			System.out.println("Usuwam zapytanie graczy "+temp);
		} else {
			gamesRepo.save(new GameEntity( temp.get(0), temp.get(1)));
			msgTemplate.convertAndSend("/ws/listener", data);
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
			System.out.println("Gracz "+temp.get(0).getName()+" wyzywa gracza "+temp.get(1).getName() );
			msgTemplate.convertAndSend("/ws/listener", gson.toJson(temp) );
		}
	}
	
	@MessageMapping("/movement")
	@SendTo("/ws/lastMove")
	public String sendGame(String data) {
		
		System.out.println(gson.fromJson(data, Movement.class));
		
		
		
		
		
		return gson.toJson("");
		/*
		
		Type listType = new TypeToken<ArrayList<PlayerEntity>>(){}.getType();
		List<PlayerEntity> temp = gson.fromJson(data, listType);
		//List<GameEntity> resultGame = gamesRepo.findAll().stream().filter( g ->  g.getPlayer1().getName().contentEquals(temp.get(0).getName()) ).collect(Collectors.toList());
		List<GameEntity> resultGame = gamesRepo.findAll().stream().filter( g ->  g.getPlayer1().getName().contentEquals("TomekW") ).collect(Collectors.toList());
		System.out.println("try to stream");
		resultGame.stream().forEach( t -> {
			System.out.println("Game:");
			System.out.println("Player1:"+t.getPlayer1().getName()+" "+t.getPlayer1().getWhiteBlack());
			System.out.println("Player2:"+t.getPlayer2().getName()+" "+t.getPlayer2().getWhiteBlack());
		});
		
		
		/*final String name = new String(players.getName());
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
		return gson.toJson(resultPaws);*/
	}
	// CLASS to recive movements
	class Movement {
		String player1;
		String player2;
		Pins last;
		Pins newPaw;
		
		class Pins{
			int x;
			int y;
		}
	}
	
	@MessageMapping("/getPlayers")
	@SendTo("/ws/getPlayers")
	public String sendPlayersList() {
		
		return gson.toJson(playerListRepo.findAll().stream()
				.map( p -> {return p.getName();})
				.collect(Collectors.toList()));
	}
}
