package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.services.pages.InPlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ScrapeService {
	@Autowired
	private WebDriverService webDriverService;

	@Autowired
	private LiveMatchesService liveMatchesService;

	@Autowired
	private InPlay inPlay;

	private HashMap<String, Timer> scrapeTimers = new HashMap<>();

	public void StartScraper(String sportName, String marketName) {
		if (scrapeTimers.containsKey(marketName)) {
			throw new ScrapeTimerException(String.format("Timer for \"%s\" is already running.\n", marketName));
		}

		System.out.printf("Starting timer for \"%s\"!\n", marketName);

		TimerTask task = new TimerTask() {
			public void run() {
				DoScrape(sportName, marketName);
			}
		};
		Timer timer = new Timer("Timer");

		long period = 5000L;
		timer.scheduleAtFixedRate(task, 0L, period);
		scrapeTimers.put(marketName, timer);
	}

	public void StopScraper(String marketName) {
		if (!scrapeTimers.containsKey(marketName)) {
			throw new ScrapeTimerException(String.format("Timer for \"%s\" is not running.\n", marketName));
		}

		System.out.printf("Stopping timer for \"%s\"!\n", marketName);

		scrapeTimers.get(marketName).cancel();
		scrapeTimers.remove(marketName);
	}

	private void DoScrape(String sportName, String marketName) {
		System.out.printf("Scraping %s on market \"%s\"\n", sportName, marketName);

		try {
			var webDriver = webDriverService.GetActiveWebDriver(sportName);

			var matches = inPlay.ScrapeLiveGamesData(webDriver, sportName, marketName);
			if (matches.length > 0) {
				liveMatchesService.UpdateMatches(matches);
			} else {
				System.out.println("No matches persisted");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);

			StopScraper(marketName);
		}
	}

	//TODO: combine these and use a message to split difirentiate them?
	public class ScrapeTimerException extends RuntimeException {
		ScrapeTimerException(String s) {
			super(s);
		}
	}
}
