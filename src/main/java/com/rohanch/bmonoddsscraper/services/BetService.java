package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.db.User;
import com.rohanch.bmonoddsscraper.repositories.BetRepository;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BetService {
	@Autowired
	private BetRepository betRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MarketStateRepository marketStateRepository;

	@Transactional
	public void AddBet(User user, Bet bet) {
		if (user.getBalance() < bet.getStake()) {
			throw new InsufficientBalanceException();
		}

		if (bet.getStake() < 0.25 || bet.getStake() > 100) {
			throw new StakeOutOfBoundsException();
		}

		var previousBet = betRepository.GetBetOnMarketStateIdAndUserId(bet.getMarketState().getId(), user.getId());
		if (previousBet != null) {
			throw new BetAlreadyPlacedException();
		}

		var targetMarketState = marketStateRepository.findById(bet.getMarketState().getId()).orElseThrow(UnknownMarketStateOnBetException::new);

		//TODO: this bookie is too lenient, check if targetMarketState matches the newest marketState of this match
		bet.setMarketState(targetMarketState);
		bet.setUser(user);
		bet.setStatus(Bet.BetStatus.PENDING);

		var decimalOdd = calculateFloatDecimalOdd(targetMarketState.getOdd());
		bet.setToReturn(bet.getStake() * decimalOdd);

		betRepository.save(bet);
		userRepository.reduceBalanceByUsername(user.getId(), bet.getStake()); //TODO: double-check how these subtractions are done in SQL
	}

	private Float calculateFloatDecimalOdd(String odd) {
		if (!odd.contains("/")) {
			throw new InvalidFractionalOddException(odd);
		}

		var oddValuesArray = odd.split("/");
		if (oddValuesArray.length != 2) {
			throw new InvalidFractionalOddException(odd);
		}

		var leftSideOdd = Float.parseFloat(oddValuesArray[0]);
		var rightSideOdd = Float.parseFloat(oddValuesArray[1]);

		return 1 + (leftSideOdd / rightSideOdd);
	}

	void ProcessFinishedBet(Bet bet, boolean wonBet) {
		Bet.BetStatus status = Bet.BetStatus.LOSS;
		if (wonBet) {
			status = Bet.BetStatus.WIN;
			userRepository.incrementBalanceByUsername(bet.getUser().getId(), bet.getToReturn());
		}

		betRepository.updateBetOnBetStatusById(bet.getUser().getId(), status);//.name()
	}

	void ProcessVoidBet(Bet bet) {
		userRepository.incrementBalanceByUsername(bet.getUser().getId(), bet.getStake());
		betRepository.updateBetOnBetStatusById(bet.getUser().getId(), Bet.BetStatus.VOID); //.name()
	}

	//TODO perhaps create a AddBetException, all of these cause a 400 anyway
	public class InsufficientBalanceException extends RuntimeException {
	}

	public class BetAlreadyPlacedException extends RuntimeException {
	}

	public class UnknownMarketStateOnBetException extends RuntimeException {
	}

	public class StakeOutOfBoundsException extends RuntimeException {
	}

	public class InvalidFractionalOddException extends RuntimeException {
		InvalidFractionalOddException(String s) {
			super(s);
		}
	}
}