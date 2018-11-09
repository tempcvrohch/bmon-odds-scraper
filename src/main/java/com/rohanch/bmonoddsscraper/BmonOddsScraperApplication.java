package com.rohanch.bmonoddsscraper;

import com.rohanch.bmonoddsscraper.pages.InPlay;
import com.rohanch.bmonoddsscraper.pages.Landing;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BmonOddsScraperApplication {
	public static void main(String[] args) {
		SpringApplication.run(BmonOddsScraperApplication.class, args);
		BmonOddsScraperApplication app = new BmonOddsScraperApplication();
		app.startChrome();
	}

	private void startChrome() {
		var currentSport = "Basketball";
		var currentMarket = "Money Line";

		System.out.println("startChrome");
		System.setProperty("webdriver.chrome.driver", "bin\\chromedriver.exe");
		WebDriver webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webDriver.manage().window().setSize(new Dimension(1600, 900));

		webDriver.get("https://bet365.com");

		try {
			Landing.chooseLanguageAndNavigate(webDriver, "English", "https://bet365.com/#/IP/");
			InPlay.OpenSportOnName(webDriver, "Basketball");
			InPlay.ScrapeLiveGamesData(webDriver, currentSport);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}


	}
}
