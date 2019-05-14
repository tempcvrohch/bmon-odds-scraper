package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.MarketState;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class MatchService {
	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchStateRepository matchStateRepository;

	@Autowired
	private MarketStateRepository marketStateRepository;

	@Autowired
	private LiveMatchesService liveMatchesService;

	public List<Match> getMatches() {
		return matchRepository.findAll();
	}

	public List<Match> getMatchesBetweenDates(String from, String to) {
		//TODO check valid timestamps, maybe in controller?
		return matchRepository.findBetweenTimestamps(Timestamp.from(Instant.parse(from)), Timestamp.from(Instant.parse(to)));
	}

	public Match getMatchById(Long id) {
		var optionalMatch = matchRepository.findById(id);
		var match = optionalMatch.orElseThrow(MatchNotFound::new);
		match.setMatchStates(matchStateRepository.findAllByMatchId(id)); //TODO: jpa should probably handle the child entities

		return match;
	}

	public MarketState[] getLatestMarketByMatchId(Long id) {
		return marketStateRepository.findLatestMarketStatesOnMatchId(id);
	}

	public List<Match> getRecentMatches() {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);
		var matches = matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis()));
		matches.forEach(match -> match.setLive(liveMatchesService.getLiveMatchEntities().containsKey(match.getBId())));

		return matches;
	}

	public class MatchNotFound extends RuntimeException {
	}
}
