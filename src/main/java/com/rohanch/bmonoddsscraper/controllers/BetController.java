package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.generic.UserWrapper;
import com.rohanch.bmonoddsscraper.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = {"*"}, allowCredentials = "true")
//TODO: avoid duplication across controllers
public class BetController {
	@Autowired
	private BetService betService;

	@PostMapping("/bet/place")
	public void placeBet(@AuthenticationPrincipal UserWrapper user, @RequestBody Bet bet) {
		betService.addBet(user.getUser(), bet);
	}
}
