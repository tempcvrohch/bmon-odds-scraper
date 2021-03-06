package com.rohanch.bmonoddsscraper.services.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.services.helpers.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InPlayPage {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Inject inject;

	@Autowired
	private ObjectMapper objectMapper;

	public boolean isLiveSportAvailable(WebDriver webDriver, String sportName) {
		try {
			return webDriver.findElements(By.xpath(String.format("//%s[text()=\"%s\"]", "div", sportName))).size() > 0;
		} catch (StaleElementReferenceException e) {
			return false;
		}
	}

	public boolean hasLiveSportSelected(WebDriver webDriver, String sportName) {
		var sportSelectElement = webDriver.findElement(By.className("ipo-ClassificationBarButtonBase_Selected"));
		return sportSelectElement.getText().contains(sportName);
	}

	public void openSportOnName(WebDriver webDriver, String sportName) {
		var sportSelectElement = webDriver.findElement(By.xpath(String.format("//%s[text()=\"%s\"]", "div", sportName)));

		logger.debug("Clicking sport: \"{}\" in InPlayPage page", sportName);
		sportSelectElement.click();
	}

	public Match[] scrapeLiveGamesData(WebDriver webDriver, String sportName, String marketName) {
		if (!hasAvailableMatchesForCurrentSportName(webDriver)) {
			logger.info("No live games available for \"{}\"", sportName);
			return new Match[]{};
			//throw new InPlayException(String.format("No Live games available for \"%s\"\n", sportName));
		}

		var expression = inject.constructJSExpressionWithFile(String.format("return ReadOddDataOnBasicMarket(\"%s\",\"%s\");", sportName, marketName), "assets\\inject\\inplay-matches-helper.js");

		var res = ((JavascriptExecutor) webDriver).executeScript(expression);
		if (res == null) {
			logger.info("No markets available for games in \"{}\"/\"{}\"", sportName, marketName);
			return new Match[]{};
			//throw new InPlayException(String.format("No markets available for games in \"%s\"/\"%s\"\n", sportName, marketName));
		}

		return objectMapper.convertValue(res, Match[].class);
	}

	private boolean hasAvailableMatchesForCurrentSportName(WebDriver webDriver) {
		try {
			webDriver.findElement(By.className("ipo-Fixture_TableRow"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public class InPlayException extends RuntimeException {
		InPlayException(String s) {
			super(s);
		}
	}
}
