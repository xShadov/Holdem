package com.tp.holdem.logic.model

import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.collection.Map

data class PokerTable(
        internal val smallBlindAmount: Int = 0,
        internal val bigBlindAmount: Int = 0,
        internal val showdown: Boolean = false,
        internal val gameOver: Boolean = false,
        internal val deck: Deck = Deck.brandNew(),
        internal val phase: Phase = Phase.START,
        internal val bettingPlayerNumber: PlayerNumber = PlayerNumber.empty(),
        internal val winnerPlayerNumber: PlayerNumber = PlayerNumber.empty(),
        internal val dealerNumber: PlayerNumber = PlayerNumber.empty(),
        internal val bigBlindPlayerNumber: PlayerNumber = PlayerNumber.empty(),
        internal val smallBlindPlayerNumber: PlayerNumber = PlayerNumber.empty(),
        internal val allPlayers: List<Player> = List.empty(),
        internal val cardsOnTable: List<Card> = List.empty(),
        internal val latestMoves: Map<PlayerNumber, Moves> = HashMap.empty()
) {
    val log by lazyLogger()

    companion object {
        @JvmStatic
        fun withBlinds(bigBlindAmount: Int, smallBlindAmount: Int): PokerTable {
            return PokerTable(
                    bigBlindAmount = bigBlindAmount,
                    smallBlindAmount = smallBlindAmount
            )
        }
    }
}