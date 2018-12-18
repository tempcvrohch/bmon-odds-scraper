package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.services.pages.InPlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ScrapeService {
	private Logger logger = LoggerFactory.getLogger(getClass());

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

		logger.debug("Starting timer for \"{}\"!\n", marketName);

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

		logger.debug("Stopping timer for \"{}\"!\n", marketName);

		scrapeTimers.get(marketName).cancel();
		scrapeTimers.remove(marketName);
	}

	private void DoScrape(String sportName, String marketName) {
		logger.debug("Scraping \"{}\" on market \"{}\"\n", sportName, marketName);

		try {
			var webDriver = webDriverService.GetActiveWebDriver(sportName);

			var matches = inPlay.ScrapeLiveGamesData(webDriver, sportName, marketName);
			if (matches.length > 0) {
				liveMatchesService.UpdateMatches(matches);
			} else {
				logger.debug("No matches persisted");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());

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
