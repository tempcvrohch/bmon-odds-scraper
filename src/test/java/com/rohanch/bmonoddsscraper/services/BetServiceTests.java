package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.JsonTestLoader;
import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.db.User;
import com.rohanch.bmonoddsscraper.repositories.BetRepository;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BetServiceTests {
	@Autowired
	private JsonTestLoader jsonTestLoader;

	@Autowired
	@InjectMocks
	private BetService betService;

	@MockBean
	private BetRepository betRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private MarketStateRepository marketStateRepository;

	private User user;
	private Bet bet;

	@Before
	public void loadTemplates() throws IOException {
		user = jsonTestLoader.loadUserFromPath("classpath:templates\\user.json");
		bet = jsonTestLoader.loadBetFromPath("classpath:templates\\bet.json");
	}

	@Test(expected = BetService.InsufficientBalanceException.class)
	public void throwsInsufficientBalanceWhenAddingBet() {
		user.setBalance(1f);
		betService.AddBet(user, bet);
	}

	@Test(expected = BetService.StakeOutOfBoundsException.class)
	public void throwsStakeOutOfBoundWhenAddingBet() {
		bet.setStake(101f);
		betService.AddBet(user, bet);
	}

	@Test(expected = BetService.BetAlreadyPlacedException.class)
	public void throwsAlreadyPlacedWhenAddingBet() {
		Mockito.when(betRepository.GetBetOnMarketStateIdAndUserId(bet.getMarketState().getId(), user.getId())).thenReturn(bet);
		betService.AddBet(user, bet);
	}

	@Test(expected = BetService.UnknownMarketStateOnBetException.class)
	public void throwsUnknownMarketStateWhenAddingBet() {
		Mockito.when(marketStateRepository.findById(bet.getMarketState().getId())).thenReturn(Optional.empty());
		betService.AddBet(user, bet);
	}

	@Test
	public void addsBetAndChangesUserBalance() {
		var optMarketState = Optional.of(bet.getMarketState());
		Mockito.when(marketStateRepository.findById(bet.getMarketState().getId())).thenReturn(optMarketState);

		betService.AddBet(user, bet);

		Mockito.verify(betRepository).save(bet);
		Mockito.verify(userRepository).reduceBalanceByUsername(user.getId(), bet.getStake());
	}

	@Test
	public void incrementsBalanceAndUpdatesBetStatusOnWonBet() {
		betService.ProcessFinishedBet(bet, true);
		Mockito.verify(userRepository).incrementBalanceByUsername(bet.getUser().getId(), bet.getToReturn());
		Mockito.verify(betRepository).updateBetOnBetStatusById(bet.getUser().getId(), Bet.BetStatus.WIN);
	}

	@Test
	public void updatesBetStatusOnLostBet() {
		betService.ProcessFinishedBet(bet, false);
		Mockito.verify(betRepository).updateBetOnBetStatusById(bet.getUser().getId(), Bet.BetStatus.LOSS);
	}

	@Test
	public void updatesBetStatusAndReturnsStakeOnVoidBet() {
		betService.ProcessVoidBet(bet);
		Mockito.verify(userRepository).incrementBalanceByUsername(bet.getUser().getId(), bet.getStake());
		Mockito.verify(betRepository).updateBetOnBetStatusById(bet.getUser().getId(), Bet.BetStatus.VOID);
	}
}
