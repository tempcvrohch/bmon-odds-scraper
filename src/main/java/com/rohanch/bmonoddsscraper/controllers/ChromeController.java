package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.request.ChromeControlRequest;
import com.rohanch.bmonoddsscraper.pages.InPlayPage;
import com.rohanch.bmonoddsscraper.pages.LandingPage;
import com.rohanch.bmonoddsscraper.services.LiveMatchesService;
import com.rohanch.bmonoddsscraper.services.WebDriverService;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ChromeController {
	@Autowired
	private WebDriverService webDriverService;

	@Autowired
	private LiveMatchesService liveMatchesService;

	@PostMapping("/chrome")
	public ResponseEntity StartChrome(@RequestBody ChromeControlRequest body) {
		if (webDriverService.getSportWebDrivers().containsKey(body.getSportName())) {
			System.out.printf("ERROR: Chrome for \"%s\" is already active!\n", body.getSportName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		System.out.printf("Starting Chrome for \"%s\"\n", body.getSportName());

		//TODO: should this stuff be in WebDriverService.java ?
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
			webDriver.close();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		webDriverService.getSportWebDrivers().put(body.getSportName(), webDriver);
		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping("/chrome")
	public ResponseEntity StopChrome(@RequestBody ChromeControlRequest body) {
		if (!webDriverService.getSportWebDrivers().containsKey(body.getSportName())) {
			System.out.printf("ERROR: Cannot stop non-existing Chrome session for \"%s\"!\n", body.getSportName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		System.out.printf("Stopping Chrome for \"%s\"!\n", body.getSportName());
		webDriverService.getSportWebDrivers().get(body.getSportName()).close();
		webDriverService.getSportWebDrivers().remove(body.getSportName());
		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/chrome/scrape")
	public ResponseEntity Scrape(@RequestBody ChromeControlRequest body) {
		if (!webDriverService.getSportWebDrivers().containsKey(body.getSportName())) {
			System.out.printf("ERROR: No active chrome session for \"%s\", please start one first.\n", body.getSportName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.ok().body(null);
	}
}
