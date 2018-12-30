package com.tp.holdem.model

import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.collection.Map

data class PokerTable(
        val smallBlindAmount: Int = 0,
        val bigBlindAmount: Int = 0,
        val showdown: Boolean = false,
        val gameOver: Boolean = false,
        val deck: Deck = Deck.brandNew(),
        val phase: Phase = Phase.START,
        val bettingPlayer: PlayerNumber = PlayerNumber.empty(),
        val winnerPlayer: PlayerNumber = PlayerNumber.empty(),
        val dealer: PlayerNumber = PlayerNumber.empty(),
        val bigBlind: PlayerNumber = PlayerNumber.empty(),
        val smallBlind: PlayerNumber = PlayerNumber.empty(),
        val allPlayers: List<Player> = List.empty(),
        val cardsOnTable: List<Card> = List.empty(),
        val movesThisPhase: Map<Int, Moves> = HashMap.empty()
) {
    val log by lazyLogger()

    companion object {
        @JvmStatic
        fun withBlinds(bigBlindAmount: Int, smallBlindAmount: Int): PokerTable {
            return PokerTable(bigBlindAmount = bigBlindAmount, smallBlindAmount = smallBlindAmount)
        }
    }
}