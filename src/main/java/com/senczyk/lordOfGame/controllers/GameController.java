package com.senczyk.lordOfGame.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
	private PlayerListRepository PlayerListRepo;
	
	private List<PlayerEntity> playerList; // odczyt z bazy danych
	private List<GameEntity> startedGames;
	
	Gson gson = new Gson();
	
	@PostConstruct
	protected void updateDataFromDatabase() {
		playerList = PlayerListRepo.findAll();
		// add a new game(fiction game)
		PlayerEntity p1 = new PlayerEntity("Tomek");
		PlayerEntity p2 = new PlayerEntity("Kamil");
		GameEntity tempGame = new GameEntity(p1, p2);
		startedGames = new ArrayList<>();
		startedGames.add(tempGame);
		// TO DO
		//startedGames = GamesRepo.findAll();
	}
	
	@MessageMapping("/get") //receive message STOMP endpoint
	@SendTo("/get/messages")  // messageBroker /messages (all subscribes get the message)
	public String send(String message) throws Exception{
		System.out.println(message);
		return gson.toJson(startedGames.get(0));
	}
	
	
	/////////////////////////////////////////////////////////////////////
	@PostMapping("/setBoard")
	public void searchGameAndUpdate(@RequestBody String gameJson, HttpServletResponse responseIdGame) {
		
		// TO DO: sprawdzic kolejnosc graczy lub po stronie clienta
		
		System.out.println(gameJson);
		startedGames.set(
				startedGames.indexOf(gson.fromJson(gameJson, GameEntity.class)), 
				gson.fromJson(gameJson, GameEntity.class)
				);
		
		PrintWriter pw;
		try {
			pw = responseIdGame.getWriter();
			if(startedGames.get(0) != null)
			pw.write(gson.toJson(startedGames.get(0), GameEntity.class));
			if(startedGames.get(0) == null)
			pw.write("no game");
			System.out.println("Send a game");
		} catch (Exception e) {
			System.out.println("Exception in PrinWriter /askPlayer");
		}
	}
	
	@PostMapping("/getBoard")
	public void getBoard(@RequestBody String reqPlayerName, HttpServletResponse res) {
		// zwracam tylko pionki przeciwnika
		PlayerEntity playerTemp = gson.fromJson(reqPlayerName, PlayerEntity.class);
		startedGames.indexOf(new GameEntity(playerTemp, playerTemp));
		//startedGames.stream().filter( game -> game.getPlayer1().getName().contentEquals(playerTemp.getName()) ).;
		//Game game = startedGames.get(  );
		
		PrintWriter pw;
		try {
			pw = res.getWriter();
			if(startedGames.get(0) != null)
			pw.write(gson.toJson(startedGames.get(0), GameEntity.class));
			if(startedGames.get(0) == null)
			pw.write("no game");
			System.out.println("Send a game");
		} catch (Exception e) {
			System.out.println("Exception in PrinWriter /askPlayer");
		}
	}
}
