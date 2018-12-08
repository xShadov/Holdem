package com.tp.holdem.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PokerTable {
	private int pot;
	private int smallBlindAmount;
	private int bigBlindAmount;
	private int fixedLimit;
	private int fixedRaiseCount;
	private int raiseCount;
	private String limitType;
	private List<Card> cardsOnTable;

	public PokerTable() {
		this.cardsOnTable = new ArrayList<>();
	}

	public void addCard(final Card card) {
		cardsOnTable.add(card);
	}
}