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
	private int chipsAmount;

	private boolean inGame;
	private boolean allIn;
	private boolean folded;
	private boolean dealerButton;
	private boolean bigBlind;
	private boolean smallBlind;

	private String name;

	@Builder.Default
	private List<Moves> possibleMoves = List.empty();
	@Builder.Default
	private List<Card> hand = List.empty();

	public Player withCards(List<Card> cards) {
		return this.toBuilder().hand(cards).build();
	}

	public boolean playing() {
		return isInGame() && !isFolded();
	}

	public boolean notPlaying() {
		return !playing();
	}

	public PlayerDTO toPlayerDTO() {
		return PlayerDTO.builder()
				.allIn(allIn)
				.betAmount(betAmount)
				.bigBlind(bigBlind)
				.chipsAmount(chipsAmount)
				.dealerButton(dealerButton)
				.folded(folded)
				.inGame(inGame)
				.name(name)
				.number(number)
				.smallBlind(smallBlind)
				.build();
	}

	public CurrentPlayerDTO toCurrentPlayerDTO() {
		return CurrentPlayerDTO.builder()
				.allIn(allIn)
				.betAmount(betAmount)
				.bigBlind(bigBlind)
				.chipsAmount(chipsAmount)
				.dealerButton(dealerButton)
				.folded(folded)
				.inGame(inGame)
				.name(name)
				.number(number)
				.smallBlind(smallBlind)
				.possibleMoves(possibleMoves.toJavaList())
				.maximumBet(maximumBet)
				.minimumBet(minimumBet)
				.hand(hand.map(Card::toDTO).toJavaList())
				.build();
	}
}
