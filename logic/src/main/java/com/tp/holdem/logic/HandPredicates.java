package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Card;
import com.tp.holdem.model.common.Honour;
import io.vavr.collection.List;

import java.util.function.Predicate;
import java.util.stream.IntStream;

import static com.tp.holdem.model.common.Honour.*;
import static com.tp.holdem.model.common.Honour.ACE;

class HandPredicates {
	static final Predicate<List<Card>> REGULAR_STRAIGHT = values -> {
		final List<Honour> mapped = values.map(Card::getHonour);
		return IntStream.range(0, mapped.size() - 1)
				.allMatch(index -> mapped.get(index + 1).isRightAfter(mapped.get(index)));
	};
	static final Predicate<List<Card>> ACE_STRAIGHT = values -> values.map(Card::getHonour).containsAll(List.of(ACE, TWO, THREE, FOUR, FIVE));
	static final Predicate<List<Card>> STRAIGHT = ACE_STRAIGHT.or(REGULAR_STRAIGHT);
	static final Predicate<List<Card>> FLUSH = HandFinder::isFlush;
	static final Predicate<List<Card>> STRAIGHT_FLUSH = STRAIGHT.and(FLUSH);
	static final Predicate<List<Card>> ROYAL_FLUSH = REGULAR_STRAIGHT.and(FLUSH).and(cards -> cards.last().getHonour() == ACE);
}
