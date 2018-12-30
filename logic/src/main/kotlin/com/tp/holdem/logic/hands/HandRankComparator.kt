package com.tp.holdem.logic.hands

import com.tp.holdem.model.HandRank
import com.tp.holdem.model.Hands
import io.vavr.collection.List
import java.util.*

internal class HandRankComparator private constructor() : Comparator<HandRank> {
    companion object {
        val INSTANCE = HandRankComparator()
    }

    override fun compare(one: HandRank, two: HandRank): Int {
        val compareHands = Comparator.comparing { hand: Hands -> hand.value() }.compare(one.hand, two.hand)

        if (compareHands != 0)
            return compareHands

        val firstHand = one.hand
        val firstCards = List.ofAll(one.cardsThatMakeHand)
        val secondHand = two.hand
        val secondCards = List.ofAll(two.cardsThatMakeHand)

        if (firstHand === Hands.HIGH_CARD)
            return HandComparators.highestKicker().compare(firstCards, secondCards)
        if (firstHand === Hands.PAIR)
            return HandComparators.highestPair().compare(firstCards, secondCards)
        if (firstHand === Hands.TWO_PAIR)
            return HandComparators.highestPairs().compare(firstCards, secondCards)
        if (firstHand === Hands.THREE_OF_A_KIND)
            return HandComparators.highestThree().compare(firstCards, secondCards)
        if (firstHand === Hands.STRAIGHT)
            return HandComparators.highestStraightKicker().compare(firstCards, secondCards)
        if (firstHand === Hands.FLUSH)
            return HandComparators.highestKicker().compare(firstCards, secondCards)
        if (firstHand === Hands.FULL_HOUSE)
            return HandComparators.highestFullHouse().compare(firstCards, secondCards)
        if (firstHand === Hands.FOUR_OF_A_KIND)
            return HandComparators.highestFour().compare(firstCards, secondCards)
        if (firstHand === Hands.STRAIGHT_FLUSH)
            return HandComparators.highestKicker().compare(firstCards, secondCards)
        return 0

    }
}
