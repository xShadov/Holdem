package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.players.*
import com.tp.holdem.logic.utils.dealCards
import com.tp.holdem.logic.model.Deck
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2
import java.util.concurrent.atomic.AtomicLong

fun PokerTable.dealCards(): PokerTable {
    log.debug("Dealing cards to players")

    val (deck, players) = deck.dealCards(2, allPlayers)
    return this.copy(
            deck = deck,
            allPlayers = players
    )
}

fun PokerTable.newRound(handCount: AtomicLong): PokerTable {
    log.debug("Preparing players for new round")
    val playersWithCleanBets = allPlayers.map { it.prepareForNewRound() }

    val smallBlindPlayer = playersWithCleanBets.get(((handCount.get() + 1) % playersWithCleanBets.size()).toInt())
    log.debug("Taking small blind from player: ${smallBlindPlayer.number}")
    val newSmallBlindPlayer = smallBlindPlayer.betSmallBlind(this)

    val dealerPlayer =
            if (allPlayers.size() == 2)
                newSmallBlindPlayer
            else
                playersWithCleanBets.get((handCount.get() % playersWithCleanBets.size()).toInt())

    val bigBlindPlayer =
            if (allPlayers.size() == 2)
                playersWithCleanBets.get((handCount.get() % playersWithCleanBets.size()).toInt())
            else
                playersWithCleanBets.get(((handCount.get() + 2) % playersWithCleanBets.size()).toInt())

    log.debug("Taking big blind from player: ${bigBlindPlayer.number}")
    val newBigBlindPlayer = bigBlindPlayer.betBigBlind(this)

    val updatedTable = this.copy(
            deck = Deck.brandNew(),
            cardsOnTable = List.empty(),
            phase = Phase.START,
            showdown = false,
            latestMoves = HashMap.empty(),
            allPlayers = playersWithCleanBets.replace(smallBlindPlayer, newSmallBlindPlayer).replace(bigBlindPlayer, newBigBlindPlayer),
            winnerPlayerNumber = PlayerNumber.empty(),
            bettingPlayerNumber = PlayerNumber.empty(),
            dealerNumber = PlayerNumber.of(dealerPlayer.number),
            bigBlindPlayerNumber = PlayerNumber.of(newBigBlindPlayer.number),
            smallBlindPlayerNumber = PlayerNumber.of(newSmallBlindPlayer.number)
    )

    return updatedTable.dealCards()
}

fun PokerTable.roundOver(): PokerTable {
    val playersAfterRound = allPlayers.map { it.roundOver() }

    val updatedTable = this.copy(
            allPlayers = playersAfterRound
    )

    //TODO handle multiple winners - pot split
    val possibleWinners = playersAfterRound.winner(updatedTable)
    val winner = possibleWinners.left

    val prizedWinner = winner.copy(
            chipsAmount = winner.chipsAmount + updatedTable.potAmount()
    )

    return this.copy(
            winnerPlayerNumber = PlayerNumber.of(winner.number),
            allPlayers = updatedTable.allPlayers.replace(winner, prizedWinner),
            phase = Phase.OVER
    )
}