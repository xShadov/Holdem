package com.tp.holdem.logic.model

import com.tp.holdem.logic.hands.HandRankComparator
import io.vavr.collection.List

data class HandRank(
        internal val hand: Hands,
        internal val cardsThatMakeHand: List<Card> = List.empty()
) : Comparable<HandRank> {
    override fun compareTo(other: HandRank): Int {
        return HandRankComparator.compare(this, other)
    }

    companion object {
        @JvmStatic
        fun from(hand: Hands, cards: List<Card>): HandRank {
            return HandRank(hand, cards)
        }

        @JvmStatic
        fun empty(hand: Hands): HandRank {
            return HandRank(hand, List.empty())
        }
    }
}
