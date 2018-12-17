package com.rohanch.bmonoddsscraper.services.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

@Service
public class Landing {
	public void ChooseLanguageAndNavigate(WebDriver webDriver, String lang, String targetPageURL) {
		WebElement langList = webDriver.findElement(By.className("lpnm"));
		var languages = langList.findElements(By.tagName("a"));
		var optLanguage = languages.stream().filter(e -> e.getText().equals(lang)).findFirst();
		var langElement = optLanguage.orElseThrow(LanguageNotAvailableException::new);

		System.out.printf("Selecting language: \"%s\"\n", langElement.getText());
		langElement.click();

		webDriver.findElement(By.className("lpgb")); //await page refresh
		webDriver.get(targetPageURL);
	}

	public class LanguageNotAvailableException extends RuntimeException {

	}
}
