package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.request.ChromeControlRequest;
import com.rohanch.bmonoddsscraper.services.ScrapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapeController {
	@Autowired
	private ScrapeService scrapeService;

	@PostMapping("/scrape")
	public void StartScraper(@RequestBody ChromeControlRequest body) {
		scrapeService.StartScraper(body.getSportName(), body.getMarketName());
	}

	@DeleteMapping("/scrape")
	public void StopScraper(@RequestBody ChromeControlRequest body) {
		scrapeService.StopScraper(body.getMarketName());
	}
}
