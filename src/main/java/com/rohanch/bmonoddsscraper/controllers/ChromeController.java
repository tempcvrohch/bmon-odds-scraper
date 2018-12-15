package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.request.ChromeControlRequest;
import com.rohanch.bmonoddsscraper.pages.InPlayPage;
import com.rohanch.bmonoddsscraper.pages.LandingPage;
import com.rohanch.bmonoddsscraper.services.LiveMatchesService;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ChromeController {
	private WebDriver webDriver;

	@Autowired
	private LiveMatchesService liveMatchesService;

	@PostMapping("/chrome/start")
	public void StartChrome(@RequestBody ChromeControlRequest body) {
		System.out.printf("Starting Chrome for \"%s\"\n", body.getSportName());

		System.setProperty("webdriver.chrome.driver", "bin\\chromedriver.exe");
		WebDriver webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webDriver.manage().window().setSize(new Dimension(1920, 900));

		webDriver.get("https://bet365.com");

		try {
			LandingPage.chooseLanguageAndNavigate(webDriver, "English", "https://bet365.com/#/IP/");
			InPlayPage.OpenSportOnName(webDriver, body.getSportName());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		setWebDriver(webDriver);
	}

	@PostMapping("/chrome/scrape")
	public void Scrape(@RequestBody ChromeControlRequest body) {
		if (webDriver == null) {
			return;
		}

		System.out.printf("Scraping %s on market \"%s\"\n", body.getSportName(), body.getMarketName());
		try {
			var matches = InPlayPage.ScrapeLiveGamesData(webDriver, body.getSportName(), body.getMarketName());
			if (matches.length > 0) {
				liveMatchesService.UpdateMatches(matches);
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
