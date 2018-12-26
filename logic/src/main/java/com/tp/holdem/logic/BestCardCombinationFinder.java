package com.tp.holdem.logic;

import com.tp.holdem.model.game.Card;
import com.tp.holdem.model.game.Hands;
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
				.maxBy(highestFullHouse())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getPairCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isPair)
				.maxBy(highestPair())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getTwoPairCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isTwoPair)
				.maxBy(highestPairs())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getThreeOfAKindCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isThreeOfAKind)
				.maxBy(highestThree())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getStraightFlushCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isStraightFlush)
				.maxBy(highestKicker())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getStraightCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isStraight)
				.maxBy(highestKicker())
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
				.maxBy(highestFour())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> getFlushCards(List<Card> cards) {
		return cards.combinations(5)
				.filter(HandFinder::isFlush)
				.maxBy(highestKicker())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> extractHighestPair(List<Card> cards) {
		return cards.combinations(2)
				.filter(HandFinder::isPair)
				.maxBy(highestOverallValue())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> extractHighestThree(List<Card> cards) {
		return cards.combinations(3)
				.filter(HandFinder::isThreeOfAKind)
				.maxBy(highestOverallValue())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static List<Card> extractHighestFour(List<Card> cards) {
		return cards.combinations(4)
				.filter(HandFinder::isFourOfAKind)
				.maxBy(highestOverallValue())
				.getOrElseThrow(COMBO_NOT_FOUND);
	}

	private static Comparator<? super List<Card>> highestFullHouse() {
		return (a, b) -> {
			final List<Card> aFirstThree = extractHighestThree(a);
			final List<Card> bFirstThree = extractHighestThree(b);

			int compareFirstThrees = highestOverallValue().compare(aFirstThree, bFirstThree);
			if (compareFirstThrees != 0)
				return compareFirstThrees;

			a = a.removeAll(aFirstThree);
			b = b.removeAll(bFirstThree);

			return highestPair().compare(a, b);
		};
	}

	private static Comparator<? super List<Card>> highestFour() {
		return (a, b) -> {
			final List<Card> aFirstFour = extractHighestFour(a);
			final List<Card> bFirstFour = extractHighestFour(b);

			int compareFirstFours = highestOverallValue().compare(aFirstFour, bFirstFour);
			if (compareFirstFours != 0)
				return compareFirstFours;

			a = a.removeAll(aFirstFour);
			b = b.removeAll(bFirstFour);

			return highestKicker().compare(a, b);
		};
	}

	private static Comparator<? super List<Card>> highestThree() {
		return (a, b) -> {
			final List<Card> aFirstThree = extractHighestThree(a);
			final List<Card> bFirstThree = extractHighestThree(b);

			int compareFirstThrees = highestOverallValue().compare(aFirstThree, bFirstThree);
			if (compareFirstThrees != 0)
				return compareFirstThrees;

			a = a.removeAll(aFirstThree);
			b = b.removeAll(bFirstThree);

			return highestKicker().compare(a, b);
		};
	}

	private static Comparator<? super List<Card>> highestPair() {
		return (a, b) -> {
			final List<Card> aFirstPair = extractHighestPair(a);
			final List<Card> bFirstPair = extractHighestPair(b);

			int compareFirstPairs = highestOverallValue().compare(aFirstPair, bFirstPair);
			if (compareFirstPairs != 0)
				return compareFirstPairs;

			a = a.removeAll(aFirstPair);
			b = b.removeAll(bFirstPair);

			return highestKicker().compare(a, b);
		};
	}

	private static Comparator<List<Card>> highestPairs() {
		return (a, b) -> {
			final List<Card> aFirstPair = extractHighestPair(a);
			final List<Card> bFirstPair = extractHighestPair(b);

			int compareFirstPairs = highestOverallValue().compare(aFirstPair, bFirstPair);
			if (compareFirstPairs != 0)
				return compareFirstPairs;

			a = a.removeAll(aFirstPair);
			b = b.removeAll(bFirstPair);

			final List<Card> aSecondPair = extractHighestPair(a);
			final List<Card> bSecondPair = extractHighestPair(b);

			int compareSecondPairs = highestOverallValue().compare(aSecondPair, bSecondPair);
			if (compareSecondPairs != 0)
				return compareSecondPairs;

			a = a.removeAll(aSecondPair);
			b = b.removeAll(bSecondPair);

			return highestOverallValue().compare(a, b);
		};
	}

	private static Comparator<List<Card>> highestKicker() {
		return (a, b) -> {
			for (int i = a.length() - 1; i >= 0; i--) {
				int firstValue = a.get(i).getValue();
				int secondValue = b.get(i).getValue();

				int compare = Integer.compare(firstValue, secondValue);

				if (compare != 0)
					return compare;
			}

			return 0;
		};
	}

	private static Comparator<List<Card>> highestOverallValue() {
		return Comparator.comparingInt(cards -> cards.map(Card::getValue).sum().intValue());
	}
}
