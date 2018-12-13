package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchController {
	@Autowired
	private MatchRepository matchRepository;

	@GetMapping("/matches")
	public Iterable<Match> getMatches(Pageable pageable) {
		return matchRepository.findAll(pageable);
	}
}
