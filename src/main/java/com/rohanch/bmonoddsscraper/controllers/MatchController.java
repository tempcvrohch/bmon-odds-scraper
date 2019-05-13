package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.MarketState;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = {"*"}, allowCredentials = "true")
public class MatchController {
	@Autowired
	private MatchService matchService;

	@GetMapping("/matches")
	public Iterable<Match> getMatches(@RequestParam(required = false) String from,
									  @RequestParam(required = false) String to) {
		if (from != null && to != null) {
			return matchService.getMatches();
		} else {
			return matchService.getMatchesBetweenDates(from, to);
		}
	}

	@GetMapping("/match/{id}")
	public Match getMatchOnId(@PathVariable("id") Long id) {
		return matchService.getMatchById(id);
	}

	@GetMapping("/match/{id}/market/latest")
	public MarketState[] getLatestMarketOnMatchId(@PathVariable("id") Long id) {
		return matchService.getLatestMarketByMatchId(id);
	}

	@GetMapping("/matches/recent")
	public Iterable<Match> getRecentMatches() {
		return matchService.getRecentMatches();
	}
}
