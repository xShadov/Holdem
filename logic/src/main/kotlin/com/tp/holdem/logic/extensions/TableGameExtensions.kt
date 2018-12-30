package com.tp.holdem.logic.extensions

import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.model.Deck
import com.tp.holdem.model.Player
import com.tp.holdem.model.PlayerNumber
import com.tp.holdem.model.PokerTable
import io.vavr.collection.HashMap
import io.vavr.collection.List
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

fun PokerTable.dealCards(): PokerTable {
    log.debug("Dealing cards to players")
    return this.copy(
            allPlayers = deck.dealCards(2, allPlayers)
    )
}

fun PokerTable.roundOver(): PokerTable {
    val playersAfterRound = allPlayers.map<Player> { it.roundOver() }

    val updatedTable = this.copy(
            allPlayers = playersAfterRound
    )

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
            allPlayers = allPlayers.map { player -> player.prepareForNewGame(startingChips) }
    )
}

fun PokerTable.newRound(handCount: AtomicLong): PokerTable {
    log.debug("Preparing players for new round")
    val playersWithCleanBets = allPlayers.map { it.prepareForNewRound() }

    val smallBlindPlayer = playersWithCleanBets.get(((handCount.get() + 1) % playersWithCleanBets.size()).toInt())
    log.debug(String.format("Taking small blind from player: %d", smallBlindPlayer.number))
    val newSmallBlindPlayer = smallBlindPlayer.betSmallBlind(this)

    val dealerPlayer: Player
    if (allPlayers.size() == 2)
        dealerPlayer = newSmallBlindPlayer
    else
        dealerPlayer = playersWithCleanBets.get((handCount.get() % playersWithCleanBets.size()).toInt())

    val bigBlindPlayer: Player
    if (allPlayers.size() == 2)
        bigBlindPlayer = playersWithCleanBets.get((handCount.get() % playersWithCleanBets.size()).toInt())
    else
        bigBlindPlayer = playersWithCleanBets.get(((handCount.get() + 2) % playersWithCleanBets.size()).toInt())

    log.debug(String.format("Taking big blind from player: %d", bigBlindPlayer.number))
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

    val playerAfterAction: Player

    when (move) {
        Moves.FOLD -> playerAfterAction = actionPlayer.fold()
        Moves.ALLIN -> playerAfterAction = actionPlayer.allIn()
        Moves.BET -> playerAfterAction = actionPlayer.bet(betAmount)
        Moves.CHECK -> playerAfterAction = actionPlayer
        Moves.CALL -> playerAfterAction = actionPlayer.bet(highestBetThisPhase() - actionPlayer.betAmountThisPhase)
        Moves.RAISE -> playerAfterAction = actionPlayer.bet(highestBetThisPhase() - actionPlayer.betAmountThisPhase).bet(betAmount)
        else -> throw IllegalArgumentException(String.format("Unsupported action type: %s", move))
    }

    return this.copy(
            allPlayers = allPlayers.replace(actionPlayer, playerAfterAction.bettingTurnOver()),
            movesThisPhase = movesThisPhase.put(actionPlayer.number, move)
    )
}