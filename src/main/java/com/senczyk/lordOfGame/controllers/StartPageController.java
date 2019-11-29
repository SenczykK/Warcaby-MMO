package com.senczyk.lordOfGame.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.*;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.senczyk.lordOfGame.entities.GameEntity;
import com.senczyk.lordOfGame.entities.GamesRepository;
import com.senczyk.lordOfGame.entities.PlayerEntity;
import com.senczyk.lordOfGame.entities.PlayerListRepository;


@CrossOrigin
@RestController
public class StartPageController {
	
	@Autowired
	private PlayerListRepository playerListRepo;
	@Autowired
	private GamesRepository gamesRepo;
	
	private List<PlayerEntity> playerList;
	private List<GameEntity> startedGames;
	
	Gson gson = new Gson();
	
	{
		playerList = new ArrayList<>();
		startedGames = new ArrayList<>();
	}
	
	@PostMapping("/loginPl")
	public String loginPlayer(@RequestBody String data, HttpServletRequest req, HttpServletResponse res) {

		//String playerName = gson.fromJson(data, String.class);
		PlayerEntity newPlayer = gson.fromJson(data, PlayerEntity.class);
		
		Predicate<PlayerEntity>  hasName = p -> p.getName().equals(newPlayer.getName());
		
		if( !playerListRepo.findAll().stream().anyMatch(hasName) ) {
			playerListRepo.save(newPlayer);
			System.out.println("New player name: "+newPlayer.getName());
			try {
				Cookie c = new Cookie("playerName", newPlayer.getName());
				c.setHttpOnly(false);
				res.addCookie(c);
				res.flushBuffer();
				return gson.toJson("Logged in...");
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return gson.toJson("Error with creating a cookie");
			}
		} else {
			System.out.println("Player name used. Try another one.");
			return gson.toJson("Try different player name");
		}
		//playerList.add(player);

	}	

	
	@GetMapping("/getPlayerNameList")
	public String getPlayerNameList(HttpServletResponse player1) {
		List<String> namesList = new ArrayList<>();
		for( PlayerEntity p : playerList) {
			namesList.add(p.getName());
		}
		Type listType = new TypeToken<ArrayList<String>>(){}.getType();
		return gson.toJson(namesList, listType);		
	}
	
	
	@GetMapping("/startGame")
	public String newGame() {
		
		PlayerEntity pl1 = new PlayerEntity();
		pl1.setName("Kamil");
		PlayerEntity pl2 = new PlayerEntity();
		pl2.setName("Tomek");
		playerList.add(pl1);
		playerList.add(pl2);
		boolean started;
		// wyszukiwanie indexu gracza
		int index1 = 0, index2 = 0;
		for(int i=0; i<playerList.size(); i++ ) {
			if( playerList.get(i).getName().contentEquals(pl1.getName()) ) {
				index1 = i;
			}
			if( playerList.get(i).getName().contentEquals(pl2.getName()) ) {
				index2 = i;
			}
		}
		// make a new game
		GameEntity game = new GameEntity(playerList.get(index1), playerList.get(index2));
		startedGames.add(game);
		return "Succes";
	}
	

}
