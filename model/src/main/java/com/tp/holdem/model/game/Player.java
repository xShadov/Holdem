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
	private int connectionId;

	private int minimumBet;
	private int maximumBet;
	private int betAmount;
	private int chipsAmount;

	private boolean inGame;
	private boolean isAllIn;
	private boolean isFolded;
	private boolean hasDealerButton;
	private boolean hasBigBlind;
	private boolean hasSmallBlind;

	private String name;

	@Builder.Default
	private List<Moves> possibleMoves = new ArrayList<>();
	@Builder.Default
	private List<Card> hand = new ArrayList<>();
	private HandRank handRank;

	public void addCard(final Card card) {
		hand.add(card);
	}

	public void clearHand() {
		hand.clear();
	}

	public static boolean playing(Player player) {
		return player.isInGame() && !player.isFolded();
	}

	public int getAvailableChips() {
		return chipsAmount - betAmount;
	}
}
