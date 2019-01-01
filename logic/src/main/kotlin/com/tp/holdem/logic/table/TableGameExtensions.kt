package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.utils.dealCards
import com.tp.holdem.logic.players.*
import com.tp.holdem.model.Deck
import com.tp.holdem.model.Player
import com.tp.holdem.model.PlayerNumber
import com.tp.holdem.model.PokerTable
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2
import java.util.concurrent.atomic.AtomicLong

fun PokerTable.gameOver(): PokerTable {
    return this.copy(
            gameOver = true,
            phase = Phase.OVER,
            allPlayers = allPlayers.map { it.gameOver() }
    )
}

fun PokerTable.showdownMode(): PokerTable {
    return this.copy(
            showdown = true
    )
}

fun PokerTable.addPlayer(player: Player): PokerTable {
    return this.copy(
            allPlayers = allPlayers.append(player)
    )
}

fun PokerTable.playerLeft(playerNumber: PlayerNumber): PokerTable {
    val foundPlayer = allPlayers.byNumber(playerNumber)

    val modifiedPlayers = allPlayers
            .replace(foundPlayer, foundPlayer.copy(inGame = false))

    return this.copy(
            allPlayers = modifiedPlayers
    )
}

fun PokerTable.dealCards(): PokerTable {
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
    val possibleWinners = playersAfterRound.winner(updatedTable)
    val winner = possibleWinners.left

    val prizedWinner = winner.copy(
            chipsAmount = winner.chipsAmount + updatedTable.potAmount()
    )

    return this.copy(
            winnerPlayer = PlayerNumber.of(winner.number),
            allPlayers = updatedTable.allPlayers.replace(winner, prizedWinner),
            phase = Phase.OVER
    )
}

fun PokerTable.preparePlayersForNewGame(startingChips: Int): PokerTable {
    return this.copy(
            allPlayers = allPlayers.map { it.prepareForNewGame(startingChips) }
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
            movesThisPhase = HashMap.empty(),
            allPlayers = playersWithCleanBets.replace(smallBlindPlayer, newSmallBlindPlayer).replace(bigBlindPlayer, newBigBlindPlayer),
            winnerPlayer = PlayerNumber.empty(),
            bettingPlayer = PlayerNumber.empty(),
            dealer = PlayerNumber.of(dealerPlayer.number),
            bigBlind = PlayerNumber.of(newBigBlindPlayer.number),
            smallBlind = PlayerNumber.of(newSmallBlindPlayer.number)
    )

    return updatedTable.dealCards()
}

fun PokerTable.playerMove(playerNumber: Int, move: Moves, betAmount: Int): PokerTable {
    val actionPlayer = allPlayers.byNumber(playerNumber)

    val playerAfterAction = when (move) {
        Moves.FOLD -> actionPlayer.fold()
        Moves.ALLIN -> actionPlayer.allIn()
        Moves.BET -> actionPlayer.bet(betAmount)
        Moves.CHECK -> actionPlayer
        Moves.CALL -> actionPlayer.bet(highestBetThisPhase() - actionPlayer.betAmountThisPhase)
        Moves.RAISE -> actionPlayer.bet(highestBetThisPhase() - actionPlayer.betAmountThisPhase).bet(betAmount)
    }

    return this.copy(
            allPlayers = allPlayers.replace(actionPlayer, playerAfterAction.bettingTurnOver()),
            movesThisPhase = movesThisPhase.put(actionPlayer.number, move)
    )
}