package com.rohanch.bmonoddsscraper.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LandingPage {
	public static void chooseLanguageAndNavigate(WebDriver webDriver, String lang, String targetPageURL) throws Exception {
		WebElement langList = webDriver.findElement(By.className("lpnm"));
		var languages = langList.findElements(By.tagName("a"));
		var optLanguage = languages.stream().filter(e -> e.getText().equals(lang)).findFirst();
		var langElement = optLanguage.orElseThrow(() -> new Exception("Language not found in list"));

		System.out.printf("Selecting language: \"%s\"\n", langElement.getText());
		langElement.click();

		webDriver.findElement(By.className("lpgb")); //await page refresh
		webDriver.get(targetPageURL);
	}
}
