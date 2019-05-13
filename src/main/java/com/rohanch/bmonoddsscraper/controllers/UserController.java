package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.generic.UserWrapper;
import com.rohanch.bmonoddsscraper.models.response.Session;
import com.rohanch.bmonoddsscraper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = {"*"}, allowCredentials = "true")
//TODO: avoid duplication across controllers
public class UserController {
	@Autowired
	private UserService userService;


	@GetMapping("/user/bets/pending")
	public List<Bet> getUserBetsPending(@AuthenticationPrincipal UserWrapper userWrapper) {
		return userService.getUserPendingBets(userWrapper);
	}

	@GetMapping("/user/session")
	public Session getUserSession(@AuthenticationPrincipal UserWrapper userWrapper) {
		return userService.getUserSession(userWrapper);
	}
}
