package com.tp.holdem.logic.utils

import com.tp.holdem.common.chunks
import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.Deck
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.players.notPlaying
import com.tp.holdem.logic.players.playing
import com.tp.holdem.logic.players.withCards
import io.vavr.Tuple
import io.vavr.Tuple2
import io.vavr.collection.List
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2

fun Deck.dealCards(numberOfCards: Int, players: List<Player>): Tuple2<Deck, List<Player>> {
    if (players.isEmpty)
        throw IllegalArgumentException("Player list is empty")

    if (numberOfCards <= 0)
        throw IllegalArgumentException("You cant deal <= 0 cards")

    if (cards.size() < players.size() * numberOfCards)
        throw IllegalStateException("Not enough cards in deck")

    val playingPlayers = players.playing()

    val (deck, cards) = drawCards(playingPlayers.size() * numberOfCards)

    val playersWithCards = playingPlayers.zip(cards.chunks(numberOfCards))
            .map { (player, hisCards) -> player.withCards(hisCards) }

    return Tuple.of(deck, playersWithCards.appendAll(players.notPlaying()))
}

fun Deck.drawCards(number: Int): Tuple2<Deck, List<Card>> {
    if (number <= 0)
        throw IllegalArgumentException("You can't draw <= 0 cards")

    if (this.cards.size() < number)
        throw IllegalStateException("Deck does not have enough cards")

    val drawn = cards.take(number)
    return Tuple.of(Deck(cards.removeAll(drawn)), drawn)
}

fun Deck.drawCard(): Tuple2<Deck, Card> {
    return drawCards(1).map2 { it.head() }
}

fun Deck.isFull() : Boolean {
    return cards.size() == 52
}