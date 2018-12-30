package com.tp.holdem.logic.hands

import com.tp.holdem.model.Card
import com.tp.holdem.model.Hands
import io.vavr.collection.List
import java.util.*
import java.util.function.Function

internal object HandFinder {
    private val CARD_COMPARATOR = Comparator.comparing { card: Card -> card.value }

    fun findHand(input: List<Card>): Hands {
        var cards = input
        if (cards.size() != 7)
            throw IllegalArgumentException("There should be 7 cards (2 player + 5 table)")

        cards = cards.sorted(CARD_COMPARATOR)

        if (isRoyalFlush(cards))
            return Hands.ROYAL_FLUSH
        if (isStraightFlush(cards))
            return Hands.STRAIGHT_FLUSH
        if (isFourOfAKind(cards))
            return Hands.FOUR_OF_A_KIND
        if (isFullHouse(cards))
            return Hands.FULL_HOUSE
        if (isFlush(cards))
            return Hands.FLUSH
        if (isStraight(cards))
            return Hands.STRAIGHT
        if (isThreeOfAKind(cards))
            return Hands.THREE_OF_A_KIND
        if (isTwoPair(cards))
            return Hands.TWO_PAIR
        return if (isPair(cards)) Hands.PAIR else Hands.HIGH_CARD
    }

    fun isRoyalFlush(cards: List<Card>): Boolean {
        return cards
                .combinations(5)
                .exists(HandPredicates.ROYAL_FLUSH)
    }

    fun isStraightFlush(cards: List<Card>): Boolean {
        return cards
                .combinations(5)
                .exists(HandPredicates.STRAIGHT_FLUSH)
    }

    fun isFourOfAKind(cards: List<Card>): Boolean {
        return countCardsBy(cards, Function { card: Card -> card.honour })
                .values
                .contains(4)
    }

    fun isFullHouse(cards: List<Card>): Boolean {
        val honourCounts = List.ofAll(countCardsBy(cards, Function { card: Card -> card.honour }).values)
        return honourCounts.containsAll(Arrays.asList(2, 3)) || honourCounts.filter { count -> count == 3 }.size() == 2
    }

    fun isStraight(cards: List<Card>): Boolean {
        return cards
                .distinctBy { card: Card -> card.honour }
                .combinations(5)
                .exists(HandPredicates.STRAIGHT)
    }

    fun isFlush(cards: List<Card>): Boolean {
        return countCardsBy(cards, Function { card: Card -> card.suit })
                .values
                .stream()
                .anyMatch { count -> count >= 5 }
    }

    fun isThreeOfAKind(cards: List<Card>): Boolean {
        return countCardsBy(cards, Function { card: Card -> card.honour })
                .values
                .contains(3)
    }

    fun isTwoPair(cards: List<Card>): Boolean {
        return countCardsBy(cards, Function { card: Card -> card.honour })
                .values
                .stream()
                .filter { count -> count == 2 }
                .count() >= 2
    }

    fun isPair(cards: List<Card>): Boolean {
        return countCardsBy(cards, Function { card: Card -> card.honour })
                .values
                .contains(2)
    }

    fun <U> countCardsBy(cards: List<Card>, mapper: Function<Card, out U>): Map<U, Int> {
        return cards
                .map(mapper)
                .groupingBy { it }
                .eachCount()
    }
}
