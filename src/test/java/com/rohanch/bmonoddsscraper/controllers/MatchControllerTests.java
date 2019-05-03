package com.rohanch.bmonoddsscraper.controllers;

import com.rohanch.bmonoddsscraper.JsonTestLoader;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.services.LiveMatchesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringRunner.class)
//@WebMvcTest(MatchController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MatchControllerTests {
	@Autowired
	private JsonTestLoader jsonTestLoader;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LiveMatchesService liveMatchesService;

	@MockBean
	private MatchRepository matchRepository;

	private Match match;
	private Match matchLive;
	private List<Match> resultMatches = new ArrayList<>();

	@Before
	public void loadTemplates() throws IOException {
		match = jsonTestLoader.loadMatchFromPath("classpath:templates\\match.json");
		matchLive = jsonTestLoader.loadMatchFromPath("classpath:templates\\match-live.json");

		resultMatches.add(match);
		resultMatches.add(matchLive);
	}

	@Test
	public void givenRecentMatches_whenGetRecentMatches_thenReturnJsonArray() throws Exception {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);

		Mockito.when(matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis()))).thenReturn(resultMatches);
		Mockito.when(liveMatchesService.getLiveMatchEntities()).thenReturn(resultMatches);

		//TODO test json res also
		mvc.perform(MockMvcRequestBuilders.get("/matches/recent")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
