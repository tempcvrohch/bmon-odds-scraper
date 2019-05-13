package com.rohanch.bmonoddsscraper.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohanch.bmonoddsscraper.JsonTestLoader;
import com.rohanch.bmonoddsscraper.models.db.MarketState;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.models.db.MatchState;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

//TODO this class needs many more tests considering different scrape states
@RunWith(SpringRunner.class)
@SpringBootTest
public class LiveMatchesServiceTests {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JsonTestLoader jsonTestLoader;

	@MockBean
	private MatchRepository matchRepository;

	@MockBean
	private MatchStateRepository matchStateRepository;

	@MockBean
	private BetResultService betResultService;

	@MockBean
	private MarketStateRepository marketStateRepository;

	@Autowired
	@InjectMocks
	private LiveMatchesService liveMatchesService;

	private Match match;
	private List<Match> matches;

	@Before
	public void loadTemplates() throws IOException {
		match = jsonTestLoader.loadMatchFromPath("classpath:templates\\match.json");
		match = setParentReferences(match);
		matches = new ArrayList<>();
		matches.add(match);
	}

	private Match setParentReferences(Match match) {
		match.getMatchState().setMatch(match);
		match.getMatchState().getMarketStates().get(0).setMatchState(match.getMatchState());
		match.getMatchState().getMarketStates().get(1).setMatchState(match.getMatchState());

		return match;
	}

	//TODO this test probably needs to be split up
	@Test
	public void persistsNewMatchToDBAndCache() throws IOException {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -2);
		Mockito.when(matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis()))).thenReturn(new ArrayList<>());

		//clone
		var persistedMatch = objectMapper.readValue(objectMapper.writeValueAsString(match), Match.class);
		persistedMatch.setId(0L);
		Mockito.when(matchRepository.save(ArgumentMatchers.eq(match))).thenReturn(persistedMatch);

		//clone
		var persistedMatchState = objectMapper.readValue(objectMapper.writeValueAsString(persistedMatch.getMatchState()), MatchState.class);
		persistedMatchState.setId(0L);
		Mockito.when(matchStateRepository.save(ArgumentMatchers.eq(persistedMatch.getMatchState()))).thenReturn(persistedMatchState);

		persistedMatch.setMatchState(persistedMatchState);
		Mockito.when(matchRepository.save(ArgumentMatchers.eq(persistedMatch))).thenReturn(persistedMatch);

		//clone
		var persistedMarketStates = objectMapper.readValue(objectMapper.writeValueAsString(persistedMatch.getMatchState().getMarketStates()), MarketState[].class);
		Mockito.when(marketStateRepository.saveAll(persistedMatch.getMatchState().getMarketStates())).thenReturn(Arrays.asList(persistedMarketStates));

		liveMatchesService.updateMatches(matches);
		Mockito.verify(matchRepository, Mockito.times(2)).save(match);
		//TODO more verifies
	}
}
