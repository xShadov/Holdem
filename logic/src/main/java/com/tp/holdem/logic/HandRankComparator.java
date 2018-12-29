package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Card;
import com.tp.holdem.logic.model.HandRank;
import com.tp.holdem.logic.model.Hands;
import io.vavr.collection.List;

import java.util.Comparator;

class HandRankComparator implements Comparator<HandRank> {
	static final HandRankComparator INSTANCE = new HandRankComparator();

	private HandRankComparator() {
	}

	@Override
	public int compare(final HandRank one, final HandRank two) {
		final int compareHands = Comparator.comparing(Hands::value).compare(one.getHand(), two.getHand());

		if (compareHands != 0)
			return compareHands;

		final Hands firstHand = one.getHand();
		final List<Card> firstCards = List.ofAll(one.getCardsThatMakeHand());
		final Hands secondHand = two.getHand();
		final List<Card> secondCards = List.ofAll(two.getCardsThatMakeHand());

		if (firstHand == Hands.HIGH_CARD)
			return HandComparators.highestKicker().compare(firstCards, secondCards);
		if (firstHand == Hands.PAIR)
			return HandComparators.highestPair().compare(firstCards, secondCards);
		if (firstHand == Hands.TWO_PAIR)
			return HandComparators.highestPairs().compare(firstCards, secondCards);
		if (firstHand == Hands.THREE_OF_A_KIND)
			return HandComparators.highestThree().compare(firstCards, secondCards);
		if (firstHand == Hands.STRAIGHT)
			return HandComparators.highestStraightKicker().compare(firstCards, secondCards);
		if (firstHand == Hands.FLUSH)
			return HandComparators.highestKicker().compare(firstCards, secondCards);
		if (firstHand == Hands.FULL_HOUSE)
			return HandComparators.highestFullHouse().compare(firstCards, secondCards);
		if (firstHand == Hands.FOUR_OF_A_KIND)
			return HandComparators.highestFour().compare(firstCards, secondCards);
		if (firstHand == Hands.STRAIGHT_FLUSH)
			return HandComparators.highestKicker().compare(firstCards, secondCards);
		if (firstHand == Hands.ROYAL_FLUSH)
			return 0;

		return 0;
	}
}
