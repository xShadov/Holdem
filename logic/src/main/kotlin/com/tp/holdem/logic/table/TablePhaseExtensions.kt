package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.players.*
import com.tp.holdem.logic.utils.drawCards
import com.tp.holdem.model.PhaseStatus
import com.tp.holdem.model.Player
import com.tp.holdem.model.PlayerNumber
import com.tp.holdem.model.PokerTable
import io.vavr.collection.HashMap
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2

fun PokerTable.nextPhase(): PokerTable {
    return when (phase.nextPhase()) {
        Phase.PRE_FLOP -> goToPreFlopPhase()
        Phase.FLOP -> goToNextPhase(3)
        Phase.TURN, Phase.RIVER -> goToNextPhase(1)
        else -> throw IllegalStateException("There is no next phase")
    }
}

private fun PokerTable.goToPreFlopPhase(): PokerTable {
    val dealerPlayer = getDealer().assertFound()
    val bettingPlayer = dealerPlayer.firstBetInRound(this)

    return this.copy(
            phase = phase.nextPhase(),
            allPlayers = allPlayers.replace(dealerPlayer, bettingPlayer),
            bettingPlayer = PlayerNumber.of(bettingPlayer.number),
            movesThisPhase = HashMap.empty()
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
            movesThisPhase = movesThisPhase.filterValues { it.goingToNextPhase() }
    )

    return updatedTable.nextPlayerToBetAfter(bigBlind.number)
}

fun PokerTable.nextPlayerToBet(): PokerTable {
    return nextPlayerToBetAfter(bettingPlayer.number)
}

private fun PokerTable.nextPlayerToBetAfter(playerNumber: Int): PokerTable {
    log.debug(String.format("Finding next to bet after: %d", playerNumber))

    val bettingPlayerIndex = allPlayers.indexOfNumber(playerNumber)
    var newBettingPlayer: Player
    var count = 1
    do {
        newBettingPlayer = allPlayers.get((bettingPlayerIndex + count++) % allPlayers.size())
    } while (newBettingPlayer.folded)

    val modifiedNewBettingPlayer = newBettingPlayer.betInPhase(this)

    return this.copy(
            allPlayers = allPlayers.replace(newBettingPlayer, modifiedNewBettingPlayer),
            bettingPlayer = PlayerNumber.of(modifiedNewBettingPlayer.number)
    )
}

fun PokerTable.phaseStatus(): PhaseStatus {
    val playersInGame = allPlayers.inGame()

    val notAllInCount = playersInGame.count { player -> !player.allIn }
    val allPlayersMoved = movesThisPhase.size() == playersInGame.size()

    if (notAllInCount <= 1 && allPlayersMoved)
        return PhaseStatus.EVERYBODY_ALL_IN

    val notFoldedPlayers = playersInGame.notFolded()
    val notFoldedCount = notFoldedPlayers.size()

    if (notFoldedCount == 1)
        return PhaseStatus.ROUND_OVER

    val allBetsAreEqual = notFoldedPlayers
            .forAll { player -> player.allIn || player.betAmountThisPhase == highestBetThisPhase() }

    val readyForNext = allPlayersMoved && allBetsAreEqual
    return when {
        readyForNext && phase == Phase.RIVER -> PhaseStatus.ROUND_OVER
        readyForNext -> PhaseStatus.READY_FOR_NEXT
        else -> PhaseStatus.KEEP_GOING
    }
}