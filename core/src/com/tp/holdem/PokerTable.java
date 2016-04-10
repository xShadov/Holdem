package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class PokerTable {

	private int pot;
	private List<Card> cardsOnTable;
	private int smallBlindAmount;
	private int bigBlindAmount;
	private String limitType;
	private int fixedLimit;
	private int fixedRaiseCount;
	private int raiseCount;

	public PokerTable() {
		this.pot = 0;
		this.cardsOnTable = new ArrayList<Card>();
	}

	public void setPot(final int newPot) {
		this.pot = newPot;
	}

	public int getPot() {
		return pot;
	}

	public List<Card> getCardList() {
		return cardsOnTable;
	}

	public void addCard(final Card card) {
		cardsOnTable.add(card);
	}

	public List<Card> getCardsOnTable() {
		return cardsOnTable;
	}

	public void setCardsOnTable(final List<Card> cardsOnTable) {
		this.cardsOnTable = cardsOnTable;
	}

	public int getSmallBlindAmount() {
		return smallBlindAmount;
	}

	public void setSmallBlindAmount(final int smallBlindAmount) {
		this.smallBlindAmount = smallBlindAmount;
	}

	public int getBigBlindAmount() {
		return bigBlindAmount;
	}

	public void setBigBlindAmount(final int bigBlindAmount) {
		this.bigBlindAmount = bigBlindAmount;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(final String limitType) {
		this.limitType = limitType;
	}

	public int getFixedLimit() {
		return fixedLimit;
	}

	public void setFixedLimit(final int fixedLimit) {
		this.fixedLimit = fixedLimit;
	}

	public int getFixedRaiseCount() {
		return fixedRaiseCount;
	}

	public void setFixedRaiseCount(final int fixedRaiseCount) {
		this.fixedRaiseCount = fixedRaiseCount;
	}

	public int getRaiseCount() {
		return raiseCount;
	}

	public void setRaiseCount(final int raiseCount) {
		this.raiseCount = raiseCount;
	}
}