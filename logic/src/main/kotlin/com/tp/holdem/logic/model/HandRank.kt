package com.tp.holdem.logic.model

import com.tp.holdem.logic.hands.HandRankComparator
import io.vavr.collection.List as VavrList

data class HandRank(
        internal val hand: Hands,
        internal val cardsThatMakeHand: VavrList<Card> = VavrList.empty()
) : Comparable<HandRank> {
    override fun compareTo(other: HandRank): Int {
        return HandRankComparator.compare(this, other)
    }

    override fun equals(other: Any?): Boolean {
        return other is HandRank && compareTo(other) == 0
    }

    companion object {
        @JvmStatic
        fun from(hand: Hands, cards: VavrList<Card>): HandRank {
            return HandRank(hand, cards)
        }

        @JvmStatic
        fun empty(hand: Hands): HandRank {
            return HandRank(hand, VavrList.empty())
        }
    }
}
