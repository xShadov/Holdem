package com.tp.holdem.logic.hands

import com.tp.holdem.model.Card
import com.tp.holdem.model.Hands
import io.vavr.collection.List
import java.util.*

internal object BestCardCombinationFinder {
    private val COMBO_NOT_FOUND = { IllegalStateException("Card combination could not be found") }

    private val CARD_COMPARATOR = Comparator.comparing{ card:Card -> card.value }

    fun findBestCardsForHand(cards: List<Card>, hand: Hands): List<Card> {
        var cards = cards
        if (cards.size() != 7)
            throw IllegalArgumentException("There should be 7 cards (2 player + 5 table)")

        cards = cards.sorted(CARD_COMPARATOR)

        if (hand === Hands.HIGH_CARD)
            return cards.takeRight(5)
        if (hand === Hands.PAIR)
            return getPairCards(cards)
        if (hand === Hands.TWO_PAIR)
            return getTwoPairCards(cards)
        if (hand === Hands.THREE_OF_A_KIND)
            return getThreeOfAKindCards(cards)
        if (hand === Hands.STRAIGHT)
            return getStraightCards(cards)
        if (hand === Hands.FLUSH)
            return getFlushCards(cards)
        if (hand === Hands.FOUR_OF_A_KIND)
            return getFourOfAKindCards(cards)
        if (hand === Hands.FULL_HOUSE)
            return getFullHouseCards(cards)
        if (hand === Hands.STRAIGHT_FLUSH)
            return getStraightFlushCards(cards)
        return if (hand === Hands.ROYAL_FLUSH) getRoyalFlushCards(cards) else List.empty()

    }

    private fun getFullHouseCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isFullHouse(it) }
                .maxBy(HandComparators.highestFullHouse())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getPairCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isPair(it) }
                .maxBy(HandComparators.highestPair())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getTwoPairCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isTwoPair(it) }
                .maxBy(HandComparators.highestPairs())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getThreeOfAKindCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isThreeOfAKind(it) }
                .maxBy(HandComparators.highestThree())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getStraightFlushCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isStraightFlush(it) }
                .maxBy(HandComparators.highestStraightKicker())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getStraightCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isStraight(it) }
                .maxBy(HandComparators.highestStraightKicker())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getRoyalFlushCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isRoyalFlush(it) }
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getFourOfAKindCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isFourOfAKind(it) }
                .maxBy(HandComparators.highestFour())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun getFlushCards(cards: List<Card>): List<Card> {
        return cards.combinations(5)
                .filter { HandFinder.isFlush(it) }
                .maxBy(HandComparators.highestKicker())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }
}
