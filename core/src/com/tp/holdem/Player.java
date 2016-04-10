package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private int number = -1;
	private List<Card> hand = new ArrayList<Card>();
	private String name = "";
	private boolean hasDealerButton = false;
	private boolean hasBigBlind = false;
	private boolean hasSmallBlind = false;
	private int betAmount = 0;
	private int betAmountThisRound = 0;
	private int chipsAmount = 0;
	private boolean isFolded = false;
	private int connectionId = 0;
	private boolean inGame = true;
	private boolean isAllIn = false;
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

	public List<Card> getHand() {
		return hand;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public boolean isFolded() {
		return isFolded;
	}

	public void setFolded(final boolean isFolded) {
		this.isFolded = isFolded;
	}

	public boolean isHasDealerButton() {
		return hasDealerButton;
	}

	public void setHasDealerButton(final boolean hasDealerButton) {
		this.hasDealerButton = hasDealerButton;
	}

	public boolean isHasBigBlind() {
		return hasBigBlind;
	}

	public void setHasBigBlind(final boolean hasBigBlind) {
		this.hasBigBlind = hasBigBlind;
	}

	public boolean isHasSmallBlind() {
		return hasSmallBlind;
	}

	public void setHasSmallBlind(final boolean hasSmallBlind) {
		this.hasSmallBlind = hasSmallBlind;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(final int betAmount) {
		this.betAmount = betAmount;
	}

	public int getChipsAmount() {
		return chipsAmount;
	}

	public void setChipsAmount(final int chipsAmount) {
		this.chipsAmount = chipsAmount;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(final int connectionId) {
		this.connectionId = connectionId;
	}

	public void setHand(final List<Card> hand) {
		this.hand = hand;
	}

	public void setInGame(final boolean inGame) {
		this.inGame = inGame;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public boolean isAllIn() {
		return isAllIn;
	}

	public void setAllIn(final boolean isAllIn) {
		this.isAllIn = isAllIn;
	}

	public Strategy getStrategy() {
		return null;
	}

	public void clearHand() {
		this.hand = new ArrayList<Card>();
	}

	public int getBetAmountThisRound() {
		return betAmountThisRound;
	}

	public void setBetAmountThisRound(final int betAmountThisRound) {
		this.betAmountThisRound = betAmountThisRound;
	}

	public int getFromWhichPot() {
		return fromWhichPot;
	}

	public void setFromWhichPot(final int fromWhichPot) {
		this.fromWhichPot = fromWhichPot;
	}

	public HandRank getHandRank() {
		return handRank;
	}

	public void setHandRank(final HandRank handRank) {
		this.handRank = handRank;
	}

}
