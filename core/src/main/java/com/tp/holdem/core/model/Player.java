package com.tp.holdem.core.model;

import com.tp.holdem.core.strategy.Strategy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player {
	private int number = -1;
	private List<Card> hand = new ArrayList<>();
	private String name = "";
	private boolean hasDealerButton;
	private boolean hasBigBlind;
	private boolean hasSmallBlind;
	private int betAmount;
	private int betAmountThisRound;
	private int chipsAmount;
	private boolean isFolded;
	private int connectionId;
	private boolean inGame = true;
	private boolean isAllIn;
	private int fromWhichPot = -1;
	private HandRank handRank;

	public Player() {

	}

	public void setAllProperties(final Player player) {
		this.number = player.getNumber();
		this.name = player.getName();
		this.hasDealerButton = player.isHasDealerButton();
		this.hasBigBlind = player.isHasBigBlind();
		this.hasSmallBlind = player.isHasSmallBlind();
		this.betAmount = player.getBetAmount();
		this.betAmountThisRound = player.getBetAmountThisRound();
		this.chipsAmount = player.getChipsAmount();
		this.isFolded = player.isFolded;
		this.inGame = player.isInGame();
		this.isAllIn = player.isAllIn;
		this.fromWhichPot = player.getFromWhichPot();
	}

	public Player(final int number) {
		this.number = number;
		this.name = "Player" + number;
	}

	public Player(final int number, final String name) {
		this.number = number;
		this.name = name;
	}

	public void addCard(final Card card) {
		hand.add(card);
	}

	public Strategy getStrategy() {
		return null;
	}

	public void clearHand() {
		hand.clear();
	}

}
