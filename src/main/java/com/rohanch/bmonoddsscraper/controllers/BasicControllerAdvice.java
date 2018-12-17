package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.services.ScrapeService;
import com.rohanch.bmonoddsscraper.services.WebDriverService;
import com.rohanch.bmonoddsscraper.services.helpers.Inject;
import com.rohanch.bmonoddsscraper.services.pages.InPlay;
import com.rohanch.bmonoddsscraper.services.pages.Landing;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BasicControllerAdvice {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ScrapeService.ScrapeTimerException.class, WebDriverService.WebDriverException.class})
	public void handleBadRequest() {
		//do logging here
	}

	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED) //?
	@ExceptionHandler({Landing.LanguageNotAvailableException.class})
	public void handleLanguageError() {
		//do logging here
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ScrapeService.ScrapeTimerException.class, WebDriverService.WebDriverException.class,
			InPlay.InPlayException.class, Inject.JSFileNotFoundException.class})
	public void handleServerError() {
		//do logging here
	}

	private void log() {
		//TODO: fix logging
	}
}
