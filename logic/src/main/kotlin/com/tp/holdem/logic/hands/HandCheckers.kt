package com.tp.holdem.logic.hands

import com.tp.holdem.common.model.Honour
import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.HandRank
import com.tp.holdem.logic.model.Hands
import io.vavr.collection.List as VavrList
import java.util.stream.IntStream

private val CARD_COMPARATOR = compareBy(Card::value)

private fun VavrList<Card>.precheck(): VavrList<Card> {
    if (this.size() != 7)
        throw IllegalArgumentException("There should be 7 cards (2 player + 5 table)")
    return this.sorted(CARD_COMPARATOR)
}

fun VavrList<Card>.findHandRank(): HandRank {
    return precheck().checkedFindHandRank()
}

fun VavrList<Card>.findHand(): Hands {
    return precheck().checkedFindHand()
}

private fun VavrList<Card>.checkedFindHandRank(): HandRank {
    val hand = checkedFindHand()
    return HandRank(hand, bestCardsForHand(hand))
}

private fun VavrList<Card>.checkedFindHand(): Hands {
    return when {
        isRoyalFlush() -> Hands.ROYAL_FLUSH
        isStraightFlush() -> Hands.STRAIGHT_FLUSH
        isFourOfAKind() -> Hands.FOUR_OF_A_KIND
        isFullHouse() -> Hands.FULL_HOUSE
        isFlush() -> Hands.FLUSH
        isStraight() -> Hands.STRAIGHT
        isThreeOfAKind() -> Hands.THREE_OF_A_KIND
        isTwoPair() -> Hands.TWO_PAIR
        isPair() -> Hands.PAIR
        else -> Hands.HIGH_CARD
    }
}

fun VavrList<Card>.isStraight(): Boolean {
    return distinctBy(Card::honour)
            .combinations(5)
            .exists { it.isAceStraight().or(it.isRegularStraight()) }
}

fun VavrList<Card>.isRegularStraight(): Boolean {
    return combinations(5)
            .map { cards -> cards.map(Card::honour) }
            .exists { honours ->
                IntStream.range(0, honours.size() - 1)
                        .allMatch { honours[it + 1].isRightAfter(honours[it]) }
            }
}

fun VavrList<Card>.isAceStraight(): Boolean {
    return map(Card::honour)
            .containsAll(VavrList.of(Honour.ACE, Honour.TWO, Honour.THREE, Honour.FOUR, Honour.FIVE))
}

fun VavrList<Card>.isRoyalFlush(): Boolean {
    return combinations(5)
            .exists { it.isRegularStraight().and(it.isFlush()).and(it.last().honour == Honour.ACE) }
}

fun VavrList<Card>.isStraightFlush(): Boolean {
    return combinations(5)
            .exists { it.isStraight().and(it.isFlush()) }
}

fun VavrList<Card>.isFourOfAKind(): Boolean {
    return countBy(Card::honour)
            .values
            .contains(4)
}

fun VavrList<Card>.isFullHouse(): Boolean {
    val honourCounts = countBy(Card::honour).values
    return honourCounts.containsAll(listOf(2, 3)) || honourCounts.filter { count -> count == 3 }.size == 2
}

fun VavrList<Card>.isFlush(): Boolean {
    return countBy(Card::suit)
            .values
            .stream()
            .anyMatch { count -> count >= 5 }
}

fun VavrList<Card>.isThreeOfAKind(): Boolean {
    return countBy(Card::honour)
            .values
            .contains(3)
}

fun VavrList<Card>.isTwoPair(): Boolean {
    return countBy(Card::honour)
            .values
            .stream()
            .filter { count -> count == 2 }
            .count() >= 2
}

fun VavrList<Card>.isPair(): Boolean {
    return countBy(Card::honour)
            .values
            .contains(2)
}

private fun <U> VavrList<Card>.countBy(mapper: (Card) -> U): Map<U, Int> {
    return map(mapper)
            .groupingBy { it }
            .eachCount()
}