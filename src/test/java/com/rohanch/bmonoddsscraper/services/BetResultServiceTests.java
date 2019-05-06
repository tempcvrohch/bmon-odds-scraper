package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.JsonTestLoader;
import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.BetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BetResultServiceTests {
	@Autowired
	private JsonTestLoader jsonTestLoader;

	@Autowired
	@InjectMocks
	private BetResultService betResultService;

	@MockBean
	private BetRepository betRepository;

	@MockBean
	private BetService betService;

	private Match match;
	private Bet bet;
	private List<Bet> betsToReturn = new ArrayList<>();

	//TODO change tests to include all winning/losing/void scores
	@Before
	public void loadTemplates() throws IOException {
		match = jsonTestLoader.loadMatchFromPath("classpath:templates\\match.json");
		bet = jsonTestLoader.loadBetFromPath("classpath:templates\\bet.json");
		betsToReturn.add(bet);

		Mockito.when(betRepository.FindBetsByMatchId(match.getId())).thenReturn(betsToReturn);
	}

	@Test
	public void marksBetWonWhenMatchEndedWithWinningBet() {
		betResultService.ProcessUserBetsOnMatch(match);
		Mockito.verify(betService).ProcessFinishedBet(bet, true);
	}

	@Test
	public void marksBetLostWhenMatchEndedWithLosingBet() {
		match.getMatchState().setSetScore("4-6,4-6");

		betResultService.ProcessUserBetsOnMatch(match);
		Mockito.verify(betService).ProcessFinishedBet(bet, false);
	}

	@Test
	public void marksBetLostWithInvalidScore() {
		match.getMatchState().setSetScore("4-6,4-4");

		betResultService.ProcessUserBetsOnMatch(match);
		Mockito.verify(betService).ProcessVoidBet(bet);
	}
}
