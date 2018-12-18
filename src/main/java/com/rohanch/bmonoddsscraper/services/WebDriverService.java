package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.services.pages.InPlay;
import com.rohanch.bmonoddsscraper.services.pages.Landing;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class WebDriverService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private HashMap<String, WebDriver> sportWebDrivers = new HashMap<>();

	@Autowired
	private InPlay inPlay;

	@Autowired
	private Landing landing;

	public void setSportWebDrivers(HashMap<String, WebDriver> sportWebDrivers) {
		this.sportWebDrivers = sportWebDrivers;
	}

	public void StartOnSportName(String sportName) {
		if (sportWebDrivers.containsKey(sportName)) {
			throw new WebDriverException(String.format("Webdriver for \"%s\" is already running.\n", sportName));
		}

		logger.debug("Starting Chrome for \"{}\"", sportName);
		System.setProperty("webdriver.chrome.driver", "bin\\chromedriver.exe");
		WebDriver webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webDriver.manage().window().setSize(new Dimension(1920, 900));

		webDriver.get("https://bet365.com");
		logger.debug("Navigated to bet365");

		landing.ChooseLanguageAndNavigate(webDriver, "English", "https://bet365.com/#/IP/");
		logger.debug("Clicked language");
		inPlay.OpenSportOnName(webDriver, sportName);
		logger.debug("Webdriver ready");

		sportWebDrivers.put(sportName, webDriver);
	}

	public void StopOnSportname(String sportName) {
		logger.debug("Stopping Chrome for \"{}\"!", sportName);
		GetActiveWebDriver(sportName).close();
		sportWebDrivers.remove(sportName);
	}

	WebDriver GetActiveWebDriver(String sportName) {
		if (!sportWebDrivers.containsKey(sportName)) {
			throw new WebDriverException(String.format("Webdriver for \"%s\" is not running.\n", sportName));
		}

		return sportWebDrivers.get(sportName);
	}

	public HashMap<String, WebDriver> getSportWebDrivers() {
		return sportWebDrivers;
	}

	public class WebDriverException extends RuntimeException {
		WebDriverException(String s) {
			super(s);
		}
	}
}
