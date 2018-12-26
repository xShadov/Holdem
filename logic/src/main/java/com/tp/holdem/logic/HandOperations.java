package com.tp.holdem.logic;

import com.tp.holdem.model.game.*;
import io.vavr.collection.List;

import java.util.Comparator;

public class HandOperations {
	private static final Comparator<Card> CARD_COMPARATOR = Comparator.comparing(Card::getValue);

	public static HandRank findHandRank(Player player, PokerTable pokerTable) {
		final List<Card> cards = List.ofAll(player.getHand())
				.appendAll(pokerTable.getCardsOnTable())
				.sorted(CARD_COMPARATOR);

		final Hands playerHand = HandFinder.findHand(cards);
		final List<Card> bestCardsThatMakeHand = BestCardCombinationFinder.findBestCardsForHand(cards, playerHand);

		return new HandRank(player, playerHand, bestCardsThatMakeHand.toJavaList());
	}
}