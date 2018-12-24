package com.tp.holdem.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
	private List<Moves> possibleMoves = new ArrayList<>();
	@Builder.Default
	private List<Card> hand = new ArrayList<>();
	private HandRank handRank;

	public void addCard(final Card card) {
		hand.add(card);
	}

	public boolean playing() {
		return isInGame() && !isFolded();
	}
}
