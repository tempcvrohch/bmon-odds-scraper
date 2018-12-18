package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.request.ChromeControlRequest;
import com.rohanch.bmonoddsscraper.services.ScrapeTimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapeController {
	@Autowired
	private ScrapeTimerService scrapeTimerService;

	@PostMapping("/scrape")
	public void StartScraper(@RequestBody ChromeControlRequest body) {
		scrapeTimerService.StartScraper(body.getSportName(), body.getMarketName());
	}

	@DeleteMapping("/scrape")
	public void StopScraper(@RequestBody ChromeControlRequest body) {
		scrapeTimerService.StopScraper(body.getMarketName());
	}
}
