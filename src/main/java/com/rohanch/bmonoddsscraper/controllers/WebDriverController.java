package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.request.ChromeControlRequest;
import com.rohanch.bmonoddsscraper.services.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebDriverController {
	@Autowired
	private WebDriverService webDriverService;

	@PostMapping("/webdriver")
	public void startWebDriver(@RequestBody ChromeControlRequest body) {
		webDriverService.startOnSportName(body.getSportName());
	}

	@DeleteMapping("/webdriver")
	public void stopWebDriver(@RequestBody ChromeControlRequest body) {
		webDriverService.stopOnSportname(body.getSportName());
	}
}
