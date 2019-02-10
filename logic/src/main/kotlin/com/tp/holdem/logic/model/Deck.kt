package com.tp.holdem.logic.model


import com.tp.holdem.common.model.Honour
import com.tp.holdem.common.model.Suit
import io.vavr.collection.List as VavrList

data class Deck(
        internal val cards: VavrList<Card> = VavrList.of(*Honour.values())
                .flatMap { honour ->
                    VavrList.of(
                            Card.from(Suit.HEART, honour),
                            Card.from(Suit.CLUB, honour),
                            Card.from(Suit.DIAMOND, honour),
                            Card.from(Suit.SPADE, honour)
                    )
                }
                .shuffle()
) {
    companion object {
        @JvmStatic
        fun brandNew(): Deck {
            return Deck()
        }
    }
}
