package com.tp.holdem.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PokerTable {
	private int potAmount;
	private int smallBlindAmount;
	private int bigBlindAmount;
	private List<Card> cardsOnTable;

	public void upBlinds() {
		smallBlindAmount *= 2;
		bigBlindAmount *= 2;
	}

	public void addCard(final Card card) {
		cardsOnTable.add(card);
	}
}