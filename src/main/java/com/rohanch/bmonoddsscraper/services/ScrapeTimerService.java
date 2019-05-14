package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.services.pages.InPlayPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ScrapeTimerService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WebDriverService webDriverService;

	@Autowired
	private LiveMatchesService liveMatchesService;

	@Autowired
	private InPlayPage inPlay;

	private HashMap<String, Timer> scrapeTimers = new HashMap<>();

	public void startScraper(String sportName, String marketName) {
		if (scrapeTimers.containsKey(marketName)) {
			throw new ScrapeTimerException(String.format("Timer for \"%s\" is already running.\n", marketName));
		}

		logger.debug("Starting timer for \"{}\"!", marketName);

		TimerTask task = new TimerTask() {
			public void run() {
				doScrape(sportName, marketName);
			}
		};
		Timer timer = new Timer(String.format("ScrapeTimer-%s", sportName));

		long period = 5000L;
		timer.scheduleAtFixedRate(task, 0L, period);
		scrapeTimers.put(marketName, timer);
	}

	public void stopScraper(String marketName) {
		if (!scrapeTimers.containsKey(marketName)) {
			throw new ScrapeTimerException(String.format("Timer for \"%s\" is not running.\n", marketName));
		}

		logger.debug("Stopping timer for \"{}\"!", marketName);

		scrapeTimers.get(marketName).cancel();
		scrapeTimers.remove(marketName);
	}

	public void doScrape(String sportName, String marketName) {
		logger.debug("Scraping \"{}\" on market \"{}\"", sportName, marketName);

		try {
			var webDriver = webDriverService.getActiveWebDriver(sportName);

			if (!inPlay.hasLiveSportSelected(webDriver, sportName) && inPlay.isLiveSportAvailable(webDriver, sportName)) {
				inPlay.openSportOnName(webDriver, sportName);
			}

			var matches = inPlay.scrapeLiveGamesData(webDriver, sportName, marketName);
			if (matches.length > 0) {
				liveMatchesService.updateMatches(Arrays.asList(matches));
			}
		} catch (org.openqa.selenium.WebDriverException e) {
			//TODO: rare event when the scrape JS is executed during a forced page reload
		} catch (Exception e) {
			logger.error("Stopping scrape timer \"{}\" on market \"{}\"", sportName, marketName, e);
			stopScraper(marketName);
		}
	}

	public class ScrapeTimerException extends RuntimeException {
		ScrapeTimerException(String s) {
			super(s);
		}
	}
}
