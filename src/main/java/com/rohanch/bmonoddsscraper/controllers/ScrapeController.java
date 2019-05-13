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
	public void doScrape(@RequestBody ChromeControlRequest body) {
		scrapeTimerService.doScrape(body.getSportName(), body.getMarketName());
	}

	@PostMapping("/scrape/timer")
	public void startScraper(@RequestBody ChromeControlRequest body) {
		scrapeTimerService.startScraper(body.getSportName(), body.getMarketName());
	}

	@DeleteMapping("/scrape/timer")
	public void stopScraper(@RequestBody ChromeControlRequest body) {
		scrapeTimerService.stopScraper(body.getMarketName());
	}
}
