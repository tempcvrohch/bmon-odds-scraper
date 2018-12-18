package com.rohanch.bmonoddsscraper.services.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.services.helpers.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InPlay {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Inject inject;

	public void OpenSportOnName(WebDriver webDriver, String sportName) {
		var sportSelectElement = webDriver.findElement(By.xpath(String.format("//%s[text()=\"%s\"]", "div", sportName)));

		logger.debug("Clicking sport: \"{}\" in InPlay page", sportName);
		sportSelectElement.click();
	}

	public Match[] ScrapeLiveGamesData(WebDriver webDriver, String sportName, String marketName) {
		if (!hasAvailableMatchesForCurrentSportName(webDriver)) {
			throw new InPlayException(String.format("No Live games available for \"%s\"\n", sportName));
		}

		var expression = inject.ConstructJSExpressionWithFile(String.format("return ReadOddDataOnBasicMarket(\"%s\",\"%s\");", sportName, marketName), "assets\\inject\\inplay-matches-helper.js");

		var res = ((JavascriptExecutor) webDriver).executeScript(expression);
		if (res == null) {
			throw new InPlayException(String.format("No markets available for games in \"%s\"/\"%s\"\n", sportName, marketName));
		}

		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(res, Match[].class);
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
