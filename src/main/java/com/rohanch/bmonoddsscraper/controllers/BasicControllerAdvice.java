package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.services.ScrapeService;
import com.rohanch.bmonoddsscraper.services.WebDriverService;
import com.rohanch.bmonoddsscraper.services.helpers.Inject;
import com.rohanch.bmonoddsscraper.services.pages.InPlay;
import com.rohanch.bmonoddsscraper.services.pages.Landing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BasicControllerAdvice {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ScrapeService.ScrapeTimerException.class, WebDriverService.WebDriverException.class})
	public void handleBadRequest(Exception exception) {
		logger.info(exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED) //?
	@ExceptionHandler({Landing.LanguageNotAvailableException.class})
	public void handleLanguageError(Exception exception) {
		logger.info(exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({InPlay.InPlayException.class, Inject.JSFileNotFoundException.class})
	public void handleServerError(Exception exception) {
		logger.error(exception.getLocalizedMessage());
	}
}
