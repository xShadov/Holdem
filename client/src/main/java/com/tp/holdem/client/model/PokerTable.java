package com.tp.holdem.client.model;

import com.google.common.collect.Lists;
import lombok.Data;

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
	private List<Card> cardsOnTable = Lists.newArrayList();

	public void addCard(final Card card) {
		cardsOnTable.add(card);
	}
}