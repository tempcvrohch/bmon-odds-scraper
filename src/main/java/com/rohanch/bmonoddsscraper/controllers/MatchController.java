package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import com.rohanch.bmonoddsscraper.services.LiveMatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

@RestController
public class MatchController {
	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchStateRepository matchStateRepository;

	@Autowired
	private LiveMatchesService liveMatchesService;

	@GetMapping("/matches")
	public Iterable<Match> GetMatches(@RequestParam Map<String, String> queryParameters) {
		if (queryParameters.containsKey("from") && queryParameters.containsKey("to")) {
			var dateFrom = Date.from(Instant.parse(queryParameters.get("from")));
			var dateTo = Date.from(Instant.parse(queryParameters.get("to")));

			return matchRepository.findBetweenTimestamps(new Timestamp(dateFrom.getTime()), new Timestamp(dateTo.getTime()));
		} else {
			return matchRepository.findAll();
		}
	}

	@GetMapping("/match/{id}")
	public Match GetMatchOnId(@PathVariable("id") Long id) {
		var optionalMatch = matchRepository.findById(id);
		var match = optionalMatch.orElseThrow(MatchNotFound::new);
		match.setMatchStates(matchStateRepository.findAllByMatchId(id)); //TODO: jpa should probably handle the child entities

		return match;
	}

	@GetMapping("/matches/recent")
	public Iterable<Match> GetRecentMatches() {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);
		var matches = matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis()));
		matches.forEach(match -> match.setLive(liveMatchesService.getLiveMatchEntities().contains(match)));

		return matches;
	}

	class MatchNotFound extends RuntimeException {
	}
}
