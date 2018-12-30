package com.tp.holdem.model


import com.tp.holdem.common.model.Honour
import com.tp.holdem.common.model.Suit
import io.vavr.collection.List

class Deck {
    companion object {
        @JvmStatic
        fun brandNew(): Deck {
            return Deck()
        }
    }

    private var cards = List.of(*Honour.values())
            .flatMap { honour ->
                List.of(
                        Card.from(Suit.HEART, honour),
                        Card.from(Suit.CLUB, honour),
                        Card.from(Suit.DIAMOND, honour),
                        Card.from(Suit.SPADE, honour)
                )
            }
            .shuffle()

    fun dealCards(numberOfCards: Int, players: List<Player>): List<Player> {
        return players
                .filter { it.playing() }
                .map { player -> player.withCards(List.fill(numberOfCards) { this.drawCard() }) }
                .appendAll(players.filter { it.notPlaying() })
    }

    fun drawCards(number: Int): List<Card> {
        val drawn = cards.take(number)
        cards = cards.removeAll(drawn)
        return drawn
    }

    fun drawCard(): Card {
        return cards.headOption()
                .peek { cards = cards.removeAt(0) }
                .getOrElseThrow { IllegalStateException("Card cannot be drawn - deck is empty") }
    }
}
