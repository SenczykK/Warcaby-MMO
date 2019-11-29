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
	
	Gson gson = new Gson();
	
	@PostMapping("/loginPl")
	public String loginPlayer(@RequestBody String data, HttpServletRequest req, HttpServletResponse res) {
		PlayerEntity newPlayer = gson.fromJson(data, PlayerEntity.class);
		
		Predicate<PlayerEntity>  hasName = p -> p.getName().equals(newPlayer.getName());
		if( !playerListRepo.findAll().stream().anyMatch(hasName) ) {
			playerListRepo.save(newPlayer);
			System.out.println("New player name: "+newPlayer.getName());
			return gson.toJson("Logged in...");
		} else {
			System.out.println("Player name used. Try another one.");
			return gson.toJson("Try different player name");
		}
	}	
	
	@GetMapping("/getPlayerNameList")
	public String getPlayerNameList(HttpServletResponse player1) {
		List<String> namesList = new ArrayList<>();
		for( PlayerEntity p : playerListRepo.findAll()) {
			namesList.add(p.getName());
		}
		Type listType = new TypeToken<ArrayList<String>>(){}.getType();
		return gson.toJson(namesList, listType);		
	}
}
