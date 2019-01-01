package com.tp.holdem.logic.hands

import com.tp.holdem.common.model.Honour
import com.tp.holdem.model.Card
import com.tp.holdem.model.HandRank
import com.tp.holdem.model.Hands
import io.vavr.collection.List
import java.util.stream.IntStream

private val CARD_COMPARATOR = compareBy(Card::value)

private fun List<Card>.precheck(): List<Card> {
    if (this.size() != 7)
        throw IllegalArgumentException("There should be 7 cards (2 player + 5 table)")
    return this.sorted(CARD_COMPARATOR)
}

fun List<Card>.findHandRank(): HandRank {
    return precheck().checkedFindHandRank()
}

fun List<Card>.findHand(): Hands {
    return precheck().checkedFindHand()
}

private fun List<Card>.checkedFindHandRank(): HandRank {
    val hand = checkedFindHand()
    return HandRank(hand, bestCardsForHand(hand))
}

private fun List<Card>.checkedFindHand(): Hands {
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

fun List<Card>.isStraight(): Boolean {
    return distinctBy(Card::honour)
            .combinations(5)
            .exists { it.isAceStraight().or(it.isRegularStraight()) }
}

fun List<Card>.isRegularStraight(): Boolean {
    return combinations(5)
            .map { cards -> cards.map(Card::honour) }
            .exists { honours ->
                IntStream.range(0, honours.size() - 1)
                        .allMatch { honours[it + 1].isRightAfter(honours[it]) }
            }
}

fun List<Card>.isAceStraight(): Boolean {
    return map(Card::honour)
            .containsAll(List.of(Honour.ACE, Honour.TWO, Honour.THREE, Honour.FOUR, Honour.FIVE))
}

fun List<Card>.isRoyalFlush(): Boolean {
    return combinations(5)
            .exists { it.isRegularStraight().and(it.isFlush()).and(it.last().honour == Honour.ACE) }
}

fun List<Card>.isStraightFlush(): Boolean {
    return combinations(5)
            .exists { it.isStraight().and(it.isFlush()) }
}

fun List<Card>.isFourOfAKind(): Boolean {
    return countBy(Card::honour)
            .values
            .contains(4)
}

fun List<Card>.isFullHouse(): Boolean {
    val honourCounts = countBy(Card::honour).values
    return honourCounts.containsAll(listOf(2, 3)) || honourCounts.filter { count -> count == 3 }.size == 2
}

fun List<Card>.isFlush(): Boolean {
    return countBy(Card::suit)
            .values
            .stream()
            .anyMatch { count -> count >= 5 }
}

fun List<Card>.isThreeOfAKind(): Boolean {
    return countBy(Card::honour)
            .values
            .contains(3)
}

fun List<Card>.isTwoPair(): Boolean {
    return countBy(Card::honour)
            .values
            .stream()
            .filter { count -> count == 2 }
            .count() >= 2
}

fun List<Card>.isPair(): Boolean {
    return countBy(Card::honour)
            .values
            .contains(2)
}

private fun <U> List<Card>.countBy(mapper: (Card) -> U): Map<U, Int> {
    return map(mapper)
            .groupingBy { it }
            .eachCount()
}