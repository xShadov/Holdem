package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.players.*
import com.tp.holdem.logic.utils.drawCards
import com.tp.holdem.logic.model.PhaseStatus
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import io.vavr.collection.HashMap
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2

fun PokerTable.goToNextPlayingPhase(): PokerTable {
    return when (phase) {
        Phase.START -> goToPreFlopPhase()
        Phase.PRE_FLOP -> goToNextPhase(3)
        Phase.FLOP, Phase.TURN -> goToNextPhase(1)
        else -> throw IllegalStateException("There is no next playing phase")
    }
}

private fun PokerTable.goToPreFlopPhase(): PokerTable {
    val dealerPlayer = getDealer().assertFound()
    val bettingPlayer = dealerPlayer.firstBetInRound(this)

    return this.copy(
            phase = phase.nextPhase(),
            allPlayers = allPlayers.replace(dealerPlayer, bettingPlayer),
            bettingPlayerNumber = bettingPlayer.number
    )
}

private fun PokerTable.goToNextPhase(cards: Int): PokerTable {
    val preparedPlayers = allPlayers.map { it.prepareForNewPhase() }

    val (deck, cards) = deck.drawCards(cards)

    val updatedTable = this.copy(
            phase = phase.nextPhase(),
            allPlayers = preparedPlayers,
            deck = deck,
            cardsOnTable = cardsOnTable.appendAll(cards),
            latestMoves = latestMoves.filterValues { it.goingToNextPhase() }
    )

    return updatedTable.nextPlayerToBetAfter(bigBlindPlayerNumber)
}

fun PokerTable.nextPlayerToBet(): PokerTable {
    if (!bettingPlayerNumber.exists())
        throw IllegalStateException("There is currenly no betting player")
    return nextPlayerToBetAfter(bettingPlayerNumber)
}

private fun PokerTable.nextPlayerToBetAfter(playerNumber: PlayerNumber): PokerTable {
    log.debug("Finding next to bet after: $playerNumber")

    val bettingPlayerIndex = allPlayers.indexOfNumber(playerNumber)
    var newBettingPlayer: Player
    var count = 1
    do {
        newBettingPlayer = allPlayers.get((bettingPlayerIndex + count++) % allPlayers.size())
    } while (newBettingPlayer.notPlaying())

    if (newBettingPlayer.number == playerNumber)
        throw IllegalStateException("New player to bet is the same as previous one")

    return this.copy(
            allPlayers = allPlayers.replace(newBettingPlayer, newBettingPlayer.betInPhase(this)),
            bettingPlayerNumber = newBettingPlayer.number
    )
}

fun PokerTable.phaseStatus(): PhaseStatus {
    if (phase.isPlaying.not())
        throw IllegalStateException("Table is in not-playing phase")

    val inGame = allPlayers.inGame()
    val notFolded = inGame.notFolded()
    val notAllIn = inGame.notAllIn()
    val allPlayersMoved = allPlayersMoved()
    val allBetsEqual = allBetsEqual()
    return when {
        inGame.size() < 2 -> throw IllegalStateException("Not enough players on table")
        notFolded.size() == 0 -> throw IllegalStateException("Every player is folded")
        notFolded.size() == 1 -> PhaseStatus.ROUND_OVER
        notAllIn.size() <= 1 && allPlayersMoved -> PhaseStatus.READY_FOR_SHOWDOWN
        allPlayersMoved && allBetsEqual && lastPlayingPhase() -> PhaseStatus.ROUND_OVER
        allPlayersMoved && allBetsEqual -> PhaseStatus.READY_FOR_NEXT
        else -> PhaseStatus.KEEP_GOING
    }
}

fun PokerTable.startShowdown(): PokerTable {
    return this.copy(
            showdown = true
    )
}

fun PokerTable.playerMove(playerNumber: PlayerNumber, move: Moves, betAmount: Int = 0): PokerTable {
    val actionPlayer = allPlayers.byNumber(playerNumber)

    if(bettingPlayerNumber != actionPlayer.number)
        throw IllegalStateException("Different player has betting turn")

    if(actionPlayer.notPlaying())
        throw IllegalStateException("Player is currently not playing")

    if(actionPlayer.possibleMoves.contains(move).not())
        throw IllegalStateException("Player cannot perform this move: $move")

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
            latestMoves = latestMoves.put(actionPlayer.number, move)
    )
}