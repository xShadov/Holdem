package com.tp.holdem.client.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class Player {
	private int number;
	private int connectionId;

	private int betAmount;
	private int betAmountThisRound;
	private int chipsAmount;
	private int fromWhichPot;

	private boolean inGame;
	private boolean isAllIn;
	private boolean isFolded;
	private boolean hasDealerButton;
	private boolean hasBigBlind;
	private boolean hasSmallBlind;

	private String name;

	private List<Card> hand;
	private HandRank handRank;

	private Player(int number, int connectionId, int betAmount,
				   int betAmountThisRound, int chipsAmount, int fromWhichPot,
				   boolean inGame, boolean isAllIn, boolean isFolded,
				   boolean hasDealerButton, boolean hasBigBlind, boolean hasSmallBlind,
				   String name, List<Card> hand, HandRank handRank) {
		this.number = number;
		this.connectionId = connectionId;
		this.betAmount = betAmount;
		this.betAmountThisRound = betAmountThisRound;
		this.chipsAmount = chipsAmount;
		this.fromWhichPot = fromWhichPot;
		this.inGame = inGame;
		this.isAllIn = isAllIn;
		this.isFolded = isFolded;
		this.hasDealerButton = hasDealerButton;
		this.hasBigBlind = hasBigBlind;
		this.hasSmallBlind = hasSmallBlind;
		this.name = name;
		this.hand = MoreObjects.firstNonNull(hand, Lists.newArrayList());
		this.handRank = handRank;
	}

	public void addCard(final Card card) {
		hand.add(card);
	}

	public void clearHand() {
		hand.clear();
	}

	public static boolean playing(Player player) {
		return player.isInGame() && !player.isFolded();
	}
}
