package com.tp.holdem.logic.hands

import com.tp.holdem.model.Card
import com.tp.holdem.model.Hands
import io.vavr.collection.List
import io.vavr.control.Option
import io.vavr.kotlin.option

private val CARD_COMPARATOR = compareBy(Card::value)
private val COMBO_NOT_FOUND = { IllegalStateException("Card combination could not be found") }

private fun List<Card>.precheck(): List<Card> {
    if (this.size() != 7)
        throw IllegalArgumentException("There should be 7 cards (2 player + 5 table)")
    return this.sorted(CARD_COMPARATOR)
}

fun List<Card>.bestCardsForHand(hand: Hands): List<Card> {
    return precheck().checkedBestCardsForHand(hand).getOrElseThrow(COMBO_NOT_FOUND)
}

private fun List<Card>.checkedBestCardsForHand(hand: Hands): Option<List<Card>> {
    return when (hand) {
        Hands.HIGH_CARD -> takeRight(5).option()
        Hands.PAIR -> getPairCards()
        Hands.TWO_PAIR -> getTwoPairCards()
        Hands.THREE_OF_A_KIND -> getThreeOfAKindCards()
        Hands.STRAIGHT -> getStraightCards()
        Hands.FLUSH -> getFlushCards()
        Hands.FOUR_OF_A_KIND -> getFourOfAKindCards()
        Hands.FULL_HOUSE -> getFullHouseCards()
        Hands.STRAIGHT_FLUSH -> getStraightFlushCards()
        Hands.ROYAL_FLUSH -> getRoyalFlushCards()
    }
}

private fun List<Card>.getFullHouseCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isFullHouse)
            .maxBy(HandComparators.highestFullHouse)
}

private fun List<Card>.getPairCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isPair)
            .maxBy(HandComparators.highestPair)
}

private fun List<Card>.getTwoPairCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isTwoPair)
            .maxBy(HandComparators.highestPairs)
}

private fun List<Card>.getThreeOfAKindCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isThreeOfAKind)
            .maxBy(HandComparators.highestThree)
}

private fun List<Card>.getStraightFlushCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isStraightFlush)
            .maxBy(HandComparators.highestStraightKicker)
}

private fun List<Card>.getStraightCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isStraight)
            .maxBy(HandComparators.highestStraightKicker)
}

private fun List<Card>.getRoyalFlushCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isRoyalFlush)
            .headOption()
}

private fun List<Card>.getFourOfAKindCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isFourOfAKind)
            .maxBy(HandComparators.highestFour)
}

private fun List<Card>.getFlushCards(): Option<List<Card>> {
    return combinations(5)
            .filter(List<Card>::isFlush)
            .maxBy(HandComparators.highestKicker)
}