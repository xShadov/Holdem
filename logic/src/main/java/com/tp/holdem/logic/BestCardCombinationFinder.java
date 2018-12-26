package com.tp.holdem.logic;

import com.tp.holdem.model.game.Card;
import com.tp.holdem.model.game.Hands;
import io.vavr.collection.List;

import java.util.Comparator;

public class BestCardCombinationFinder {
	private static final Comparator<Card> CARD_COMPARATOR = Comparator.comparing(Card::getValue);

	public static List<Card> findBestCardsForHand(List<Card> cards, Hands hand) {
		cards = cards.sorted(CARD_COMPARATOR);

		return List.empty();
	}
}
