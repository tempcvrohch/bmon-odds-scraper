package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.generic.UserWrapper;
import com.rohanch.bmonoddsscraper.models.response.Session;
import com.rohanch.bmonoddsscraper.repositories.BetRepository;
import com.rohanch.bmonoddsscraper.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = {"*"}, allowCredentials = "true")
//TODO: avoid duplication across controllers
public class UserController {
	@Autowired
	private BetService betService;

	@Autowired
	private BetRepository betRepository;

	@GetMapping("/user/bets/pending")
	public Bet[] GetUserBetsPending(@AuthenticationPrincipal UserWrapper user) {
		return betRepository.GetBetsPendingOnUserId(user.getUser().getId());
	}

	@GetMapping("/user/session")
	public Session GetUserSession(@AuthenticationPrincipal UserWrapper user) {
		var pendingBetsAmount = betRepository.GetAmountBetsPendingOnUserId(user.getUser().getId(), Bet.BetStatus.PENDING.name());
		return new Session(user.getUsername(), user.getUser().getBalance(), pendingBetsAmount);
	}
}
