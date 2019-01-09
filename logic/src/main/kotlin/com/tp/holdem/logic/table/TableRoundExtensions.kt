package com.tp.holdem.logic.table

import com.tp.holdem.common.cycleGet
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.hands.HandRankComparator
import com.tp.holdem.logic.model.Deck
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.*
import com.tp.holdem.logic.utils.dealCards
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.control.Either
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2

fun PokerTable.newRound(handCount: Int): PokerTable {
    if (allPlayers.playing().notBroke().size() < 2)
        throw IllegalStateException("Table needs to have minimum 2 playing players with chips")

    log.debug("Preparing players for new round")
    val playersWithCleanBets = allPlayers.map { it.prepareForNewRound() }

    val smallBlindPlayer = playersWithCleanBets.cycleGet(handCount + 1)
    log.debug("Taking small blind from player: ${smallBlindPlayer.number}")
    val newSmallBlindPlayer = smallBlindPlayer.betSmallBlind(this)

    fun PokerTable.findDealerPlayer(): Player {
        return when {
            allPlayers.size() == 2 -> newSmallBlindPlayer
            else -> playersWithCleanBets.cycleGet(handCount)
        }
    }

    fun PokerTable.findBigBlindPlayer(): Player {
        return when {
            allPlayers.size() == 2 -> playersWithCleanBets.cycleGet(handCount)
            else -> playersWithCleanBets.cycleGet(handCount + 2)
        }
    }

    val dealerPlayer = findDealerPlayer()
    val bigBlindPlayer = findBigBlindPlayer()

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
            dealerPlayerNumber = dealerPlayer.number,
            bigBlindPlayerNumber = newBigBlindPlayer.number,
            smallBlindPlayerNumber = newSmallBlindPlayer.number
    )

    return updatedTable.dealCards()
}

private fun PokerTable.dealCards(): PokerTable {
    log.debug("Dealing cards to players")

    val (deck, players) = deck.dealCards(2, allPlayers)
    return this.copy(
            deck = deck,
            allPlayers = players
    )
}

fun PokerTable.roundOver(): PokerTable {
    val playersAfterRound = allPlayers.map { it.roundOver() }

    val updatedTable = this.copy(
            allPlayers = playersAfterRound
    )

    //TODO handle multiple winners - pot split
    val possibleWinners = updatedTable.findWinner()
    val winner = possibleWinners.left

    val prizedWinner = winner.copy(
            chipsAmount = winner.chipsAmount + updatedTable.potAmount()
    )

    return this.copy(
            winnerPlayerNumber = winner.number,
            allPlayers = updatedTable.allPlayers.replace(winner, prizedWinner),
            phase = Phase.OVER
    )
}

private fun PokerTable.findWinner(): Either<Player, List<Player>> {
    val notFoldedPlayers = allPlayers.notFolded()

    if (notFoldedPlayers.size() == 1) {
        return Either.left<Player, List<Player>>(notFoldedPlayers.single())
                .also { log.debug("Everyone folded except one player, he's the winner") }
    }

    val hands = allPlayers.playing()
            .toMap({ it }, { it.handRank(this) })
            .also { log.debug("Players to hands map: $it") }

    val maxHandRank = hands.values().max().getOrElseThrow { IllegalStateException("Could not find max handRank") }

    val playersWithMaxHandRank = hands
            .filterValues { it == maxHandRank }
            .map { tuple -> tuple._1 }
            .toList()

    return when (playersWithMaxHandRank.size()) {
        1 -> Either.left(playersWithMaxHandRank.head())
        else -> Either.right(playersWithMaxHandRank)
    }
}