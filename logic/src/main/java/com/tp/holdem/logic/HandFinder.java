package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Card;
import com.tp.holdem.model.common.Hands;
import io.vavr.collection.List;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class HandFinder {
	private static final Comparator<Card> CARD_COMPARATOR = Comparator.comparing(Card::getValue);

	static Hands findHand(List<Card> cards) {
		if (cards.size() != 7)
			throw new IllegalArgumentException("There should be 7 cards (2 player + 5 table)");

		cards = cards.sorted(CARD_COMPARATOR);

		if (isRoyalFlush(cards))
			return Hands.ROYAL_FLUSH;
		if (isStraightFlush(cards))
			return Hands.STRAIGHT_FLUSH;
		if (isFourOfAKind(cards))
			return Hands.FOUR_OF_A_KIND;
		if (isFullHouse(cards))
			return Hands.FULL_HOUSE;
		if (isFlush(cards))
			return Hands.FLUSH;
		if (isStraight(cards))
			return Hands.STRAIGHT;
		if (isThreeOfAKind(cards))
			return Hands.THREE_OF_A_KIND;
		if (isTwoPair(cards))
			return Hands.TWO_PAIR;
		if (isPair(cards))
			return Hands.PAIR;
		return Hands.HIGH_CARD;
	}

	static boolean isRoyalFlush(final List<Card> cards) {
		return cards
				.combinations(5)
				.exists(HandPredicates.ROYAL_FLUSH);
	}

	static boolean isStraightFlush(final List<Card> cards) {
		return cards
				.combinations(5)
				.exists(HandPredicates.STRAIGHT_FLUSH);
	}

	static boolean isFourOfAKind(final List<Card> cards) {
		return countCardsBy(cards, Card::getHonour)
				.values()
				.contains(4L);
	}

	static boolean isFullHouse(final List<Card> cards) {
		final List<Long> honourCounts = List.ofAll(countCardsBy(cards, Card::getHonour).values());
		return honourCounts.containsAll(Arrays.asList(2L, 3L)) || honourCounts.filter(count -> count == 3).size() == 2;
	}

	static boolean isStraight(final List<Card> cards) {
		return cards
				.distinctBy(Card::getHonour)
				.combinations(5)
				.exists(HandPredicates.STRAIGHT);
	}

	static boolean isFlush(final List<Card> cards) {
		return countCardsBy(cards, Card::getSuit)
				.values()
				.stream()
				.anyMatch(count -> count >= 5);
	}

	static boolean isThreeOfAKind(final List<Card> cards) {
		return countCardsBy(cards, Card::getHonour)
				.values()
				.contains(3L);
	}

	static boolean isTwoPair(final List<Card> cards) {
		return countCardsBy(cards, Card::getHonour)
				.values()
				.stream()
				.filter(count -> count == 2)
				.count() >= 2;
	}

	static boolean isPair(final List<Card> cards) {
		return countCardsBy(cards, Card::getHonour)
				.values()
				.contains(2L);
	}

	static <U> Map<U, Long> countCardsBy(List<Card> cards, Function<Card, ? extends U> mapper) {
		return cards
				.map(mapper)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
}
