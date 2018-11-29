package com.rohanch.bmonoddsscraper.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohanch.bmonoddsscraper.models.Match;
import com.rohanch.bmonoddsscraper.utils.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class InPlay {
	public static void OpenSportOnName(WebDriver webDriver, String sportName) throws Exception {
		var sportSelectElement = webDriver.findElement(By.xpath(String.format("//%s[text()=\"%s\"]", "div", sportName)));

		System.out.printf("Clicking sport: %s in InPlay page...\n", sportName);
		sportSelectElement.click();
	}

	public static void ScrapeLiveGamesData(WebDriver webDriver, String sportName, String marketName) throws Exception {
		var expression = Inject.constructJSExpressionWithFile(String.format("return ReadOddDataOnBasicMarket(\"%s\",\"%s\");", sportName, marketName), "assets\\inject\\inplay-matches-helper.js");

		var res = ((JavascriptExecutor) webDriver).executeScript(expression);
		if (res == null) {
			throw new Exception(String.format("could not fetch current live games data for: %s\n", sportName));
		}

		ObjectMapper mapper = new ObjectMapper();
		Match[] matches = mapper.convertValue(res, Match[].class);
	}
}
