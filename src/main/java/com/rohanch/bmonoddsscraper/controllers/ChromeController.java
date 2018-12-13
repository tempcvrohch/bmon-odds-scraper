package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.pages.InPlay;
import com.rohanch.bmonoddsscraper.pages.Landing;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RestController
public class ChromeController {
	private WebDriver webDriver;

	@Autowired
	private MatchRepository matchRepository;

	@PostMapping("/chrome")
	public void StartChrome() {
		var sportName = "Tennis";
		var marketName = "Next Game";

		System.out.println("startChrome");
		System.setProperty("webdriver.chrome.driver", "bin\\chromedriver.exe");
		WebDriver webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webDriver.manage().window().setSize(new Dimension(1920, 900));

		webDriver.get("https://bet365.com");

		try {
			Landing.chooseLanguageAndNavigate(webDriver, "English", "https://bet365.com/#/IP/");
			InPlay.OpenSportOnName(webDriver, sportName);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		setWebDriver(webDriver);
	}

	@PostMapping("/scrape")
	public void Scrape() {
		if (webDriver == null) {
			return;
		}

		var sportName = "Tennis";
		var marketName = "Next Game";

		try {
			var matches = InPlay.ScrapeLiveGamesData(webDriver, sportName, marketName);
			if (matches.length > 0) {
				matchRepository.saveAll(Arrays.asList(matches));
			} else {
				System.out.println("No matches persisted");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	private void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
}
