package com.tp.holdem.logic.model;

import com.tp.holdem.model.common.Moves;
import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import com.tp.holdem.model.message.dto.PlayerDTO;
import io.vavr.collection.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Player {
	private int number;

	private int minimumBet;
	private int maximumBet;
	private int betAmount;
	private int betAmountThisPhase;
	private int chipsAmount;

	private boolean inGame;
	private boolean allIn;
	private boolean folded;

	private String name;

	@Builder.Default
	private List<Moves> possibleMoves = List.empty();
	@Builder.Default
	private List<Card> hand = List.empty();

	public static Player numbered(int number) {
		return Player.builder()
				.name("Player" + number)
				.number(number)
				.build();
	}

	public Player withCards(List<Card> cards) {
		return this.toBuilder().hand(cards).build();
	}

	public boolean playing() {
		return isInGame() && !isFolded();
	}

	public boolean notPlaying() {
		return !playing();
	}

	public int availableChips() {
		return chipsAmount - betAmountThisPhase;
	}

	public Player allIn() {
		return this.toBuilder()
				.allIn(true)
				.betAmount(chipsAmount)
				.build();
	}

	public Player fold() {
		return this.toBuilder()
				.folded(true)
				.build();
	}

	public Player bet(int bet) {
		if (availableChips() < bet)
			throw new IllegalArgumentException("Player does not have enough chips");
		return this.toBuilder()
				.betAmountThisPhase(getBetAmountThisPhase() + bet)
				.build();
	}

	public Player newPhase() {
		return this.toBuilder()
				.betAmount(getBetAmount() + getBetAmountThisPhase())
				.betAmountThisPhase(0)
				.chipsAmount(getChipsAmount() - getBetAmountThisPhase())
				.allIn(false)
				.folded(false)
				.minimumBet(0)
				.maximumBet(0)
				.possibleMoves(List.empty())
				.build();
	}

	public Player newRound() {
		return this.toBuilder()
				.betAmount(0)
				.betAmountThisPhase(0)
				.possibleMoves(List.empty())
				.maximumBet(0)
				.minimumBet(0)
				.folded(false)
				.allIn(false)
				.hand(List.empty())
				.build();
	}

	public PlayerDTO toPlayerDTO() {
		return PlayerDTO.builder()
				.allIn(allIn)
				.betAmount(betAmount)
				.betAmountThisPhase(betAmountThisPhase)
				.chipsAmount(chipsAmount)
				.folded(folded)
				.inGame(inGame)
				.name(name)
				.number(number)
				.build();
	}

	public CurrentPlayerDTO toCurrentPlayerDTO() {
		return CurrentPlayerDTO.builder()
				.allIn(allIn)
				.betAmount(betAmount)
				.betAmountThisPhase(betAmountThisPhase)
				.chipsAmount(chipsAmount)
				.folded(folded)
				.inGame(inGame)
				.name(name)
				.number(number)
				.possibleMoves(possibleMoves.toJavaList())
				.maximumBet(maximumBet)
				.minimumBet(minimumBet)
				.hand(hand.map(Card::toDTO).toJavaList())
				.build();
	}

	public Player bettingTurnOver() {
		return this.toBuilder()
				.minimumBet(0)
				.maximumBet(0)
				.possibleMoves(List.empty())
				.build();
	}
}
