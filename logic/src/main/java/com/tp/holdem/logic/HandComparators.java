package com.tp.holdem.logic;

import com.tp.holdem.model.game.Card;
import io.vavr.collection.List;

import java.util.Comparator;
import java.util.function.Supplier;

class HandComparators {
	private static final Supplier<IllegalStateException> COMBO_NOT_FOUND = () -> new IllegalStateException("Card combination could not be found");

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

	static Comparator<List<Card>> highestOverallValue() {
		return Comparator.comparingInt(cards -> cards.map(Card::getValue).sum().intValue());
	}

	static Comparator<? super List<Card>> highestFullHouse() {
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

	static Comparator<? super List<Card>> highestFour() {
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

	static Comparator<? super List<Card>> highestThree() {
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

	static Comparator<? super List<Card>> highestPair() {
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

	static Comparator<List<Card>> highestPairs() {
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

	static Comparator<List<Card>> highestKicker() {
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

	public static Comparator<List<Card>> highestStraightKicker() {
		return (a, b) -> {
			final boolean aAceStraight = HandPredicates.ACE_STRAIGHT.test(a);
			final boolean bAceStraight = HandPredicates.ACE_STRAIGHT.test(b);

			if (aAceStraight && bAceStraight)
				return 0;

			if (aAceStraight)
				return -1;

			if (bAceStraight)
				return 1;

			return highestKicker().compare(a, b);
		};
	}
}
