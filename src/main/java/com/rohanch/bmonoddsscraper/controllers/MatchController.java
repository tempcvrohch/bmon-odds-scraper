package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RestController
public class MatchController {
	@Autowired
	private MatchRepository matchRepository;

	@GetMapping("/matches")
	public Iterable<Match> GetMatches(Pageable pageable) {
		return matchRepository.findAll();
	}

	@GetMapping("/matches/recent")
	public Iterable<Match> GetRecentMatches(Pageable pageable) {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);
		return matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis()));
	}

	@GetMapping("/matches/all")
	public Iterable<Match> FindAllJoined(Pageable pageable) {
		return matchRepository.findAll();
	}
}
