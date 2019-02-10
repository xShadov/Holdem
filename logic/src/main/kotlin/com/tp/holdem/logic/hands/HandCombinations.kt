package com.tp.holdem.logic.hands

import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.Hands
import io.vavr.collection.List as VavrList
import io.vavr.control.Option
import io.vavr.kotlin.option

private val CARD_COMPARATOR = compareBy(Card::value)
private val COMBO_NOT_FOUND = { IllegalStateException("Card combination could not be found") }

private fun VavrList<Card>.precheck(): VavrList<Card> {
    if (this.size() != 7)
        throw IllegalArgumentException("There should be 7 cards (2 player + 5 table)")
    return this.sorted(CARD_COMPARATOR)
}

fun VavrList<Card>.bestCardsForHand(hand: Hands): VavrList<Card> {
    return precheck().checkedBestCardsForHand(hand).getOrElseThrow(COMBO_NOT_FOUND)
}

private fun VavrList<Card>.checkedBestCardsForHand(hand: Hands): Option<VavrList<Card>> {
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

private fun VavrList<Card>.getFullHouseCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isFullHouse)
            .maxBy(HandComparators.highestFullHouse)
}

private fun VavrList<Card>.getPairCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isPair)
            .maxBy(HandComparators.highestPair)
}

private fun VavrList<Card>.getTwoPairCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isTwoPair)
            .maxBy(HandComparators.highestPairs)
}

private fun VavrList<Card>.getThreeOfAKindCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isThreeOfAKind)
            .maxBy(HandComparators.highestThree)
}

private fun VavrList<Card>.getStraightFlushCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isStraightFlush)
            .maxBy(HandComparators.highestStraightKicker)
}

private fun VavrList<Card>.getStraightCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isStraight)
            .maxBy(HandComparators.highestStraightKicker)
}

private fun VavrList<Card>.getRoyalFlushCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isRoyalFlush)
            .headOption()
}

private fun VavrList<Card>.getFourOfAKindCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isFourOfAKind)
            .maxBy(HandComparators.highestFour)
}

private fun VavrList<Card>.getFlushCards(): Option<VavrList<Card>> {
    return combinations(5)
            .filter(VavrList<Card>::isFlush)
            .maxBy(HandComparators.highestKicker)
}