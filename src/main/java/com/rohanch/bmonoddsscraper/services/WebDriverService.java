package com.rohanch.bmonoddsscraper.services;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class WebDriverService {
	private HashMap<String, WebDriver> sportWebDrivers = new HashMap<>();

	public HashMap<String, WebDriver> getSportWebDrivers() {
		return sportWebDrivers;
	}

	public void setSportWebDrivers(HashMap<String, WebDriver> sportWebDrivers) {
		this.sportWebDrivers = sportWebDrivers;
	}
}
