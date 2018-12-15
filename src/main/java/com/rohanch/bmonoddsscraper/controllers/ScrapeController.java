package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.request.ChromeControlRequest;
import com.rohanch.bmonoddsscraper.pages.InPlayPage;
import com.rohanch.bmonoddsscraper.services.LiveMatchesService;
import com.rohanch.bmonoddsscraper.services.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class ScrapeController {
	@Autowired
	private WebDriverService webDriverService;

	@Autowired
	private LiveMatchesService liveMatchesService;

	private HashMap<String, Timer> scrapeTimers = new HashMap<>();

	@PostMapping("/scrape")
	public ResponseEntity StartScraper(@RequestBody ChromeControlRequest body) {
		if (!webDriverService.getSportWebDrivers().containsKey(body.getSportName())) {
			System.out.printf("ERROR: No active chrome session for \"%s\", please start one first.\n", body.getSportName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		if (scrapeTimers.containsKey(body.getMarketName())) {
			System.out.printf("ERROR: Timer already running for \"%s\"!\n", body.getMarketName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		System.out.printf("Starting timer for \"%s\"!\n", body.getMarketName());

		TimerTask task = new TimerTask() {
			public void run() {
				System.out.printf("Scraping %s on market \"%s\"\n", body.getSportName(), body.getMarketName());
				try {
					var matches = InPlayPage.ScrapeLiveGamesData(webDriverService.getSportWebDrivers().get(body.getSportName()), body.getSportName(), body.getMarketName());
					if (matches.length > 0) {
						liveMatchesService.UpdateMatches(matches);
					} else {
						System.out.println("No matches persisted");
					}
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
			}
		};
		Timer timer = new Timer("Timer");

		long period = 5000L;
		timer.scheduleAtFixedRate(task, 0L, period);
		scrapeTimers.put(body.getMarketName(), timer);

		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping("/scrape")
	public ResponseEntity StopScraper(@RequestBody ChromeControlRequest body) {
		if (!scrapeTimers.containsKey(body.getMarketName())) {
			System.out.printf("ERROR: Timer does not exist for \"%s\"!\n", body.getMarketName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		System.out.printf("Stopping timer for \"%s\"!\n", body.getMarketName());

		scrapeTimers.get(body.getMarketName()).cancel();
		scrapeTimers.remove(body.getMarketName());

		return ResponseEntity.ok().body(null);
	}
}
