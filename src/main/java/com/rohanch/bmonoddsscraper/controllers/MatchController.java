package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.services.LiveMatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/matches/recent")
	public Iterable<Match> GetRecentMatches(Pageable pageable) {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);
		var matches = matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis()));
		matches.forEach(match -> match.setLive(liveMatchesService.getLiveMatchEntities().contains(match)));

		return matches;
	}
}
