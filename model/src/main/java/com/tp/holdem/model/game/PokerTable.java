package com.tp.holdem.model.game;

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
}