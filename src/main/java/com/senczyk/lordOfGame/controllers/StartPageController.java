package com.senczyk.lordOfGame.controllers;

import java.time.LocalDate;
import java.util.function.Predicate;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
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
			newPlayer.setLastLogin(LocalDate.now());
			playerListRepo.save(newPlayer);
			System.out.println("New player name: "+newPlayer.getName());
			return gson.toJson("Logged in...");
		} else {
			System.out.println(newPlayer.getName()+" Player name used. Try another one.");
			return gson.toJson("Try different player name");
		}
	}	
	
}
