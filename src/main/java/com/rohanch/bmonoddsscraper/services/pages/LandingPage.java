package com.rohanch.bmonoddsscraper.services.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LandingPage {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void chooseLanguageAndNavigate(WebDriver webDriver, String lang, String targetPageURL) {
		WebElement langList = webDriver.findElement(By.className("lpnm"));
		var languages = langList.findElements(By.tagName("a"));
		var optLanguage = languages.stream().filter(e -> e.getText().equals(lang)).findFirst();
		var langElement = optLanguage.orElseThrow(LanguageNotAvailableException::new);

		logger.debug("Selecting language: \"{}\"", langElement.getText());
		langElement.click();

		webDriver.findElement(By.className("lpgb")); //await page refresh
		webDriver.get(targetPageURL);
	}

	public class LanguageNotAvailableException extends RuntimeException {

	}
}
