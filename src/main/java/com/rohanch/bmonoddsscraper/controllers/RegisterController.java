package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.models.db.User;
import com.rohanch.bmonoddsscraper.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = {"*"}, allowCredentials = "true")
public class RegisterController {
	@Autowired
	private UserDetailService userDetailService;

	//TODO add annotation javax validation
	@PostMapping("/register")
	public void register(@RequestBody User newUser) {
		if (isInvalidInput(newUser.getUsername())) {
			throw new InvalidSyntaxCredentials("invalid username");
		} else if (isInvalidInput(newUser.getPassword())) {
			throw new InvalidSyntaxCredentials("invalid password");
		}

		userDetailService.register(newUser);
	}

	private boolean isInvalidInput(String input) {
		return input.length() < 5 || input.length() > 15 || !input.matches("[A-Za-z0-9_]+");
	}

	class InvalidSyntaxCredentials extends RuntimeException {
		InvalidSyntaxCredentials(String s) {
			super(s);
		}
	}
}
