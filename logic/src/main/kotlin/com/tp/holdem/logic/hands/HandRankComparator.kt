package com.tp.holdem.logic.hands

import com.tp.holdem.model.HandRank
import com.tp.holdem.model.Hands
import java.util.*

internal object HandRankComparator : Comparator<HandRank> {
    override fun compare(one: HandRank, two: HandRank): Int {
        val compareHands = compareValuesBy(one.hand, two.hand, Hands::value)

        if (compareHands != 0)
            return compareHands

        val comparator = when (one.hand) {
            Hands.HIGH_CARD -> HandComparators.highestKicker
            Hands.PAIR -> HandComparators.highestPair
            Hands.TWO_PAIR -> HandComparators.highestPairs
            Hands.THREE_OF_A_KIND -> HandComparators.highestThree
            Hands.STRAIGHT -> HandComparators.highestStraightKicker
            Hands.FLUSH -> HandComparators.highestKicker
            Hands.FULL_HOUSE -> HandComparators.highestFullHouse
            Hands.FOUR_OF_A_KIND -> HandComparators.highestFour
            Hands.STRAIGHT_FLUSH -> HandComparators.highestKicker
            Hands.ROYAL_FLUSH -> HandComparators.highestKicker
        }

        return compareValuesBy(one, two, comparator, HandRank::cardsThatMakeHand)
    }
}
