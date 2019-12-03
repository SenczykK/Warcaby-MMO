package com.senczyk.lordOfGame.controllers;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String setLoginPage() {
		return "login.html";
	}
}
