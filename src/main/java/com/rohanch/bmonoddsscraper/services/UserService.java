package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.generic.UserWrapper;
import com.rohanch.bmonoddsscraper.models.response.Session;
import com.rohanch.bmonoddsscraper.repositories.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	@Autowired
	private BetRepository betRepository;

	public List<Bet> getUserPendingBets(UserWrapper userWrapper) {
		return betRepository.getBetsPendingOnUserId(userWrapper.getUser().getId());
	}

	public Session getUserSession(UserWrapper userWrapper) {
		var pendingBetsAmount = betRepository.getAmountBetsPendingOnUserId(userWrapper.getUser().getId(), Bet.BetStatus.PENDING.name());
		return new Session(userWrapper.getUsername(), userWrapper.getUser().getBalance(), pendingBetsAmount);
	}
}
