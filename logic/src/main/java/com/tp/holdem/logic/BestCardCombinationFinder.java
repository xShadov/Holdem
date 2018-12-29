package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Card;
import com.tp.holdem.logic.model.Hands;
import io.vavr.collection.List;

import java.util.Comparator;
import java.util.function.Supplier;

class BestCardCombinationFinder {
	private static final Supplier<IllegalStateException> COMBO_NOT_FOUND = () -> new IllegalStateException("Card combination could not be found");

	private static final Comparator<Card> CARD_COMPARATOR = Comparator.comparing(Card::getValue);

	static List<Card> findBestCardsForHand(List<Card> cards, Hands hand) {
		if (cards.size() != 7)
			throw new IllegalArgumentException("There should be 7 cards (2 player + 5 table)");

		cards = cards.sorted(CARD_COMPARATOR);

		if (hand == Hands.HIGH_CARD)
			return cards.takeRight(5);
		if (hand == Hands.PAIR)
			return getPairCards(cards);
		if (hand == Hands.TWO_PAIR)
			return getTwoPairCards(cards);
		if (hand == Hands.THREE_OF_A_KIND)
			return getThreeOfAKindCards(cards);
		if (hand == Hands.STRAIGHT)
			return getStraightCards(cards);
		if (hand == Hands.FLUSH)
			return getFlushCards(cards);
		if (hand == Hands.FOUR_OF_A_KIND)
			return getFourOfAKindCards(cards);
		if (hand == Hands.FULL_HOUSE)
			return getFullHouseCards(cards);
		if (hand == Hands.STRAIGHT_FLUSH)
			return getStraightFlushCards(cards);
		if (hand == Hands.ROYAL_FLUSH)
			return getRoyalFlushCards(cards);

		return List.empty();
	}

	private static List<Card> getFullHouseCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isFullHouse)
				.maxBy(HandComparators.highestFullHouse())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getPairCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isPair)
				.maxBy(HandComparators.highestPair())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getTwoPairCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isTwoPair)
				.maxBy(HandComparators.highestPairs())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getThreeOfAKindCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isThreeOfAKind)
				.maxBy(HandComparators.highestThree())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getStraightFlushCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isStraightFlush)
				.maxBy(HandComparators.highestStraightKicker())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getStraightCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isStraight)
				.maxBy(HandComparators.highestStraightKicker())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getRoyalFlushCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isRoyalFlush)
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getFourOfAKindCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isFourOfAKind)
				.maxBy(HandComparators.highestFour())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getFlushCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isFlush)
				.maxBy(HandComparators.highestKicker())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}
}
