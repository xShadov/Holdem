package com.tp.holdem.logic.hands

import com.tp.holdem.model.Card
import io.vavr.collection.List

import java.util.function.Predicate
import java.util.stream.IntStream

import com.tp.holdem.common.model.Honour.*
import com.tp.holdem.common.model.Honour.ACE

internal object HandPredicates {
    val REGULAR_STRAIGHT = Predicate { values: List<Card> ->
        val mapped = values.map { it.honour }
        IntStream.range(0, mapped.size() - 1)
                .allMatch { index -> mapped.get(index + 1).isRightAfter(mapped.get(index)) }
    }
    val ACE_STRAIGHT = Predicate { values: List<Card> -> values.map { it.honour }.containsAll(List.of(ACE, TWO, THREE, FOUR, FIVE)) }
    val STRAIGHT = ACE_STRAIGHT.or(REGULAR_STRAIGHT)
    val FLUSH = Predicate { values: List<Card> -> HandFinder.isFlush(values) }
    val STRAIGHT_FLUSH = STRAIGHT.and(FLUSH)
    val ROYAL_FLUSH = REGULAR_STRAIGHT.and(FLUSH).and { cards -> cards.last().honour == ACE }
}
