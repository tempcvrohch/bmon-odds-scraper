package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Transactional
public class LiveMatchesService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchStateRepository matchStateRepository;

	@Autowired
	private BetResultService betResultService;

	@Autowired
	private MarketStateRepository marketStateRepository;

	private List<Match> persistedMatchEntities = new ArrayList<>(); //contains matches of last 24H
	private List<Match> liveMatchEntities = new ArrayList<>(); //contains matches of the current UpdateMatches tick
	public List<Match> getLiveMatchEntities() {
		return liveMatchEntities;
	}

	/**
	 * Compares updatedMatches with the cached persistedMatches and upserts new Match/Market states if changed.
	 *
	 * @param updatedMatches The currently scraped list of matches.
	 */
//	@Transactional
	void UpdateMatches(List<Match> updatedMatches) {
		if (persistedMatchEntities.isEmpty()) {
			persistedMatchEntities = getRecentMatchesFromDB();
		} else {
			processDisappearedMatches(liveMatchEntities, updatedMatches);
		}

		liveMatchEntities = updatedMatches;

		logger.debug("Checking {} matches.", liveMatchEntities.size());

		liveMatchEntities.forEach(this::processUpdatedLiveMatch);
	}

	private void processUpdatedLiveMatch(Match liveMatch) {
		var optMatch = persistedMatchEntities
				.stream()
				.filter(fMatch -> fMatch.equals(liveMatch))
				.findFirst();

		if (optMatch.isPresent()) {
			var persistedMatch = optMatch.get();
			updateMatchNestedElements(persistedMatch, liveMatch);
		} else {
			persistNewMatch(liveMatch);
		}
	}

	private void processDisappearedMatches(List<Match> previousMatchEntities, List<Match> currentMatchEntities) {
		previousMatchEntities.forEach(prevMatch -> {
			var optCurMatch = currentMatchEntities.stream()
					.filter(curMatch -> curMatch.equals(prevMatch)).findFirst();

			if (optCurMatch.isEmpty()) {
				//this prevMatch doesn't have the db reference so find the correct one.
				var optLiveMatch = persistedMatchEntities.stream()
						.filter(persistedMatch -> persistedMatch.equals(prevMatch)).findFirst();

				optLiveMatch.ifPresent(match -> betResultService.ProcessUserBetsOnMatch(match));
			}
		});
	}

	/**
	 * In case of an application restart the persisted matches variable should be consistent with what is in the database.
	 */
	private ArrayList<Match> getRecentMatchesFromDB() {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -2);
		return new ArrayList<>(matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis())));
	}

	/**
	 * Sets a parent reference of Match on MatchState and MatchState on multiple MarketStates
	 *
	 * @param match to be updated.
	 * @return the updated match with referenced parents.
	 */
	private Match setParentReferencesOnChildren(Match match, Match targetMatch) {
		match.getMatchState().setMatch(targetMatch);
		for (var marketState : match.getMatchState().getMarketStates()) {
			marketState.setMatchState(targetMatch.getMatchState());
		}

		return match;
	}

	/**
	 * Persist a match and the associated MatchState and MarketStates to the DB and cache list
	 *
	 * @param newMatch a match not yet added to the DB
	 */
	private void persistNewMatch(Match newMatch) {
		var preparedMatch = setParentReferencesOnChildren(newMatch, newMatch);

		//the match object needs a db reference to the latest matchState(which isn't set yet) so unset it first
		var transientMatchState = preparedMatch.getMatchState();
		preparedMatch.setMatchState(null);

		//save the Match without the MatchState
		var insertedMatch = matchRepository.save(preparedMatch);
		//save the matchState(this one has a reference to its parent)
		transientMatchState = matchStateRepository.save(transientMatchState);

		//the match state now has an id and can be added to the existing match record
		insertedMatch.setMatchState(transientMatchState);
		//TODO JPA doesn't trigger on setMatchState?
		insertedMatch = matchRepository.save(insertedMatch);

		var insertedMarketStates = marketStateRepository.saveAll(insertedMatch.getMatchState().getMarketStates());

		//add the inserted marketStates back to the insertedMatch object(they now also have DB ids)
		insertedMatch.getMatchState().setMarketStates(insertedMarketStates);
		persistedMatchEntities.add(insertedMatch);
	}

	/**
	 * Checks if either market states have changed between the current and new match update, if so persist.
	 * Checks if the match state has changed between the current and new match update, if so persist.
	 * <p>
	 * Sets the new match state as the current match state(eg a point score of 0-0 -> 0-1).
	 * <p>
	 * Both params should refer to the same match (eg persistedMatch.name = "PSV vs Ajax" and updatedMatch.name = "PSV vs Ajax").
	 *  @param persistedMatch current persisted match
	 * @param updatedMatch   the updated match
	 */
	private void updateMatchNestedElements(Match persistedMatch, Match updatedMatch) {
		//The updatedMatch is directly from bet365 and doesn't have JPA Parents set so copy the ones of our cached match
		var preparedMatch = setParentReferencesOnChildren(updatedMatch, persistedMatch);

		//Check for a point/set/serve index/score difference
		if (!persistedMatch.getMatchState().equals(preparedMatch.getMatchState())) {
			persistedMatch.setMatchState(preparedMatch.getMatchState());
			var insertedMatchState = matchStateRepository.save(persistedMatch.getMatchState());

			//have the Match object in the DB reference to the most recent MatchState
			persistedMatch.setMatchState(insertedMatchState);
			matchRepository.save(persistedMatch);
		}

		if (persistedMatch.getMatchState().getMarketStates() == null ||
				!persistedMatch.getMatchState().getMarketStates().equals(preparedMatch.getMatchState().getMarketStates())) {

			persistedMatch.getMatchState().setMarketStates(preparedMatch.getMatchState().getMarketStates());
			marketStateRepository.saveAll(persistedMatch.getMatchState().getMarketStates());

			//MarketStates are not interesting enough to update together with the Match object so don't update the Match object.
		}
	}
}
