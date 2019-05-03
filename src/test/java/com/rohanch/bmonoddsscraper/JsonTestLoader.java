package com.rohanch.bmonoddsscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.models.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JsonTestLoader {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ResourceLoader resourceLoader;


	private String loadJsonStringFromFile(String strPath) throws IOException {
		var path = Path.of(resourceLoader.getResource(strPath).getURI());
		return new String(Files.readAllBytes(path));
	}

	//TODO refractor these to accept a type?
	public Match loadMatchFromPath(String strPath) throws IOException {
		return objectMapper.readValue(loadJsonStringFromFile(strPath), Match.class);
	}

	public Bet loadBetFromPath(String strPath) throws IOException {
		return objectMapper.readValue(loadJsonStringFromFile(strPath), Bet.class);
	}

	public User loadUserFromPath(String strPath) throws IOException {
		return objectMapper.readValue(loadJsonStringFromFile(strPath), User.class);
	}
}
